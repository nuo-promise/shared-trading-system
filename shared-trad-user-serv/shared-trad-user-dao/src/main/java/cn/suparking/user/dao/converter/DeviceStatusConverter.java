package cn.suparking.user.dao.converter;

import cn.suparking.user.api.enums.DeviceStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DeviceStatusConverter implements AttributeConverter<DeviceStatus, String> {
    @Override
    public String convertToDatabaseColumn(final DeviceStatus deviceStatus) {
        return deviceStatus.getCode();
    }

    @Override
    public DeviceStatus convertToEntityAttribute(final String code) {
        return DeviceStatus.convert(code);
    }
}
