package cn.suparking.invoice.service.impl;

import api.beans.InvoiceDetail;
import api.beans.InvoiceModelDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.HttpInvoice;
import cn.suparking.invoice.dao.entity.InvoiceDetailDO;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import cn.suparking.invoice.dao.mapper.InvoiceDetailMapper;
import cn.suparking.invoice.dao.mapper.InvoiceModelMapper;
import cn.suparking.invoice.service.InvoiceModelService;
import cn.suparking.invoice.tools.InvoiceConstant;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InvoiceModelServiceImpl implements InvoiceModelService {

    private final InvoiceModelMapper invoiceModelMapper;
    private final InvoiceDetailMapper invoiceDetailMapper;
    @Value("${sparking.invoice.nnfpurl}")
    private String nnfpurl;

    public InvoiceModelServiceImpl(final InvoiceModelMapper invoiceModelMapper, final InvoiceDetailMapper invoiceDetailMapper) {
        this.invoiceModelMapper = invoiceModelMapper;
        this.invoiceDetailMapper = invoiceDetailMapper;
    }

    /**
     * 开票.
     *
     * @param invoiceModelDTO {@linkplain InvoiceModelDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult makeInvoiceModel(final InvoiceModelDTO invoiceModelDTO) {
        //请求诺诺开发票
        String makeInvoiceData = invoiceModelDTO.getMakeInvoiceData();

        JSONObject result = HttpInvoice.requestMethod(nnfpurl, makeInvoiceData);
        if (Objects.isNull(result) || !InvoiceConstant.SUCCESS.equals(result.getString("status"))) {
            log.warn("请求获取开票失败 [{}]", JSONObject.toJSONString(result));
            return SpkCommonResult.error("请求开票失败");
        }
        //赋值流水编号，存储数据
        String fpqqlsh = result.getString("fpqqlsh");
        invoiceModelDTO.setFpqqlsh(fpqqlsh);

        InvoiceModelDO invoiceModelDO = InvoiceModelDO.buildCarGroupOrderDO(invoiceModelDTO);
        List<InvoiceDetailDO> invoiceDetailDOList = new ArrayList<>();
        //开票详情
        List<InvoiceDetail> invoiceDetailList = invoiceModelDTO.getInvoiceDetailList();
        for (InvoiceDetail invoiceDetail : invoiceDetailList) {
            invoiceDetail.setInvoiceModelId(invoiceModelDO.getId());
            InvoiceDetailDO invoiceDetailDO = InvoiceDetailDO.buildCarGroupOrderDO(invoiceDetail);
            invoiceDetailDOList.add(invoiceDetailDO);
        }
        Integer insert = invoiceModelMapper.insert(invoiceModelDO);
        Integer insertBatch = invoiceDetailMapper.insertBatch(invoiceDetailDOList);
        if (insert < 1 || insertBatch < 1) {
            log.warn("存储开票信息失败  开票信息 [{}]  详情 [{}]", JSONObject.toJSONString(invoiceModelDO), JSONObject.toJSONString(invoiceDetailDOList));
            return SpkCommonResult.error("存储开票信息失败");
        }
        //启用轮询查开票结果


        return null;
    }
}
