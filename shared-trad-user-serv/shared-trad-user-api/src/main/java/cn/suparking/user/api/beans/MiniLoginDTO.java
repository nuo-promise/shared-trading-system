package cn.suparking.user.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Mini login dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniLoginDTO implements Serializable {

    private static final long serialVersionUID = -2597698266148254564L;

    @NotNull
    @NotBlank
    private String code;

}

