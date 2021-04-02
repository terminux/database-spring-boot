package com.ugrong.framework.database.enums;

/**
 * 查询匹配符
 */
public enum EnumQueryOperator {

    /**
     * 等于
     */
    EQ("等于"),
    /**
     * !=
     */
    NEQ("!="),
    /**
     * 大于
     */
    GT("大于"),
    /**
     * 小于
     */
    LT("小于"),
    /**
     * 大于等于
     */
    GTE("大于等于"),
    /**
     * 小于等于
     */
    LTE("小于等于"),
    /**
     * like %x%
     */
    LIKE("like %x%"),
    /**
     * like %x
     */
    LLIKE("like %x"),
    /**
     * like x%
     */
    RLIKE("like x%"),
    /**
     * in
     */
    IN("in"),

    /**
     * not in
     */
    NIN("not in"),
    /**
     * not null
     */
    NOTNULL("not null"),
    /**
     * is null
     */
    ISNULL("is null");

    private final String desc;

    EnumQueryOperator(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return this.desc;
    }
}
