package com.ugrong.framework.database.sql.listener;

import com.ugrong.framework.database.enums.EnumSqlType;

/**
 * The interface Sql execute listener.
 */
public interface SqlExecuteListener {

    /**
     * 分发sql至handle处理 异步执行.
     *
     * @param sql     the sql
     * @param sqlType the sql type
     */
    void dispatch(String sql, EnumSqlType sqlType);

}
