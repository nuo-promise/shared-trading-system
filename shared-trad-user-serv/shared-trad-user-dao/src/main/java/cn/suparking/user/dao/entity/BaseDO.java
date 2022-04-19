package cn.suparking.user.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * BaseDo.
 */
@Data
public class BaseDO implements Serializable {

    private String id;

    private Timestamp dateCreated;

    private Timestamp dateUpdated;
}
