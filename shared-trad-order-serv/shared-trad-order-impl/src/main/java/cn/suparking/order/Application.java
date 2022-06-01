package cn.suparking.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"cn.suparking"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.suparking"})
public class Application {

    /**
     * order app starter.
     * @param args {@link String}
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
