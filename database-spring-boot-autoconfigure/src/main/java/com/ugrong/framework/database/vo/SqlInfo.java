package com.ugrong.framework.database.vo;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.ugrong.framework.database.enums.EnumSqlType;

import java.io.Serializable;

/**
 * sql信息 暂不支持复杂SQL
 */
public class SqlInfo implements Serializable {

    /**
     * 原始sql
     */
    protected String sql;

    /**
     * 原始解析数据
     */
    protected SQLStatement statement;

    /**
     * sql类型
     */
    protected EnumSqlType sqlType;

    /**
     * sql操作的表名
     */
    protected String tableName;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SQLStatement getStatement() {
        return statement;
    }

    public void setStatement(SQLStatement statement) {
        this.statement = statement;
    }

    public EnumSqlType getSqlType() {
        return sqlType;
    }

    public void setSqlType(EnumSqlType sqlType) {
        this.sqlType = sqlType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "SqlInfo{" +
                "sql='" + sql + '\'' +
                ", statement=" + statement +
                ", sqlType=" + sqlType +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
