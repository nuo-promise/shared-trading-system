package cn.suparking.customer.api.beans.discount;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountUsedDTO {

    private String projectNo;

    private String orderNo;

    private JSONObject discountInfo;

    private String userId;

}
