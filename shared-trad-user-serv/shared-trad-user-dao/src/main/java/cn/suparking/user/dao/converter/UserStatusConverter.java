package cn.suparking.user.dao.converter;

import cn.suparking.user.api.enums.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(final UserStatus userStatus) {
        return userStatus.getCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(final String code) {
        return UserStatus.convert(code);
    }
}
