package cn.suparking.customer.controller.invoice.service.impl;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceModelQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.controller.invoice.service.InvoiceService;
import cn.suparking.customer.controller.park.service.impl.ParkServiceImpl;
import cn.suparking.customer.feign.invoice.InvoiceTemplateService;
import cn.suparking.customer.tools.SignUtils;
import cn.suparking.customer.vo.park.ParkInfoVO;
import cn.suparking.invoice.dao.vo.InvoiceModelVO;
import cn.suparking.invoice.dao.vo.InvoiceSourceVO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceTemplateService invoiceTemplateService;

    private final ParkServiceImpl parkServiceImpl;

    @Resource
    private SharedProperties sharedProperties;

    public InvoiceServiceImpl(final InvoiceTemplateService invoiceTemplateService, final ParkServiceImpl parkServiceImpl) {
        this.invoiceTemplateService = invoiceTemplateService;
        this.parkServiceImpl = parkServiceImpl;
    }

    /**
     * 申请开票.
     *
     * @param sign                 秘钥
     * @param invoiceModelQueryDTO {@link InvoiceModelQueryDTO}
     * @return {@link SpkCommonResult}
     */
    @Override
    public SpkCommonResult makeInvoiceModel(final String sign, final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        // 校验 sign
        if (!SignUtils.invoke(sign, invoiceModelQueryDTO.getProjectNo())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        return invoiceTemplateService.makeInvoiceModel(invoiceModelQueryDTO);
    }

    /**
     * 获取可开票列表.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    @Override
    public SpkCommonResult getInvoiceSource(final String sign, final InvoiceSourceDTO invoiceSourceDTO) {
        // 校验 sign
        if (!SignUtils.invoke(sign, String.valueOf(invoiceSourceDTO.getUserId()))) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        Map<String, List<InvoiceSourceVO>> invoiceSourceMap = new HashMap<>();
        long currentSecond = DateUtils.getCurrentSecond();
        Timestamp begin = DateUtils.getBeforeTimestampDay(currentSecond * 1000, sharedProperties.getOrderInterval());
        Timestamp end = new Timestamp(currentSecond * 1000);
        invoiceSourceDTO.setBeginDate(begin.getTime() / 1000);
        invoiceSourceDTO.setEndDate(end.getTime() / 1000);
        if ("VIP".equals(invoiceSourceDTO.getType())) {
            invoiceSourceDTO.setSourceDoc("ORDER");
        }
        if ("TEMP".equals(invoiceSourceDTO.getType())) {
            invoiceSourceDTO.setSourceDoc("PARKING");
        }

        Map<String, ParkInfoVO> parkInfoMap = parkServiceImpl.allLocationMap();
        List<InvoiceSourceVO> invoiceSource = invoiceTemplateService.getInvoiceSource(invoiceSourceDTO);

        if (Objects.nonNull(invoiceSource) && Objects.nonNull(parkInfoMap) && !invoiceSource.isEmpty()) {
            invoiceSource.forEach(item -> {
                ParkInfoVO parkInfoVO = parkInfoMap.get(item.getProjectNo());
                if (Objects.nonNull(parkInfoVO)) {
                    item.setProjectName(parkInfoVO.getProjectName());
                    item.setAddress(parkInfoVO.getAddressSelect());
                    List<InvoiceSourceVO> invoiceSourceList = invoiceSourceMap.get(item.getProjectNo());
                    if (Objects.nonNull(invoiceSourceList)) {
                        invoiceSourceMap.get(item.getProjectNo()).add(item);
                    } else {
                        invoiceSourceList = new ArrayList<>();
                        invoiceSourceList.add(item);
                        invoiceSourceMap.put(item.getProjectNo(), invoiceSourceList);
                    }
                }
            });
        }
        return SpkCommonResult.success(invoiceSourceMap);
    }

    /**
     * 获取开票内容.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    @Override
    public SpkCommonResult getInvoiceContent(final String sign, final InvoiceSourceDTO invoiceSourceDTO) {
        // 校验 sign
        if (!SignUtils.invoke(sign, String.valueOf(invoiceSourceDTO.getProjectNo()))) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        JSONObject result = invoiceTemplateService.getInvoiceContent(invoiceSourceDTO);
        return SpkCommonResult.success(result);
    }

    /**
     * 获取用户开票历史列表.
     *
     * @param sign                 秘钥
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult invoiceModelList(final String sign, final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        // 校验 sign
        if (!SignUtils.invoke(sign, String.valueOf(invoiceModelQueryDTO.getUserId()))) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        long currentSecond = DateUtils.getCurrentSecond();
        Timestamp begin = DateUtils.getBeforeTimestampDay(currentSecond * 1000, sharedProperties.getOrderInterval());
        Timestamp end = new Timestamp(currentSecond * 1000);

        String beginDate = DateUtils.dateToString(new Date(begin.getTime()));
        String endDate = DateUtils.dateToString(new Date(end.getTime()));
        invoiceModelQueryDTO.setBeginDate(beginDate);
        invoiceModelQueryDTO.setEndDate(endDate);

        List<InvoiceModelVO> invoiceModelDOList = invoiceTemplateService.invoiceModelList(invoiceModelQueryDTO);
        return SpkCommonResult.success(invoiceModelDOList);
    }

    /**
     * 根据开票历史记录 查询对应的开票订单.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult getInvoiceSourceByNo(final String sign, final InvoiceSourceDTO invoiceSourceDTO) {
        if (Objects.isNull(invoiceSourceDTO.getInvoiceCode())) {
            return SpkCommonResult.error("发票信息编号不能为空");
        }

        // 校验 sign
        if (!SignUtils.invoke(sign, String.valueOf(invoiceSourceDTO.getInvoiceCode()))) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        Map<String, ParkInfoVO> parkInfoMap = parkServiceImpl.allLocationMap();
        List<InvoiceSourceVO> invoiceSourceDOList = invoiceTemplateService.getInvoiceSourceByNo(invoiceSourceDTO);

        if (Objects.nonNull(invoiceSourceDOList) && Objects.nonNull(parkInfoMap) && !invoiceSourceDOList.isEmpty()) {
            invoiceSourceDOList.forEach(item -> {
                ParkInfoVO parkInfoVO = parkInfoMap.get(item.getProjectNo());
                if (Objects.nonNull(parkInfoVO)) {
                    item.setProjectName(parkInfoVO.getProjectName());
                    item.setAddress(parkInfoVO.getAddressSelect());
                }
            });
        }
        return SpkCommonResult.success(invoiceSourceDOList);
    }

    /**
     * 删除常用地址.
     *
     * @param sign                秘钥
     * @param invoiceInfoQueryDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult deleteInvoiceInfo(final String sign, final InvoiceInfoQueryDTO invoiceInfoQueryDTO) {
        // 校验 sign
        if (!SignUtils.invoke(sign, invoiceInfoQueryDTO.getUserId())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        InvoiceInfoDTO invoiceInfoDTO = InvoiceInfoDTO.builder().userId(Long.valueOf(invoiceInfoQueryDTO.getUserId())).id(Long.valueOf(invoiceInfoQueryDTO.getId())).build();
        Integer count = invoiceTemplateService.deleteInvoiceInfo(invoiceInfoDTO);
        if (count > 0) {
            return SpkCommonResult.success();
        }
        return SpkCommonResult.error("删除地址失败");
    }
}
