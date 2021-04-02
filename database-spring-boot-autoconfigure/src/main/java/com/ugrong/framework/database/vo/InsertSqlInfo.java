package com.ugrong.framework.database.vo;

import java.util.List;
import java.util.Map;

/**
 * 新增sql信息 暂不支持复杂SQL
 */
public class InsertSqlInfo extends SqlInfo {

    /**
     * 插入语句的参数 支持批量插入
     */
    protected List<Map<String, String>> values;

    public List<Map<String, String>> getValues() {
        return values;
    }

    public void setValues(List<Map<String, String>> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "InsertSqlInfo{" +
                "values=" + values +
                ", sql='" + sql + '\'' +
                ", statement=" + statement +
                ", sqlType=" + sqlType +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
