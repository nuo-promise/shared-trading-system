package cn.suparking.customer.controller.invoice.service.impl;

import api.beans.InvoiceDetail;
import api.beans.InvoiceModelDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.customer.api.beans.invoice.InvoiceModelQueryDTO;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.controller.invoice.service.InvoiceService;
import cn.suparking.customer.feign.invoice.InvoiceTemplateService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceTemplateService invoiceTemplateService;
    @Resource
    private SharedProperties sharedProperties;
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
        // // 校验 sign
        // if (!invoke(sign, invoiceModelQueryDTO.getOrderNo())) {
        //     return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        // }
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
     * check sign.
     *
     * @param sign     sign.
     * @param deviceNo deviceNo
     * @return Boolean
     */
    private Boolean invoke(final String sign, final String deviceNo) {
        return md5(sharedProperties.getSecret() + deviceNo + DateUtils.currentDate() + sharedProperties.getSecret(), sign);
    }

    /**
     * MD5.
     *
     * @param data  the data
     * @param token the token
     * @return boolean
     */
    private boolean md5(final String data, final String token) {
        String keyStr = DigestUtils.md5Hex(data.toUpperCase()).toUpperCase();
        log.info("Mini MD5 Value: " + keyStr);
        if (keyStr.equals(token)) {
            return true;
        } else {
            log.warn("Mini Current MD5 :" + keyStr + ", Data Token : " + token);
        }
        return false;
    }
}
