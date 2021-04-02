package com.ugrong.framework.database.sql.listener;

import com.ugrong.framework.database.sql.handler.SqlHandler;
import com.ugrong.framework.database.sql.handler.SqlHandlerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * sql handler bean 生命周期监听器
 */
public class SqlHandlerLifecycleListener implements BeanPostProcessor {

    private final SqlHandlerContainer container;

    public SqlHandlerLifecycleListener(SqlHandlerContainer container) {
        this.container = container;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (SqlHandler.class.isAssignableFrom(bean.getClass())) {
            container.put((SqlHandler) bean);
        }
        return bean;
    }
}
