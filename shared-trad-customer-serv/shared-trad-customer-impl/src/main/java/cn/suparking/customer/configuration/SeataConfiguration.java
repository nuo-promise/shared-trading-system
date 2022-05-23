package cn.suparking.customer.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class SeataConfiguration {

    /**
     * set druidDataSource.
     * @return {@link DruidDataSource}
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * datasourceProxy.
     * @param druidDataSource {@link DruidDataSource}
     * @return {@link DataSource}
     */
    @Bean("dataSource")
    @Primary
    public DataSource dataSourceDelegation(final DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }
}
