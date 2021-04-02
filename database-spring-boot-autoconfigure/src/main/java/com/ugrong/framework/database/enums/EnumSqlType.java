package com.ugrong.framework.database.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL类型
 */
public enum EnumSqlType {

    UNKNOWN("未知"), INSERT("新增"), UPDATE("修改"), DELETE("删除"), SELECT("查询");

    private final String desc;

    EnumSqlType(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return this.desc;
    }

    public static EnumSqlType of(String type) {
        if (StringUtils.isNotBlank(type)) {
            try {
                return EnumSqlType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                //ignore exception
            }
        }
        return null;
    }
}
