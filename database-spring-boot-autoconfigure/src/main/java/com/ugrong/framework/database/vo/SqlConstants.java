package com.ugrong.framework.database.vo;

public abstract class SqlConstants {

    /**
     * WHERE关键字
     */
    public final static String SQL_KEYWORD_WHERE = "WHERE";

    /**
     * LIMIT关键字
     */
    public final static String SQL_KEYWORD_LIMIT = "LIMIT";

    /**
     * 主键列名
     */
    public static final String COLUMN_NAME_ID = "id";

    /**
     * 逻辑删除列名
     */
    public static final String COLUMN_NAME_LOGIC_DELETE = "deleted";

    /**
     * 逻辑删除的值
     */
    public static final String LOGIC_DELETE_VALUE = "1";

    /**
     * 未逻辑删除的值
     */
    public static final String LOGIC_NOT_DELETE_VALUE = "0";

    /**
     * 公司列名
     */
    public static final String COLUMN_NAME_COMPANY_ID = "company_id";

    /**
     * 记录创建人列名
     */
    public static final String COLUMN_NAME_CREATE_BY = "create_by";

    /**
     * 记录修改人列名
     */
    public static final String COLUMN_NAME_MODIFY_BY = "modify_by";

    /**
     * 默认的表别名
     */
    public static final String DEFAULT_TABLE_ALIAS_NAME = "t";

}
