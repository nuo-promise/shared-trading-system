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
     * 车场状态:true启用 false禁用.
     */
    private Boolean status;
}
