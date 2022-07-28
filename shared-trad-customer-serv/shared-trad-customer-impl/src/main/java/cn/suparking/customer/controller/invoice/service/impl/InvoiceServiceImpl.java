package cn.suparking.customer.controller.invoice.service.impl;

import api.beans.InvoiceDetail;
import api.beans.InvoiceModelDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.api.beans.invoice.InvoiceModelQueryDTO;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.controller.invoice.service.InvoiceService;
import cn.suparking.customer.feign.invoice.InvoiceTemplateService;
import cn.suparking.customer.tools.SignUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceTemplateService invoiceTemplateService;

    @Value("${sparking.invoice.url}")
    private String invoiceUrl;

    public InvoiceServiceImpl(final InvoiceTemplateService invoiceTemplateService) {
        this.invoiceTemplateService = invoiceTemplateService;
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
        if (!SignUtils.invoke(sign, invoiceModelQueryDTO.getOrderno())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        //rpc开票请求
        // invoiceTemplateService.makeInvoiceModel(invoiceModelQueryDTO);



        //发送开票申请,获取开票数据
        JSONObject makeResult = HttpUtils.sendPost(invoiceUrl + ParkConstant.INTERFACE_INVOICE_MAKE, JSON.toJSONString(invoiceModelQueryDTO));
        if (Objects.isNull(makeResult) || !ParkConstant.SUCCESS.equals(makeResult.getString("code")) || Objects.isNull(makeResult.getJSONObject("invoiceModel"))) {
            log.warn("请求获取开票信息失败 [{}]", JSONObject.toJSONString(makeResult));
            return SpkCommonResult.error("请求获取开票信息失败");
        }

        //组织返回数据
        JSONObject invoiceModel = makeResult.getJSONObject("invoiceModel");
        String makeInvoiceData = makeResult.getString("makeInvoiceData");
        InvoiceModelDTO invoiceModelDTO = JSONObject.toJavaObject(invoiceModel, InvoiceModelDTO.class);
        JSONArray detail = invoiceModel.getJSONArray("detail");
        List<InvoiceDetail> invoiceDetailList = JSONArray.parseArray(JSONObject.toJSONString(detail), InvoiceDetail.class);
        invoiceModelDTO.setInvoiceDetailList(invoiceDetailList);

        invoiceModelDTO.setUserId(invoiceModelQueryDTO.getUserId());
        invoiceModelDTO.setProjectNo(invoiceModelQueryDTO.getProjectNo());
        invoiceModelDTO.setMakeInvoiceData(makeInvoiceData);

        return invoiceTemplateService.makeInvoiceModel(invoiceModelDTO);
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
        // // 校验 sign
        // if (!SignUtils.invoke(sign, String.valueOf(invoiceSourceDTO.getUserId()))) {
        //     return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        // }
        return invoiceTemplateService.getInvoiceSource(invoiceSourceDTO);
    }
}
