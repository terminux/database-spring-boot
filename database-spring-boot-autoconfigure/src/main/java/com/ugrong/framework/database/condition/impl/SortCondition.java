package com.ugrong.framework.database.condition.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ugrong.framework.database.condition.Condition;
import com.ugrong.framework.database.enums.EnumSortType;
import io.swagger.annotations.ApiModelProperty;

public class SortCondition extends Condition {

    public SortCondition() {
    }

    public SortCondition(String field, EnumSortType type) {
        this.setField(field);
        this.type = type;
    }

    @ApiModelProperty(value = "排序类型", position = 102)
    private EnumSortType type;

    @Override
    public void wrapper(QueryWrapper<?> wrapper) {
        if (this.getType() == EnumSortType.ASC) {
            wrapper.orderByAsc(this.getColumnName());
            return;
        }
        // EnumSortType.DESC
        wrapper.orderByDesc(this.getColumnName());
    }

    public EnumSortType getType() {
        return type;
    }

    public void setType(EnumSortType type) {
        this.type = type;
    }
}