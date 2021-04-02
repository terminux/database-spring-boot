package com.ugrong.framework.database.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * 数据库配置
 */
@ConfigurationProperties(prefix = "app.config.database")
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
    private String txExpression = "execution (* com.ugrong.framework.api..*.service..*.*(..))";

    /**
     * 雪花算法，终端ID，默认：0.
     */
    private Long workerId = 0L;

    /**
     * 雪花算法，数据中心ID，默认：1.
     */
    private Long dataCenterId = 1L;

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

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    @Override
    public String toString() {
        return "DatabaseProperties{" +
                "excludeSqlTables=" + excludeSqlTables +
                ", entityPackages=" + Arrays.toString(entityPackages) +
                ", txExpression='" + txExpression + '\'' +
                ", workerId=" + workerId +
                ", dataCenterId=" + dataCenterId +
                '}';
    }
}
