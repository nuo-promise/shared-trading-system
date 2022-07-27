package cn.suparking.invoice.service.impl;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceSourceDTO;
import api.beans.ProjectConfig;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import cn.suparking.invoice.dao.mapper.InvoiceSourceMapper;
import cn.suparking.invoice.service.InvoiceSourceService;
import cn.suparking.invoice.tools.InvoiceConstant;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupRefundOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingRefundOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class InvoiceSourceServiceImpl implements InvoiceSourceService {

    private final InvoiceSourceMapper invoiceSourceMapper;

    public InvoiceSourceServiceImpl(final InvoiceSourceMapper invoiceSourceMapper) {
        this.invoiceSourceMapper = invoiceSourceMapper;
    }

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public List<InvoiceSourceDO> invoiceSourceList(final InvoiceSourceDTO invoiceSourceDTO) {
        return invoiceSourceMapper.list(invoiceSourceDTO);
    }

    /**
     * 获取开票订单列表模糊订单号.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public List<InvoiceSourceDO> invoiceSourceListLikeOrderNo(final InvoiceSourceDTO invoiceSourceDTO) {
        return invoiceSourceMapper.listLikeOrderNo(invoiceSourceDTO);
    }

    /**
     * 新增 | 修改 发票抬头.
     *
     * @param invoiceSourceDTO 发票抬头请求信息
     * @return Integer
     */
    @Override
    public Integer createOrUpdate(final InvoiceSourceDTO invoiceSourceDTO) {
        InvoiceSourceDO invoiceSourceDO = InvoiceSourceDO.buildCarGroupOrderDO(invoiceSourceDTO);
        if (ObjectUtils.isEmpty(invoiceSourceDTO.getId())) {
            return invoiceSourceMapper.insert(invoiceSourceDO);
        } else {
            return invoiceSourceMapper.update(invoiceSourceDO);
        }
    }

    @Override
    public Integer deleteById(final Long id) {
        return invoiceSourceMapper.deleteById(id);
    }

    @Override
    public Integer createOrUpdateParkingOrderInvoice(final ParkingOrderDTO parkingOrderDTO) {
        try {
            if (Objects.isNull(parkingOrderDTO)) {
                log.error("parkingOrderDTO is null");
                return -1;
            }
            if (Objects.nonNull(parkingOrderDTO.getRefundState()) && !parkingOrderDTO.getRefundState().equals(InvoiceConstant.REFUND_NONE)) {
                log.info("parkingOrderDTO is refund, ignore");
                return 0;
            }

            String invoiceState = parkingOrderDTO.getInvoiceState();
            if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                // 默认处理手动开票标记.
                InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                        .sourceId(parkingOrderDTO.getId())
                        .build();
                List<InvoiceSourceDO> invoiceSourceDOList = invoiceSourceList(invoiceSourceDTO);
                if (Objects.nonNull(invoiceSourceDOList) && invoiceSourceDOList.size() > 0) {
                    invoiceSourceDOList.forEach(item -> {
                        item.setState(true);
                        invoiceSourceMapper.update(item);
                    });
                }
                return 0;
            }

            if (parkingOrderDTO.getReceivedAmount() > 0 && StringUtils.isNotEmpty(parkingOrderDTO.getUserId())) {
                log.info("用户: " + parkingOrderDTO.getUserId() + " 开票金额: " + parkingOrderDTO.getReceivedAmount() + "生成开票信息");
                if (parkingOrderDTO.getReceivedAmount() > 100000) {
                    int count = parkingOrderDTO.getReceivedAmount() / 100000;
                    for (int i = 0; i < count; i++) {
                        InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                                .userId(Long.valueOf(parkingOrderDTO.getUserId()))
                                .orderNo(parkingOrderDTO.getOrderNo() + "@" + i)
                                .payAmount(100000)
                                .payType(Objects.isNull(parkingOrderDTO.getPayType()) ? "OTHER" : parkingOrderDTO.getPayType())
                                .projectNo(parkingOrderDTO.getProjectNo())
                                .sourceDoc(InvoiceConstant.SOURCE_PARKING)
                                .sourceId(parkingOrderDTO.getId())
                                .startTime(DateUtils.secondToDateTime(parkingOrderDTO.getBeginTime()))
                                .endTime(DateUtils.secondToDateTime(parkingOrderDTO.getEndTime()))
                                .build();
                        boolean state = false;
                        if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                            String invoiceOrderNo = parkingOrderDTO.getInvoiceOrderNo();
                            if (StringUtils.isEmpty(invoiceOrderNo)) {
                                state = true;
                            } else {
                                if (invoiceSourceDTO.getOrderNo().equals(invoiceOrderNo)) {
                                    state = true;
                                }
                            }
                            invoiceSourceDTO.setState(state);
                        }
                        createOrUpdate(invoiceSourceDTO);
                        log.info("根据ParkingOrder生成开票信息: " + invoiceSourceDTO.toString());
                    }
                    if (parkingOrderDTO.getReceivedAmount() % 100000 > 0) {
                        InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                                .userId(Long.valueOf(parkingOrderDTO.getUserId()))
                                .orderNo(parkingOrderDTO.getOrderNo())
                                .payAmount(parkingOrderDTO.getReceivedAmount() % 100000)
                                .payType(Objects.isNull(parkingOrderDTO.getPayType()) ? "OTHER" : parkingOrderDTO.getPayType())
                                .projectNo(parkingOrderDTO.getProjectNo())
                                .sourceDoc(InvoiceConstant.SOURCE_PARKING)
                                .sourceId(parkingOrderDTO.getId())
                                .startTime(DateUtils.secondToDateTime(parkingOrderDTO.getBeginTime()))
                                .endTime(DateUtils.secondToDateTime(parkingOrderDTO.getEndTime()))
                                .build();
                        boolean state = false;
                        if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                            String invoiceOrderNo = parkingOrderDTO.getInvoiceOrderNo();
                            if (StringUtils.isEmpty(invoiceOrderNo)) {
                                state = true;
                            } else {
                                if (invoiceSourceDTO.getOrderNo().equals(invoiceOrderNo)) {
                                    state = true;
                                }
                            }
                            invoiceSourceDTO.setState(state);
                        }
                        createOrUpdate(invoiceSourceDTO);
                        log.info("根据ParkingOrder生成开票信息: " + invoiceSourceDTO.toString());
                    }
                } else {
                    InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                            .userId(Long.valueOf(parkingOrderDTO.getUserId()))
                            .orderNo(parkingOrderDTO.getOrderNo())
                            .payAmount(parkingOrderDTO.getReceivedAmount())
                            .payType(Objects.isNull(parkingOrderDTO.getPayType()) ? "OTHER" : parkingOrderDTO.getPayType())
                            .projectNo(parkingOrderDTO.getProjectNo())
                            .sourceDoc(InvoiceConstant.SOURCE_PARKING)
                            .sourceId(parkingOrderDTO.getId())
                            .startTime(DateUtils.secondToDateTime(parkingOrderDTO.getBeginTime()))
                            .endTime(DateUtils.secondToDateTime(parkingOrderDTO.getEndTime()))
                            .build();
                    boolean state = false;
                    if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                        state = true;
                    }
                    invoiceSourceDTO.setState(state);
                    createOrUpdate(invoiceSourceDTO);
                    log.info("根据ParkingOrder生成开票信息: " + invoiceSourceDTO.toString());
                }
            }
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            return -1;
        }
        return 0;
    }

    @Override
    public Integer refundParkingOrderInvoice(final ParkingRefundOrderDTO parkingRefundOrderDTO) {
        try {
            if (!parkingRefundOrderDTO.getOrderState().equals(InvoiceConstant.CAR_GROUP_ORDER_SUCCESS)) {
                log.warn("临停订单:" + parkingRefundOrderDTO.getOrderNo() + ",订单状态不为SUCCESS, 忽略");
                return -1;
            }

            int refundAmount = Objects.isNull(parkingRefundOrderDTO.getRefundAmount()) ? 0 : parkingRefundOrderDTO.getRefundAmount();
            if (refundAmount == 0) {
                log.warn("临停订单:" + parkingRefundOrderDTO.getOrderNo() + ",退款金额为0, 忽略");
                return -1;
            }

            InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                    .orderNo(parkingRefundOrderDTO.getOrderNo())
                    .sourceDoc(InvoiceConstant.SOURCE_PARKING)
                    .build();
            List<InvoiceSourceDO> invoiceSourceDOList = invoiceSourceListLikeOrderNo(invoiceSourceDTO);
            if (CollectionUtils.isEmpty(invoiceSourceDOList)) {
                log.warn("临停订单:" + parkingRefundOrderDTO.getOrderNo() + ",没有开票信息, 忽略");
                return -1;
            }
            int count = invoiceSourceDOList.size();
            if (count == 1) {
                InvoiceSourceDO invoiceSourceDO = invoiceSourceDOList.get(0);
                int payAmount = invoiceSourceDO.getPayAmount();
                if (refundAmount >= payAmount) {
                    log.info("临停订单:" + parkingRefundOrderDTO.getOrderNo() + ",退款金额大于等于开票金额, 删除开票信息");
                    deleteById(invoiceSourceDO.getId());
                } else {
                    log.info("临停订单:" + parkingRefundOrderDTO.getOrderNo() + ",退款金额小于开票金额, 更新开票信息");
                    invoiceSourceDO.setPayAmount(payAmount - refundAmount);
                    invoiceSourceMapper.update(invoiceSourceDO);
                }
                return 0;
            }
            // 如果存在多条.
            if (count > 1) {
                InvoiceSourceDO invoiceSourceDO = invoiceSourceDOList.get(0);
                Integer maxRefundAmount = Objects.isNull(parkingRefundOrderDTO.getMaxRefundAmount()) ? 0 : parkingRefundOrderDTO.getMaxRefundAmount();
                int between = maxRefundAmount - refundAmount;
                // 现将原有发票数据删除.
                invoiceSourceDOList.forEach(item -> {
                    deleteById(item.getId());
                });
                if (between > 0) {
                    ProjectConfig projectConfig = projectConfigService.getProjectConfig(parkingRefundOrderDTO.getProjectNo());
                    Integer invoiceLimit = projectConfig.getInvoiceConfig().getInvoiceLimit();

                    if (Objects.isNull(invoiceLimit)) {
                        count = between / 100000;
                        if (between > 100000) {
                            for (int i = 0; i < count; i++) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(100000)
                                        .orderNo(parkingRefundOrderDTO.getPayOrderNo() + "@" + i)
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("临停退费订单:" + parkingRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                            if (between % 100000 > 0) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(between % 100000)
                                        .orderNo(parkingRefundOrderDTO.getPayOrderNo())
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("临停退费订单:" + parkingRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                        }
                    } else {
                        count = between / invoiceLimit;
                        if (between > invoiceLimit) {
                            for (int i = 0; i < count; i++) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(invoiceLimit)
                                        .orderNo(parkingRefundOrderDTO.getPayOrderNo() + "@" + i)
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("临停退费订单:" + parkingRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                            if (between % invoiceLimit > 0) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(between % invoiceLimit)
                                        .orderNo(parkingRefundOrderDTO.getPayOrderNo())
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("临停退费订单:" + parkingRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            return -1;
        }
        return 0;
    }

    @Override
    public Integer createOrUpdateCarGroupOrderInvoice(final CarGroupOrderDTO carGroupOrderDTO) {
        try {
            if (Objects.nonNull(carGroupOrderDTO.getRefundState()) && !carGroupOrderDTO.getRefundState().equals(InvoiceConstant.REFUND_NONE)) {
                log.info("parkingOrderDTO is refund, ignore");
                return 0;
            }

            String invoiceState = carGroupOrderDTO.getInvoiceState();
            if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                // 默认处理手动开票标记.
                InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                        .sourceId(carGroupOrderDTO.getId())
                        .build();
                List<InvoiceSourceDO> invoiceSourceDOList = invoiceSourceList(invoiceSourceDTO);
                if (Objects.nonNull(invoiceSourceDOList) && invoiceSourceDOList.size() > 0) {
                    invoiceSourceDOList.forEach(item -> {
                        item.setState(true);
                        invoiceSourceMapper.update(item);
                    });
                }
                return 0;
            }

            if (carGroupOrderDTO.getDueAmount() <= 0) {
                log.info("carGroupOrderDTO is due, ignore");
                return 0;
            }

            String orderState = carGroupOrderDTO.getOrderState();
            if (!orderState.equals(InvoiceConstant.CAR_GROUP_ORDER_SUCCESS)) {
                log.info("carGroupOrderDTO is not success, ignore");
                return 0;
            }
            log.info("用户: " + carGroupOrderDTO.getUserId() + " 开票金额: " + carGroupOrderDTO.getDueAmount() + "生成开票信息");
            if (carGroupOrderDTO.getDueAmount() > 100000) {
                int count = carGroupOrderDTO.getDueAmount() / 100000;
                for (int i = 0; i < count; i++) {
                    InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                            .userId(carGroupOrderDTO.getUserId())
                            .orderNo(carGroupOrderDTO.getOrderNo() + "@" + i)
                            .payAmount(100000)
                            .payType(Objects.isNull(carGroupOrderDTO.getPayType()) ? "OTHER" : carGroupOrderDTO.getPayType())
                            .projectNo(carGroupOrderDTO.getProjectNo())
                            .sourceDoc(InvoiceConstant.SOURCE_ORDER)
                            .sourceId(carGroupOrderDTO.getId())
                            .startTime(DateUtils.secondToDateTime(carGroupOrderDTO.getBeginTime()))
                            .endTime(DateUtils.secondToDateTime(carGroupOrderDTO.getEndTime()))
                            .protocolId(carGroupOrderDTO.getProtocolId())
                            .build();
                    boolean state = false;
                    if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                        String invoiceOrderNo = carGroupOrderDTO.getInvoiceOrderNo();
                        if (StringUtils.isEmpty(invoiceOrderNo)) {
                            state = true;
                        } else {
                            if (invoiceSourceDTO.getOrderNo().equals(invoiceOrderNo)) {
                                state = true;
                            }
                        }
                        invoiceSourceDTO.setState(state);
                    }
                    createOrUpdate(invoiceSourceDTO);
                    log.info("根据CarGroupOrder生成开票信息: " + invoiceSourceDTO.toString());
                }
                if (carGroupOrderDTO.getDueAmount() % 100000 > 0) {
                    InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                            .userId(carGroupOrderDTO.getUserId())
                            .orderNo(carGroupOrderDTO.getOrderNo())
                            .payAmount(carGroupOrderDTO.getDueAmount() % 100000)
                            .payType(Objects.isNull(carGroupOrderDTO.getPayType()) ? "OTHER" : carGroupOrderDTO.getPayType())
                            .projectNo(carGroupOrderDTO.getProjectNo())
                            .sourceDoc(InvoiceConstant.SOURCE_ORDER)
                            .sourceId(carGroupOrderDTO.getId())
                            .startTime(DateUtils.secondToDateTime(carGroupOrderDTO.getBeginTime()))
                            .endTime(DateUtils.secondToDateTime(carGroupOrderDTO.getEndTime()))
                            .protocolId(carGroupOrderDTO.getProtocolId())
                            .build();
                    boolean state = false;
                    if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                        String invoiceOrderNo = carGroupOrderDTO.getInvoiceOrderNo();
                        if (StringUtils.isEmpty(invoiceOrderNo)) {
                            state = true;
                        } else {
                            if (invoiceSourceDTO.getOrderNo().equals(invoiceOrderNo)) {
                                state = true;
                            }
                        }
                        invoiceSourceDTO.setState(state);
                    }
                    createOrUpdate(invoiceSourceDTO);
                    log.info("根据CarGroupOrder生成开票信息: " + invoiceSourceDTO.toString());
                }
            } else {
                InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                        .userId(Long.valueOf(carGroupOrderDTO.getUserId()))
                        .orderNo(carGroupOrderDTO.getOrderNo())
                        .payAmount(carGroupOrderDTO.getDueAmount())
                        .payType(Objects.isNull(carGroupOrderDTO.getPayType()) ? "OTHER" : carGroupOrderDTO.getPayType())
                        .projectNo(carGroupOrderDTO.getProjectNo())
                        .sourceDoc(InvoiceConstant.SOURCE_ORDER)
                        .sourceId(carGroupOrderDTO.getId())
                        .startTime(DateUtils.secondToDateTime(carGroupOrderDTO.getBeginTime()))
                        .endTime(DateUtils.secondToDateTime(carGroupOrderDTO.getEndTime()))
                        .protocolId(carGroupOrderDTO.getProtocolId())
                        .build();
                boolean state = false;
                if (StringUtils.isNotBlank(invoiceState) && (InvoiceConstant.INVOICE_STATE_PAPER.equals(invoiceState) || InvoiceConstant.INVOICE_STATE_ELECTRONIC.equals(invoiceState))) {
                    state = true;
                }
                invoiceSourceDTO.setState(state);
                createOrUpdate(invoiceSourceDTO);
                log.info("根据CarGroupOrder生成开票信息: " + invoiceSourceDTO.toString());
            }
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            return -1;
        }
        return 0;
    }

    @Override
    public Integer refundCarGroupOrderInvoice(final CarGroupRefundOrderDTO carGroupRefundOrderDTO) {
        try {
            if (!carGroupRefundOrderDTO.getOrderState().equals(InvoiceConstant.CAR_GROUP_ORDER_SUCCESS)) {
                log.warn("合约订单:" + carGroupRefundOrderDTO.getOrderNo() + ",订单状态不为SUCCESS, 忽略");
                return -1;
            }

            int refundAmount = Objects.isNull(carGroupRefundOrderDTO.getRefundAmount()) ? 0 : carGroupRefundOrderDTO.getRefundAmount();
            if (refundAmount == 0) {
                log.warn("合约订单:" + carGroupRefundOrderDTO.getOrderNo() + ",退款金额为0, 忽略");
                return -1;
            }

            InvoiceSourceDTO invoiceSourceDTO = InvoiceSourceDTO.builder()
                    .orderNo(carGroupRefundOrderDTO.getOrderNo())
                    .sourceDoc(InvoiceConstant.SOURCE_ORDER)
                    .build();
            List<InvoiceSourceDO> invoiceSourceDOList = invoiceSourceListLikeOrderNo(invoiceSourceDTO);
            if (CollectionUtils.isEmpty(invoiceSourceDOList)) {
                log.warn("合约订单:" + carGroupRefundOrderDTO.getOrderNo() + ",没有开票信息, 忽略");
                return -1;
            }
            int count = invoiceSourceDOList.size();
            if (count == 1) {
                InvoiceSourceDO invoiceSourceDO = invoiceSourceDOList.get(0);
                int payAmount = invoiceSourceDO.getPayAmount();
                if (refundAmount >= payAmount) {
                    log.info("合约订单:" + carGroupRefundOrderDTO.getOrderNo() + ",退款金额大于等于开票金额, 删除开票信息");
                    deleteById(invoiceSourceDO.getId());
                } else {
                    log.info("合约订单:" + carGroupRefundOrderDTO.getOrderNo() + ",退款金额小于开票金额, 更新开票信息");
                    invoiceSourceDO.setPayAmount(payAmount - refundAmount);
                    invoiceSourceMapper.update(invoiceSourceDO);
                }
                return 0;
            }
            // 如果存在多条.
            if (count > 1) {
                InvoiceSourceDO invoiceSourceDO = invoiceSourceDOList.get(0);
                Integer maxRefundAmount = Objects.isNull(carGroupRefundOrderDTO.getMaxRefundAmount()) ? 0 : carGroupRefundOrderDTO.getMaxRefundAmount();
                int between = maxRefundAmount - refundAmount;
                // 现将原有发票数据删除.
                invoiceSourceDOList.forEach(item -> {
                    deleteById(item.getId());
                });
                if (between > 0) {
                    ProjectConfig projectConfig = projectConfigService.getProjectConfig(carGroupRefundOrderDTO.getProjectNo());
                    Integer invoiceLimit = projectConfig.getInvoiceConfig().getInvoiceLimit();

                    if (Objects.isNull(invoiceLimit)) {
                        count = between / 100000;
                        if (between > 100000) {
                            for (int i = 0; i < count; i++) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(100000)
                                        .orderNo(carGroupRefundOrderDTO.getPayOrderNo() + "@" + i)
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("合约退费订单:" + carGroupRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                            if (between % 100000 > 0) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(between % 100000)
                                        .orderNo(carGroupRefundOrderDTO.getPayOrderNo())
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("合约退费订单:" + carGroupRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                        }
                    } else {
                        count = between / invoiceLimit;
                        if (between > invoiceLimit) {
                            for (int i = 0; i < count; i++) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(invoiceLimit)
                                        .orderNo(carGroupRefundOrderDTO.getPayOrderNo() + "@" + i)
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("合约退费订单:" + carGroupRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                            if (between % invoiceLimit > 0) {
                                InvoiceSourceDTO invoiceSOurceDTO = invoiceSourceDTO.builder()
                                        .userId(invoiceSourceDO.getUserId())
                                        .payAmount(between % invoiceLimit)
                                        .orderNo(carGroupRefundOrderDTO.getPayOrderNo())
                                        .state(invoiceSourceDO.getState())
                                        .projectNo(invoiceSourceDO.getProjectNo())
                                        .projectName(invoiceSourceDO.getProjectName())
                                        .startTime(invoiceSourceDO.getStartTime())
                                        .endTime(invoiceSourceDO.getEndTime())
                                        .payType(invoiceSourceDO.getPayType())
                                        .payChannel(invoiceSourceDO.getPayChannel())
                                        .payTime(invoiceSourceDO.getPayTime())
                                        .sourceDoc(invoiceSourceDO.getSourceDoc())
                                        .sourceId(invoiceSourceDO.getSourceId())
                                        .protocolId(invoiceSourceDO.getProtocolId())
                                        .invoiceCode(invoiceSourceDO.getInvoiceCode())
                                        .termNo(invoiceSourceDO.getTermNo())
                                        .operator(invoiceSourceDO.getOperator())
                                        .build();
                                createOrUpdate(invoiceSourceDTO);
                                log.info("合约退费订单:" + carGroupRefundOrderDTO.getOrderNo() + ",大于100000重新生成开票信息: " + invoiceSOurceDTO.toString());
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            return -1;
        }
        return 0;
    }
}
