package com.ugrong.framework.database.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库配置
 */
@ConfigurationProperties(prefix = "app.config.database")
@Validated
public class DatabaseProperties {

    /**
     * 不需要监听sql的表集合，支持正则表达式.
     */
    private List<String> excludeSqlTables;

    /**
     * 要扫描的entity包，默认扫描所有包，用来做别名映射，支持联表动态查询.
     */
    private String[] entityPackages;

    /**
     * 事务切面拦截规则
     */
    @NotEmpty(message = "事务切面拦截规则不能为空")
    private String txExpression;

    public List<String> getExcludeSqlTables() {
        return excludeSqlTables;
    }

    public void setExcludeSqlTables(List<String> excludeSqlTables) {
        this.excludeSqlTables = excludeSqlTables;
    }

    public String[] getEntityPackages() {
        return entityPackages;
    }

    public void setEntityPackages(String[] entityPackages) {
        this.entityPackages = entityPackages;
    }

    public String getTxExpression() {
        return txExpression;
    }

    public void setTxExpression(String txExpression) {
        this.txExpression = txExpression;
    }

    @Override
    public String toString() {
        return "DatabaseProperties{" +
                "excludeSqlTables=" + excludeSqlTables +
                ", entityPackages=" + Arrays.toString(entityPackages) +
                ", txExpression='" + txExpression + '\'' +
                '}';
    }
}
