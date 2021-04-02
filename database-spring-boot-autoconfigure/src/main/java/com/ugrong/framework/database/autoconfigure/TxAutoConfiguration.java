package com.ugrong.framework.database.autoconfigure;

import com.ugrong.framework.database.config.properties.DatabaseProperties;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TxAutoConfiguration {

    /* 事务拦截器 */
    @Bean("txAdvice")
    public TransactionInterceptor txAdvice(TransactionManager manager) {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        //只读事务
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

        //当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute(
                TransactionDefinition.PROPAGATION_REQUIRED,
                Arrays.asList(new RollbackRuleAttribute(Throwable.class), new RollbackRuleAttribute(RuntimeException.class)));

        Map<String, TransactionAttribute> txMap = new HashMap<>();
        txMap.put("query*", readOnlyTx);
        txMap.put("get*", readOnlyTx);
        txMap.put("select*", readOnlyTx);
        txMap.put("list*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("fetch*", readOnlyTx);
        txMap.put("page*", readOnlyTx);
        txMap.put("*", requiredTx);
        source.setNameMap(txMap);
        return new TransactionInterceptor(manager, source);
    }

    /**
     * 切面拦截规则 参数会自动从容器中注入
     */
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(TransactionInterceptor txAdvice, DatabaseProperties databaseProperties) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(txAdvice);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(databaseProperties.getTxExpression());
        advisor.setPointcut(pointcut);
        return advisor;
    }

}
