package cn.suparking.data.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishData implements Serializable {

    private static final long serialVersionUID = 3308598151404380549L;

    private String from;

    private String type;

    private String data;
}
