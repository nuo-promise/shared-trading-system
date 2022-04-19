package cn.suparking.user.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MerchantWalletDO extends BaseDO {

    private String userId;

    private String merchantId;

    private Long amount;
}
