package cn.suparking.user.dao.converter;

import cn.suparking.user.api.enums.RegisterType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RegisterTypeConverter implements AttributeConverter<RegisterType, String> {
    @Override
    public String convertToDatabaseColumn(final RegisterType registerType) {
        return registerType.getCode();
    }

    @Override
    public RegisterType convertToEntityAttribute(final String code) {
        return RegisterType.convert(code);
    }
}
