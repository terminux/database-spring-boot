package com.ugrong.framework.database.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import com.ugrong.framework.database.domain.ApplicationHolder;
import com.ugrong.framework.database.enums.EnumSqlType;
import com.ugrong.framework.database.sql.listener.SqlExecuteListener;
import com.ugrong.framework.database.utils.SqlUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义 p6spy sql输出格式
 */
public class P6spySqlFormatConfig implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        String message = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(sql)) {
            String formatSql = sql.replaceAll("[\\s]+", StringUtils.SPACE).concat(";");
            EnumSqlType sqlType = SqlUtil.getSqlType(formatSql);
            SqlExecuteListener listener = ApplicationHolder.getBean(SqlExecuteListener.class);
            if (listener != null) {
                listener.dispatch(formatSql, sqlType);
            }
            message = String.format("执行 [%s] 语句 | 耗时 [%s] ms | SQL [\n%s\n]", sqlType.desc(), elapsed, formatSql);
        }
        return message;
    }
}
