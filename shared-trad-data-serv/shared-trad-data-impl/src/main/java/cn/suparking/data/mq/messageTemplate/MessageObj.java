package cn.suparking.data.mq.messageTemplate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageObj {
    private String groupId;
}
