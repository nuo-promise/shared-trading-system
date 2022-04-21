package cn.suparking.user.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarLicenseDTO implements Serializable {

    private static final long serialVersionUID = -4740605825334062507L;

    private String id;

    @NotNull
    @NotBlank
    private String userId;

    @NotNull
    @NotBlank
    private String carLicense;

    @NotNull
    private Integer type;

    private String driverId;

}
