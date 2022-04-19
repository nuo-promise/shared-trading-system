package cn.suparking.user.dao.converter;

import cn.suparking.user.api.enums.DeviceType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DeviceTypeConverter implements AttributeConverter<DeviceType, String> {
    @Override
    public String convertToDatabaseColumn(final DeviceType deviceType) {
        return deviceType.getCode();
    }

    @Override
    public DeviceType convertToEntityAttribute(final String code) {
        return DeviceType.convert(code);
    }
}
