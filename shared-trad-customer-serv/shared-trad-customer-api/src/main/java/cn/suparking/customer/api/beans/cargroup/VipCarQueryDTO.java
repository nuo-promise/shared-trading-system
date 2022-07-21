package cn.suparking.customer.api.beans.cargroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VipCarQueryDTO {

    /**
     * id.
     */
    private String userId;
}
