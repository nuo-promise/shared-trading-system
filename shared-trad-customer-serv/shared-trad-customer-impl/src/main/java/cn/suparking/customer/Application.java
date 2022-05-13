package cn.suparking.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.suparking"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.suparking"})
public class Application {

    /**
     * invoice app starter.
     *
     * @param args {@link String}
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
