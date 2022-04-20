package cn.suparking.common.api.configuration;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * SnowFlake config.
 */
public class SnowflakeConfig {

   /**
    * 终端ID
    */
   private static long workerId;

   /**
    * 数据中心ID
    */
   private static final long dataCenterId = 1L;

   private static final Snowflake snowflake = IdUtil.getSnowflake(workerId, dataCenterId);

   static {
      workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
   }

   public static synchronized long snowflakeId() {
      return snowflake.nextId();
   }

   public static synchronized long snowflakeId(final long workerId, final long dataCenterId) {
      Snowflake snowflake1 = IdUtil.getSnowflake(workerId, dataCenterId);
      return snowflake1.nextId();
   }
}
