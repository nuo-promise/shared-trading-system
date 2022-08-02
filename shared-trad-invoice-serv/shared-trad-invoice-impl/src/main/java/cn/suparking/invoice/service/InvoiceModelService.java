package cn.suparking.invoice.service;

import api.beans.InvoiceModelQueryDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import cn.suparking.invoice.dao.vo.InvoiceModelVO;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface InvoiceModelService {

    /**
     * 开票.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult makeInvoiceModel(InvoiceModelQueryDTO invoiceModelQueryDTO);

    /**
     * 开票结果查询.
     *
     * @param ffqqlsh 发票请求流水号
     * @return {@linkplain JSONObject}
     */
    JSONObject invoiceQueryByFpqqlsh(String ffqqlsh);

    /**
     * 开票状态修稿.
     *
     * @param invoiceData 开票结果查询返回内容
     * @param userId      用户id
     * @return java.lang.Boolean
     */
    Boolean modifyInvoiceModel(JSONObject invoiceData, Long userId);

    /**
     * 获取用户开票历史列表.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceModelVO> invoiceModelList(InvoiceModelQueryDTO invoiceModelQueryDTO);
}
