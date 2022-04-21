package cn.suparking.user.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Car Park DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarParkDTO implements Serializable {

    private static final long serialVersionUID = 275245010953440452L;

    private String id;

    @NotNull
    @NotBlank
    private String userId;

    @NotNull
    @NotBlank
    private String parkNo;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    @NotBlank
    private String parkId;

    @NotNull
    private Integer status;

    private String deviceId;

    private Integer deviceStatus;

    private Integer deviceType;
}
