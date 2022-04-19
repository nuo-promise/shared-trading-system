package cn.suparking.user.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "spk_merchant")
public class MerchantDO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "merchant_name", nullable = false)
    private String merchantName;

    @Column(name = "iphone", nullable = false)
    private String iphone;

    @Column(name = "merchant_number", nullable = false)
    private String merchantNumber;

    @Column(name = "card_id", nullable = false)
    private String cardId;

    @Column(name = "business_license_id", nullable = false)
    private String businessLicenseId;

    @Column(name = "bank_card", nullable = false)
    private String bankCard;

    @Column(name = "bank_card_type", nullable = false)
    private String bankCardType;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private int enabled;

    @Column(name = "creator", nullable = false)
    private String creator;

    @Column(name = "modify", nullable = true)
    private String modify;

    @CreatedDate
    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "date_updated", nullable = false)
    private Timestamp dateUpdated;
}
