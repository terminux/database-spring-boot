package com.ugrong.framework.database.sql.handler;


import com.ugrong.framework.database.vo.SqlInfo;

import java.io.Serializable;

public interface SqlHandler extends Serializable {

    void handle(SqlInfo info);
}
