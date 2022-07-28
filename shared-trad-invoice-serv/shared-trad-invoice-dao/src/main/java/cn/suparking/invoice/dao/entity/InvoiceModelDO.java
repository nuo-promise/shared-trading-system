package cn.suparking.invoice.dao.entity;

import api.beans.InvoiceModelDTO;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceModelDO implements Serializable {

    private static final long serialVersionUID = 3914877750106917179L;

    /**
     * 发票记录ID.
     */
    private Long id;

    /**
     * 用户ID.
     */
    private Long userId;

    /**
     * 场库编号.
     */
    private String projectNo;

    /**
     * 购方企业名称.
     */
    private String buyername;

    /**
     * 购方企业税号.
     */
    private String taxnum;

    /**
     * 购方企业地址.
     */
    private String address;

    /**
     * 购方企业银行开户行及账号.
     */
    private String account;

    /**
     * 购方企业电话.
     */
    private String telephone;

    /**
     * 订单号.
     */
    private String orderno;

    /**
     * 单据时间.
     */
    private Date invoicedate;

    /**
     * 销方企业税号.
     */
    private String saletaxnum;

    /**
     * 销方企业银行开户行及账号.
     */
    private String saleaccount;

    /**
     * 销方企业电话.
     */
    private String salephone;

    /**
     * 销方企业地址.
     */
    private String saleaddress;

    /**
     * 发票类型，1:正票;2：红票.
     */
    private String kptype;

    /**
     * 开票人.
     */
    private String clerk;

    /**
     * 收款人.
     */
    private String payee;

    /**
     * 复核人.
     */
    private String checker;

    /**
     * 推送方式 -1 不推送 0 邮箱 1 手机  2 邮箱&手机.
     */
    private String tsfs;

    /**
     * 推送邮箱.
     */
    private String email;

    /**
     * 推送手机号.
     */
    private String phone;

    /**
     * 发票请求流水号.
     */
    private String fpqqlsh;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;

    /**
     * build InvoiceModelDO.
     *
     * @param invoiceModelDTO {@link InvoiceModelDTO}
     * @return {@link InvoiceModelDO}
     */
    public static InvoiceModelDO buildCarGroupOrderDO(final InvoiceModelDTO invoiceModelDTO) {
        return Optional.ofNullable(invoiceModelDTO).map(item -> {
            InvoiceModelDO invoiceModelDO = InvoiceModelDO.builder()
                    .userId(item.getUserId())
                    .projectNo(item.getProjectNo())
                    .buyername(item.getBuyername())
                    .taxnum(item.getTaxnum())
                    .address(item.getAddress())
                    .account(item.getAccount())
                    .telephone(item.getTelephone())
                    .orderno(item.getOrderno())
                    .invoicedate(item.getInvoicedate())
                    .saletaxnum(item.getSaletaxnum())
                    .saleaccount(item.getSaleaccount())
                    .salephone(item.getPhone())
                    .kptype(item.getKptype())
                    .clerk(item.getClerk())
                    .payee(item.getPayee())
                    .checker(item.getChecker())
                    .tsfs(item.getTsfs())
                    .email(item.getEmail())
                    .phone(item.getPhone())
                    .fpqqlsh(item.getFpqqlsh())
                    .build();
            if (Objects.isNull(item.getId())) {
                invoiceModelDO.setId(SnowflakeConfig.snowflakeId());
            } else {
                invoiceModelDO.setId(item.getId());
            }
            return invoiceModelDO;
        }).orElse(null);
    }
}
