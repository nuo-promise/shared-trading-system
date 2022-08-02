package cn.suparking.invoice.service;

import cn.suparking.invoice.concurrent.SparkingThreadFactory;
import cn.suparking.invoice.configuration.SharedProperties;
import cn.suparking.invoice.service.impl.InvoiceModelServiceImpl;
import cn.suparking.invoice.tools.BeansManager;
import cn.suparking.invoice.tools.InvoiceConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InvoiceStateQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceStateQueryService.class);

    // 定时器中断线程Map.
    private static final Map<String, ScheduledFuture<?>> FUTUREMAP = new ConcurrentHashMap<>();

    // 线程执行器.
    private static ScheduledThreadPoolExecutor executor;

    private final SharedProperties sharedProperties = BeansManager.getBean("SharedProperties", SharedProperties.class);

    private final InvoiceModelServiceImpl invoiceModelService = BeansManager.getBean("InvoiceModelService", InvoiceModelServiceImpl.class);

    // 开票结果查询变量.
    private String fpqqlsh;

    private Long userId;

    private Integer count = 0;

    public InvoiceStateQueryService() {
        if (Objects.isNull(executor)) {
            executor = new ScheduledThreadPoolExecutor(sharedProperties.getCorePoolSize(), SparkingThreadFactory.create("invoice-state-query", true));
        }
    }

    /**
     * 开票结果查询.
     *
     * @param fpqqlsh 发票请求流水号
     * @param userId  用户编号
     */
    public void queryInvoiceState(final String fpqqlsh, final Long userId) {
        this.fpqqlsh = fpqqlsh;
        this.userId = userId;
        count = 0;
        if (StringUtils.isEmpty(fpqqlsh)) {
            LOG.error("开票结果查询失败,发票请求流水号为空");
            return;
        }

        ScheduledFuture<?> invoiceStateQueryFuture = FUTUREMAP.get(fpqqlsh);
        if (Objects.nonNull(invoiceStateQueryFuture)) {
            LOG.info("开票结果查询失败,执行器中已存在对应开票结果，发票请求流水编号： " + fpqqlsh);
            return;
        }

        invoiceStateQueryFuture = executor.scheduleWithFixedDelay(this::invoiceQuery, sharedProperties.getInitialDelay(), sharedProperties.getQueryInterval(), TimeUnit.SECONDS);
        FUTUREMAP.put(fpqqlsh, invoiceStateQueryFuture);
        LOG.info("发票请求流水号: " + fpqqlsh + " 开始查询开票结果,开启成功...");
    }

    /**
     * 开票结果查询.
     */
    private void invoiceQuery() {
        LOG.info("开票结果查询参数 : " + fpqqlsh);
        JSONObject invoiceStateResult = invoiceModelService.invoiceQueryByFpqqlsh(fpqqlsh);

        if (Objects.isNull(invoiceStateResult)) {
            LOG.error("开票结果查询失败,返回 null,发票请求流水号：" + fpqqlsh);
            return;
        }

        LOG.info("发票请求流水号: " + fpqqlsh + "查询次数: " + count++ + " 开票结果查询返回结果：" + invoiceStateResult.toJSONString());

        String code = invoiceStateResult.getString("code");
        if (!InvoiceConstant.SUCCESS.equals(code)) {
            LOG.error("开票结果查询失败,返回状态码不为00000,发票请求流水号：" + fpqqlsh);
            return;
        }
        JSONArray data = invoiceStateResult.getJSONArray("data");
        if (Objects.isNull(data) || data.size() < 1) {
            LOG.error("开票结果查询失败,返回结果为null,发票请求流水号：" + fpqqlsh);
            return;
        }

        JSONObject invoiceData = data.getJSONObject(0);
        String status = invoiceData.getString("c_status");

        //开票成功
        if (InvoiceConstant.INVOICE_STATE_SUCCESS.equals(status)) {
            LOG.info("发票请求流水号: " + fpqqlsh + " 查询结果：开票完成，开票结果查询结束...");
            // 取消定时任务.
            if (invoiceModelService.modifyInvoiceModel(invoiceData, userId)) {
                LOG.info("发票请求流水号: " + fpqqlsh + " 开票完成,停止线程...");
            }

            FUTUREMAP.get(fpqqlsh).cancel(true);
            FUTUREMAP.remove(fpqqlsh);
        } else if (InvoiceConstant.INVOICE_STATE_FAILED.equals(status) || InvoiceConstant.INVOICE_STATE_SIGN_FAILED.equals(status)) {
            //开票失败
            LOG.info("发票请求流水号: " + fpqqlsh + " 开票失败，开票结果查询结束...");

            if (invoiceModelService.modifyInvoiceModel(invoiceData, userId)) {
                LOG.info("发票请求流水号: " + fpqqlsh + " 修改开票状态完成,停止线程...");
            }
            // 取消定时任务.
            FUTUREMAP.get(fpqqlsh).cancel(true);
            FUTUREMAP.remove(fpqqlsh);
        } else {
            //开票中
            LOG.info("发票请求流水号: " + fpqqlsh + " 发票处理开票中......");
            if (invoiceModelService.modifyInvoiceModel(invoiceData, userId)) {
                LOG.info("发票请求流水号: " + fpqqlsh + " 修改开票状态完成，开票中...");
            }
            //超过最大尝试查询开票结果次数
            if (count > sharedProperties.getInvoiceCount()) {
                // 取消定时任务.
                FUTUREMAP.get(fpqqlsh).cancel(true);
                FUTUREMAP.remove(fpqqlsh);
            }
        }
    }
}
