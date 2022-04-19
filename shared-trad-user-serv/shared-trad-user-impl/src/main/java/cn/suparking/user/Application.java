package cn.suparking.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"cn.suparking"})
@ComponentScan(basePackages = {"cn.suparking"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.suparking"})
public class Application {

    /**
     * user app starter.
     * @param args {@link String}
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
