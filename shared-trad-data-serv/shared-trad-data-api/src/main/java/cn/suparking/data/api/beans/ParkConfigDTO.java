package cn.suparking.data.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkConfigDTO {

    @NotNull
    @NotBlank
    private String projectNo;

    @NotNull
    @NotBlank
    private String resource;

    @NotNull
    private String data;

    @NotNull
    @NotBlank
    // ADD DEL
    private String type;
}
