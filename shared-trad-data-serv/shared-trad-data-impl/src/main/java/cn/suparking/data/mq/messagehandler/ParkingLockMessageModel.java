package cn.suparking.data.mq.messagehandler;

import cn.suparking.data.api.beans.ParkingLockModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Message;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLockMessageModel {

    private Message message;

    private ParkingLockModel parkingLockModel;
}
