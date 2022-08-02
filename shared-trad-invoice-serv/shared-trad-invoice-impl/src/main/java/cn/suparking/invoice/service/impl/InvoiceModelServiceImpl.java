package cn.suparking.invoice.service.impl;

import api.beans.InvoiceDetail;
import api.beans.InvoiceModelDTO;
import api.beans.InvoiceModelQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.hutool.core.date.DateUtil;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.invoice.configuration.InvoiceProperties;
import cn.suparking.invoice.dao.entity.InvoiceDetailDO;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import cn.suparking.invoice.dao.mapper.InvoiceDetailMapper;
import cn.suparking.invoice.dao.mapper.InvoiceModelMapper;
import cn.suparking.invoice.dao.mapper.InvoiceSourceMapper;
import cn.suparking.invoice.dao.vo.InvoiceModelVO;
import cn.suparking.invoice.dao.vo.InvoiceSourceVO;
import cn.suparking.invoice.service.InvoiceInfoService;
import cn.suparking.invoice.service.InvoiceModelService;
import cn.suparking.invoice.service.InvoiceStateQueryService;
import cn.suparking.invoice.tools.InvoiceConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.Objects;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("InvoiceModelService")
public class InvoiceModelServiceImpl implements InvoiceModelService {

    private final InvoiceInfoService invoiceInfoService;

    private final InvoiceModelMapper invoiceModelMapper;

    private final InvoiceDetailMapper invoiceDetailMapper;

    private final InvoiceSourceMapper invoiceSourceMapper;

    @Resource
    private InvoiceProperties invoiceProperties;

    public InvoiceModelServiceImpl(final InvoiceInfoService invoiceInfoService, final InvoiceModelMapper invoiceModelMapper,
                                   final InvoiceDetailMapper invoiceDetailMapper, final InvoiceSourceMapper invoiceSourceMapper) {
        this.invoiceInfoService = invoiceInfoService;
        this.invoiceModelMapper = invoiceModelMapper;
        this.invoiceDetailMapper = invoiceDetailMapper;
        this.invoiceSourceMapper = invoiceSourceMapper;
    }

    /**
     * 开票.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult makeInvoiceModel(final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        List<String> orderList = invoiceModelQueryDTO.getLockOrderList().stream().map(InvoiceSourceDTO::getOrderNo).collect(Collectors.toList());
        invoiceModelQueryDTO.setCodeList(orderList);

        //发送开票申请,获取开票结果
        JSONObject makeResult = HttpUtils.sendPost(invoiceProperties.getInvoiceUrl() + InvoiceConstant.INTERFACE_INVOICE_MAKE, JSON.toJSONString(invoiceModelQueryDTO));
        if (Objects.isNull(makeResult) || !InvoiceConstant.SUCCESS.equals(makeResult.getString("code")) || Objects.isNull(makeResult.getJSONObject("invoiceModel"))) {
            log.warn("请求获取开票信息失败 [{}]", JSONObject.toJSONString(makeResult));
            return SpkCommonResult.error("请求获取开票信息失败");
        }

        //组织开票数据
        JSONObject invoiceModel = makeResult.getJSONObject("invoiceModel");

        invoiceModelQueryDTO.setOrderno(invoiceModel.getString("orderno"));
        //更新发票订单:发票信息编号、开票状态

        Integer count = invoiceSourceMapper.updateByCodeList(invoiceModelQueryDTO);
        if (count < 1) {
            log.warn("更新发票订单失败 [{}]", JSONObject.toJSONString(invoiceModelQueryDTO));
            return SpkCommonResult.error("更新发票订单失败");
        }

        //赋值流水编号，存储数据
        String fpqqlsh = makeResult.getString("fpqqlsh");
        InvoiceModelDTO invoiceModelDTO = JSONObject.toJavaObject(invoiceModel, InvoiceModelDTO.class);
        invoiceModelDTO.setUserId(invoiceModelQueryDTO.getUserId());
        invoiceModelDTO.setProjectNo(invoiceModelQueryDTO.getProjectNo());
        invoiceModelDTO.setFpqqlsh(fpqqlsh);
        InvoiceModelDO invoiceModelDO = InvoiceModelDO.buildCarGroupOrderDO(invoiceModelDTO);

        //开票详情
        List<InvoiceDetailDO> invoiceDetailDOList = new ArrayList<>();
        JSONArray detail = invoiceModel.getJSONArray("detail");
        List<InvoiceDetail> invoiceDetailListArr = JSONArray.parseArray(JSONObject.toJSONString(detail), InvoiceDetail.class);
        for (InvoiceDetail invoiceDetail : invoiceDetailListArr) {
            invoiceDetail.setInvoiceModelId(invoiceModelDO.getId());
            InvoiceDetailDO invoiceDetailDO = InvoiceDetailDO.buildCarGroupOrderDO(invoiceDetail);
            invoiceDetailDOList.add(invoiceDetailDO);
        }
        //保存开票记录
        Integer insert = invoiceModelMapper.insert(invoiceModelDO);
        Integer insertBatch = invoiceDetailMapper.insertBatch(invoiceDetailDOList);

        //保存抬头
        if (invoiceModelQueryDTO.getInvoiceHeadSaveStatus()) {
            Integer invoiceInfoInsert = invoiceInfoService.createOrUpdate(invoiceModelQueryDTO.getInvoiceInfo());
            if (invoiceInfoInsert < 1) {
                log.warn("存储开票抬头信息失败  开票抬头 [{}]", JSONObject.toJSONString(invoiceModelQueryDTO.getInvoiceInfo()));
                return SpkCommonResult.error("存储开票抬头信息失败");
            }
        }

        if (insert < 1 || insertBatch < 1) {
            log.warn("存储开票信息失败  开票信息 [{}]  详情 [{}]", JSONObject.toJSONString(invoiceModelDO), JSONObject.toJSONString(invoiceDetailDOList));
            return SpkCommonResult.error("存储开票信息失败");
        }
        //启用轮询查开票结果
        new InvoiceStateQueryService().queryInvoiceState(fpqqlsh, invoiceModelQueryDTO.getUserId());
        return SpkCommonResult.success();
    }

    /**
     * 根据发票请求流水号查询开票结果.
     *
     * @param ffqqlsh 发票请求流水号
     * @return com.alibaba.fastjson.JSONObject
     * @author ZDD
     * @date 2022/7/29 10:25:29
     */
    public JSONObject invoiceQueryByFpqqlsh(final String ffqqlsh) {
        if (StringUtils.isBlank(ffqqlsh)) {
            log.warn("发票请求流水号不能为空");
            return null;
        }
        //发送开票申请,获取开票结果
        JSONObject params = new JSONObject();
        params.put("fpqqlsh", ffqqlsh);
        return HttpUtils.sendPost(invoiceProperties.getInvoiceUrl() + InvoiceConstant.INTERFACE_INVOICE_FIND_INVOICE_RESULT, params.toJSONString());
    }

