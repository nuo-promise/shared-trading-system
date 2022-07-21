package cn.suparking.data.api.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryOrderTraceInfo {

    private String begin;

    private String end;

    private Integer unitCount;

    private Integer maxAmount;

    private Integer historyEffectiveAmount = 0;

    private LinkedList<HistoryPayment> historyPayments = new LinkedList<>();
}
