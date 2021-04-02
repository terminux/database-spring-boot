package com.ugrong.framework.database.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * 获取spring上下文
 */
@Component
public class ApplicationHolder implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ApplicationHolder.class);

    private static ApplicationContext context = null;

    /**
     * 获取应用程序的名称
     *
     * @return application name
     */
    public static String getApplicationName() {
        if (context != null) {
            return context.getEnvironment().getProperty("spring.application.name");
        }
        return null;
    }

    /**
     * 获取激活的profile
     *
     * @return profiles string [ ]
     */
    public static String[] getActiveProfile() {
        if (context != null) {
            return context.getEnvironment().getActiveProfiles();
        }
        return null;
    }

    /**
     * Gets bean.
     *
     * @param beanName the bean name
     * @return the bean
     */
    public static Object getBean(String beanName) {
        if (context != null) {
            try {
                return context.getBean(beanName);
            } catch (Exception e) {
                log.error("Failed to get bean.beanName=[{}],message=[{}]", beanName, e.getMessage());
            }
        }
        return null;
    }

    /**
     * Gets bean.
     *
     * @param <T>       the type parameter
     * @param beanName  the bean name
     * @param beanClass the bean class
     * @return the bean
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        if (context != null) {
            try {
                return context.getBean(beanName, beanClass);
            } catch (Exception e) {
                log.error("Failed to get bean.beanName=[{}],message=[{}]", beanName, e.getMessage());
            }
        }
        return null;
    }

    /**
     * Gets beans with annotation.
     *
     * @param annotation the annotation
     * @return the beans with annotation
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        if (context != null) {
            try {
                return context.getBeansWithAnnotation(annotation);
            } catch (Exception e) {
                log.error("Failed to get bean.annotation=[{}],message=[{}]", annotation, e.getMessage());
            }
        }
        return null;
    }

    /**
     * Gets bean.
     *
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @return the bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        if (context != null) {
            try {
                return context.getBean(beanClass);
            } catch (Exception e) {
                log.error("Failed to get bean.beanClass=[{}],message=[{}]", beanClass, e.getMessage());
            }
        }
        return null;
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * Gets bean of type.
     *
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @return 返回bean的多个实现 bean of type
     */
    public static <T> Collection<T> getBeanOfType(Class<T> beanClass) {
        if (context != null) {
            try {
                return context.getBeansOfType(beanClass).values();
            } catch (Exception e) {
                log.error("Failed to get beans of type.beanClass=[{}],message=[{}]", beanClass, e.getMessage());
            }
        }
        return null;
    }

    /**
     * 发布事件
     *
     * @param event the event
     */
    public static void publishEvent(Object event) {
        if (context != null) {
            context.publishEvent(event);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }
}
