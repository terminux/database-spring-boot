package com.ugrong.framework.database.condition;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public abstract class Condition implements Serializable {

    @ApiModelProperty(value = "要操作的字段名，可选值请查看 availableFields 字段", position = 100)
    private String field;

    /**
     * 数据库对应的字段名
     */
    @JsonIgnore
    private String columnName;

    public abstract void wrapper(QueryWrapper<?> wrapper);

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
