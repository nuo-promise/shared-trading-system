package cn.suparking.customer.dao.vo.cargroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVipCarVo {
    /**
     * id.
     */
    private String id;

    /**
     * 车场编号.
     */
    private String projectNo;

    /**
     * 车场名称.
     */
    private String projectName;

    /**
     * 车场地址.
     */
    private String address;

    /**
     * 经度.
     */
    private BigDecimal longitude;

    /**
     * 纬度.
     */
    private BigDecimal latitude;

    /**
     * 开放时间.
     */
    private List<String> openTime;

    /**
     * 车场状态:OPENING启用 CLOSED禁用.
     */
    private String status;
}