    /**
     * 开票状态修改.
     *
     * @param invoiceData 开票结果查询返回内容
     * @param userId      用户编号
     * @return java.lang.Boolean
     */
    @Override
    public Boolean modifyInvoiceModel(final JSONObject invoiceData, final Long userId) {
        String state = invoiceData.getString("c_status");
        String fpqqlsh = invoiceData.getString("c_fpqqlsh");
        String invoiceCode = invoiceData.getString("c_orderno");
        //开票成功
        if (InvoiceConstant.INVOICE_STATE_SUCCESS.equals(state)) {
            //获取图片地址保存
            String pdfUrl = invoiceData.getString("c_url");
            String imgUrl = invoiceData.getString("c_imgUrls");
            String pdfPath = uploadInvoiceURL(pdfUrl, fpqqlsh, ".pdf");
            String imgPath = uploadInvoiceURL(imgUrl, fpqqlsh, ".jpg");
            InvoiceModelDO invoiceModelDO = InvoiceModelDO.builder().fpqqlsh(fpqqlsh).imgUrl(imgUrl).pdfUrl(pdfUrl)
                    .imgPath(imgPath).pdfPath(pdfPath).state(state).userId(userId).build();
            invoiceModelDO.setDateUpdated(new Timestamp(System.currentTimeMillis()));
            Integer modelCount = invoiceModelMapper.updateByFpqqlsh(invoiceModelDO);
            Integer sourceCount = invoiceSourceMapper.updateByInvoiceCode(invoiceCode, state, userId);
            return modelCount > 0 && sourceCount > 0;
        }

        //开票中/开票失败
        InvoiceModelDO invoiceModelDO = InvoiceModelDO.builder().fpqqlsh(fpqqlsh).state(state).userId(userId).build();
        invoiceModelDO.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        Integer modelCount = invoiceModelMapper.updateByFpqqlsh(invoiceModelDO);
        //开票失败的订单状态需要改回未开票
        if (InvoiceConstant.INVOICE_STATE_FAILED.equals(state) || InvoiceConstant.INVOICE_STATE_SIGN_FAILED.equals(state)) {
            state = "0";
        }
        Integer sourceCount = invoiceSourceMapper.updateByInvoiceCode(invoiceCode, state, userId);
        return modelCount > 0 && sourceCount > 0;
    }

    /**
     * 获取用户开票历史列表.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain InvoiceModelDO}
     */
    @Override
    public List<InvoiceModelVO> invoiceModelList(final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        List<InvoiceModelVO> invoiceModelVOS = invoiceModelMapper.invoiceModelList(invoiceModelQueryDTO);
        for (InvoiceModelVO invoiceModelVO : invoiceModelVOS) {
            List<InvoiceSourceVO> invoiceCode = invoiceSourceMapper.getInvoiceSourceByInvoiceCode(invoiceModelVO.getOrderno());
            if (Objects.nonNull(invoiceCode)) {
                int amount = 0;
                for (InvoiceSourceVO invoiceSourceVO : invoiceCode) {
                    amount += invoiceSourceVO.getPayAmount();
                }
                invoiceModelVO.setDueAmount(amount);
                invoiceModelVO.setCount(invoiceCode.size());
            }
        }
        return invoiceModelVOS;
    }

    /**
     * 存储图片到本地.
     *
     * @param fileUrl    图片地址
     * @param ffqqlsh    开票请求流水号
     * @param suffixName 后缀名
     * @return java.lang.String
     */
    private String uploadInvoiceURL(final String fileUrl, final String ffqqlsh, final String suffixName) {
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        String finalSavePath = invoiceProperties.getImagePath() + today + "/";
        //文件名  时间+开票请求流水号
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        sb.append("_");
        sb.append(ffqqlsh);
        sb.append(suffixName);

        //图片完整存储路径(绝对路径)
        String absolutePath = finalSavePath + sb;
        //图片的相对路径
        String dbPath = today + "/" + sb;

        try {
            //获取保存文件的上级路径
            String path = absolutePath.substring(0, absolutePath.lastIndexOf("/"));

            File targetFile = new File(path);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            URL url = new URL(fileUrl);
            //此为联系获得⽹络资源的固定格式⽤法，以便后⾯的in变量获得url截取⽹络资源的输⼊流
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(new FileOutputStream(absolutePath));
            //将截取的图⽚的存储在本地地址赋值给out输出流所指定的地址
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
            connection.disconnect();
            return dbPath;
        } catch (Exception e) {
            log.error("保存文件异常  msg = [{}]", e.getMessage());
            return null;
        }
    }
}
