package cn.suparking.user.dao.entity;

import cn.suparking.user.api.enums.RegisterType;
import cn.suparking.user.api.enums.UserStatus;
import cn.suparking.user.dao.converter.RegisterTypeConverter;
import cn.suparking.user.dao.converter.UserStatusConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "spk_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "iphone", nullable = false)
    private String iphone;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "enabled", nullable = false)
    @Convert(converter = UserStatusConverter.class)
    private UserStatus enabled;

    @Column(name = "register_type", nullable = false)
    @Convert(converter = RegisterTypeConverter.class)
    private RegisterType registerType;

    @Column(name = "merchant_id", nullable = true)
    private String merchantId;

    @CreatedDate
    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "date_updated", nullable = false)
    private Timestamp dateUpdated;
}
