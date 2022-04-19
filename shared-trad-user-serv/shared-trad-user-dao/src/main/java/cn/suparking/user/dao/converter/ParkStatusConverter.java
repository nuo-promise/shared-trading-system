package cn.suparking.user.dao.converter;

import cn.suparking.user.api.enums.ParkStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ParkStatusConverter implements AttributeConverter<ParkStatus, String> {
    @Override
    public String convertToDatabaseColumn(final ParkStatus parkStatus) {
        return parkStatus.getCode();
    }

    @Override
    public ParkStatus convertToEntityAttribute(final String code) {
        return ParkStatus.convert(code);
    }
}
