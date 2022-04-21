package cn.suparking.common.api.configuration;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;

/**
 * SnowFlake config.
 */
public class SnowflakeConfig {

    private static long workerId;

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
