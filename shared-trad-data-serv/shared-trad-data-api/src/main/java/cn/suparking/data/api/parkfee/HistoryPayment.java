package cn.suparking.data.api.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPayment {

    private String userId;

    private String begin;

    private String end;

    private Integer effectiveAmount;
}
