package cn.suparking.data;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"cn.suparking"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.suparking"})
public class Application implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * order app starter.
     * @param args {@link String}
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    /**
     * getBean.
     * @param name service name
     * @param clazz service class
     * @param <T> template
     * @return service Bean.
     */
    public static <T> T getBean(final String name, final Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * contains bean.
     * @param name service name
     * @return boolean
     */
    public static boolean containsBean(final String name) {
        return applicationContext.containsBean(name);
    }
}
