package cn.suparking.user.dao.entity;

import cn.suparking.user.api.enums.DeviceStatus;
import cn.suparking.user.api.enums.DeviceType;
import cn.suparking.user.api.enums.ParkStatus;
import cn.suparking.user.dao.converter.DeviceStatusConverter;
import cn.suparking.user.dao.converter.DeviceTypeConverter;
import cn.suparking.user.dao.converter.ParkStatusConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
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
@Table(name = "spk_car_park")
public class CarParkDO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "park_no", nullable = false)
    private String parkNo;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "park_id", nullable = false)
    private String parkId;

    @Column(name = "park_status", nullable = false)
    @Convert(converter = ParkStatusConverter.class)
    private ParkStatus parkStatus;

    @Column(name = "device_id", nullable = true)
    private String deviceId;

    @Column(name = "device_status", nullable = true)
    @Convert(converter = DeviceStatusConverter.class)
    private DeviceStatus deviceStatus;

    @Column(name = "device_type", nullable = true)
    @Convert(converter = DeviceTypeConverter.class)
    private DeviceType deviceType;

    @CreatedDate
    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "date_updated", nullable = false)
    private Timestamp dateUpdated;


}
