package cn.suparking.data.mq.messagehandler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageObj {
    private String groupId;
}
