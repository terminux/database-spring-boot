package com.ugrong.framework.database.vo;

import java.util.Map;

/**
 * 更新sql信息 暂不支持复杂SQL
 */
public class UpdateSqlInfo extends SqlInfo {

    /**
     * where条件sql 不含where关键字
     */
    protected String whereSql;

    /**
     * sql参数 key为列名 value为参数值 不含where语句中的参数
     */
    protected Map<String, String> params;

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 判断当前语句是否为逻辑删除语句
     *
     * @return 是否为逻辑删除
     */
    public boolean isLogicDelete() {
        if (params == null || params.isEmpty()) {
            return false;
        }
        String deleted = params.get(SqlConstants.COLUMN_NAME_LOGIC_DELETE);
        return SqlConstants.LOGIC_DELETE_VALUE.equals(deleted);
    }

    @Override
    public String toString() {
        return "UpdateSqlInfo{" +
                "sql='" + sql + '\'' +
                ", statement=" + statement +
                ", sqlType=" + sqlType +
                ", tableName='" + tableName + '\'' +
                ", whereSql='" + whereSql + '\'' +
                ", params=" + params +
                ", isLogicDelete=" + isLogicDelete() +
                '}';
    }
}
