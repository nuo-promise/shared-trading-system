package cn.suparking.invoice.rabbitmq.messagehandler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageObj {
    private String groupId;
}
