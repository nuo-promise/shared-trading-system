package cn.suparking.customer.vo.park;

import cn.suparking.customer.model.park.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkInfoVO {

    private String id;

    private String projectName;

    private String projectNo;

    private String addressSelect;

    private Location location;

    private String helpLine;

    private List<String> openTime;

    /**
     * OPENING, CLOSED.
     */
    private String status;

    private String distance;

    /**
     * 空余车位.
     */
    private Integer freePark;

    /**
     * 每小时多少钱.
     */
    private Integer perCharge;

    /**
     * 距离.
     */
    private String value;

}
