package cn.suparking.customer.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoQueryDTO {
    @NotBlank
    private String projectNo;

    private String parkNo;
}
