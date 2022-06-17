package cn.suparking.data.api.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkQuery implements Serializable {

    private static final long serialVersionUID = 1418454874761375157L;

    // 项目ID
    private Long projectId;

    // 项目编号
    private String projectNo;

    // 车位编号
    private String parkId;
}
