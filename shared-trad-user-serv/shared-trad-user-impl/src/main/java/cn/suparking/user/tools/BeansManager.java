package cn.suparking.user.tools;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Adapted Factory.
 * @deprecated not use
 */
@Component
public class BeansManager implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    /**
     * get class bean.
     * @param name class name
     * @param clazz class
     * @param <T> class
     * @return class
     */
    public static <T> T getBean(final String name, final Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
}
