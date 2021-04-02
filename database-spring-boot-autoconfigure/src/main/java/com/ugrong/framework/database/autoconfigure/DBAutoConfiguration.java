package com.ugrong.framework.database.autoconfigure;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.ugrong.framework.database.config.properties.DatabaseProperties;
import com.ugrong.framework.database.handler.ModelMetaDataHandler;
import com.ugrong.framework.database.sql.handler.DefaultSqlHandlerContainer;
import com.ugrong.framework.database.sql.handler.SqlHandlerContainer;
import com.ugrong.framework.database.sql.listener.SqlExecuteListener;
import com.ugrong.framework.database.sql.listener.SqlExecuteListenerImpl;
import com.ugrong.framework.database.sql.listener.SqlHandlerLifecycleListener;
import com.ugrong.framework.database.support.provider.DefaultExtendColumnProvider;
import com.ugrong.framework.database.support.provider.ExtendColumnProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
public class DBAutoConfiguration {

    @Bean
    public ModelMetaDataHandler modelMetaDataHandler() {
        return new ModelMetaDataHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public IdentifierGenerator idGenerator(DatabaseProperties databaseProperties) {
        return new DefaultIdentifierGenerator(databaseProperties.getWorkerId(), databaseProperties.getDataCenterId());
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlHandlerContainer sqlHandlerContainer() {
        return new DefaultSqlHandlerContainer();
    }

    @Bean
    public SqlHandlerLifecycleListener sqlHandlerLifecycleListener(SqlHandlerContainer container) {
        return new SqlHandlerLifecycleListener(container);
    }

    @Bean
    public SqlExecuteListener sqlExecuteListener(SqlHandlerContainer container) {
        return new SqlExecuteListenerImpl(container);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExtendColumnProvider extendColumnProvider(DatabaseProperties databaseProperties) {
        return new DefaultExtendColumnProvider(databaseProperties);
    }
}