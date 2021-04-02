package com.ugrong.framework.database.utils;

import com.ugrong.framework.database.enums.EnumSqlType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class SqlUtil {

    private static final String SQL_ANNOTATION_SUFFIX = "*/";

    private SqlUtil() {

    }

    public static EnumSqlType getSqlType(String sql) {
        EnumSqlType sqlType = null;
        sql = removeAnnotation(sql);
        if (StringUtils.isNotBlank(sql)) {
            String[] strings = sql.split(StringUtils.SPACE, 2);
            if (ArrayUtils.isNotEmpty(strings)) {
                String type = strings[0];
                if (StringUtils.isNotBlank(type)) {
                    sqlType = EnumSqlType.of(type);
                }
            }
        }
        return sqlType == null ? EnumSqlType.UNKNOWN : sqlType;
    }

    private static String removeAnnotation(String sql) {
        if (StringUtils.contains(sql, SQL_ANNOTATION_SUFFIX)) {
            return StringUtils.trim(StringUtils.substringAfter(sql, SQL_ANNOTATION_SUFFIX));
        }
        return sql;
    }
}
