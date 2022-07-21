package cn.suparking.data.api.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCustomer {

    private String id;

    private String discountNo;

    private String projectNo;

    private String merchantNo;

    private String valueType;

    private Integer value;

    private Long createTime;

    private Long effectDate;

    private Long expireDate;

    private Integer usedCount;

    private Integer maxAvailableCount;

    private String usedPlateNo;

    private Long usedTime;

    private String remark;

    private Boolean enabled;

    private String batchId;

    private String discountCategoryId;

    private String discountCategoryName;


}
