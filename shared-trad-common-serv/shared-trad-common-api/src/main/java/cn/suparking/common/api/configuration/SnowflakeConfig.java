package cn.suparking.common.api.configuration;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;

/**
 * SnowFlake config.
 */
public class SnowflakeConfig {

    private static long workerId;

    private static final long DATACENTER = 1L;

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(workerId, DATACENTER);

    static {
        workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
    }

    /**
     * get snowflake id.
     * @return long
     */
    public static synchronized long snowflakeId() {
        return SNOWFLAKE.nextId();
    }

    /**
     * get snow id.
     * @param workerId worker id
     * @param dataCenterId data center id.
     * @return snow id
     */
    public static synchronized long snowflakeId(final long workerId, final long dataCenterId) {
        Snowflake snowflake1 = IdUtil.getSnowflake(workerId, dataCenterId);
        return snowflake1.nextId();
    }
}
