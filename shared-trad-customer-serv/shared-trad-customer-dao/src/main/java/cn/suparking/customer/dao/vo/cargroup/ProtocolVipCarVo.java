package cn.suparking.customer.dao.vo.cargroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolVipCarVo {
    /**
     * id.
     */
    private String id;

    /**
     * 车场编号.
     */
    private String projectNo;

    /**
     * 车辆类型名称.
     */
    private String carTypeName;

    /**
     * 合约协议id.
     */
    private String protocolId;

    /**
     * 合约协议名称.
     */
    private String protocolName;

    /**
     * 合约描述.
     */
    private String protocolDesc;

    /**
     * 合约单价.
     */
    private Integer price;

    /**
     * 库存ID.
     */
    private String stockId;

    /**
     * 库存数量.
     */
    private Integer stockQuantity;

    /**
     * 时间类型.
     */
    private String durationType;

    /**
     * 数量.
     */
    private Integer quantity;
}
