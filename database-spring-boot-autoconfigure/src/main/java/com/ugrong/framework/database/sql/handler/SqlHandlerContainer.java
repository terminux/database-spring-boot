package com.ugrong.framework.database.sql.handler;


import com.ugrong.framework.database.enums.EnumSqlType;

import java.io.Serializable;
import java.util.List;

public interface SqlHandlerContainer extends Serializable {

    void put(SqlHandler handler);

    List<SqlHandler> get(EnumSqlType sqlType);
}
