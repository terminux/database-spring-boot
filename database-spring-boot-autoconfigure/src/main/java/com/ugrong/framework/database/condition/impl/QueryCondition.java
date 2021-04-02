package com.ugrong.framework.database.condition.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ugrong.framework.database.condition.Condition;
import com.ugrong.framework.database.enums.EnumQueryOperator;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;

public class QueryCondition extends Condition {

    public QueryCondition(EnumQueryOperator operator, String field, Object value) {
        this.operator = operator;
        this.setField(field);
        this.value = value;
    }

    public QueryCondition() {
    }

    @ApiModelProperty(value = "要查询的值", position = 101)
    private Object value;

    @ApiModelProperty(value = "查询匹配符", position = 102)
    private EnumQueryOperator operator;

    @Override
    public void wrapper(QueryWrapper<?> wrapper) {
        switch (this.getOperator()) {
            case EQ:
                wrapper.eq(this.getColumnName(), this.getValue());
                break;
            case NEQ:
                wrapper.ne(this.getColumnName(), this.getValue());
                break;
            case IN:
                wrapper.in(this.getColumnName(), (Collection<?>) this.getValue());
                break;
            case NIN:
                wrapper.notIn(this.getColumnName(), (Collection<?>) this.getValue());
                break;
            case GT:
                wrapper.gt(this.getColumnName(), this.getValue());
                break;
            case LT:
                wrapper.lt(this.getColumnName(), this.getValue());
                break;
            case GTE:
                wrapper.ge(this.getColumnName(), this.getValue());
                break;
            case LTE:
                wrapper.le(this.getColumnName(), this.getValue());
                break;
            case LIKE:
                wrapper.like(this.getColumnName(), this.getValue());
                break;
            case LLIKE:
                wrapper.likeLeft(this.getColumnName(), this.getValue());
                break;
            case RLIKE:
                wrapper.likeRight(this.getColumnName(), this.getValue());
                break;
            case ISNULL:
                wrapper.isNull(this.getColumnName());
                break;
            case NOTNULL:
                wrapper.isNotNull(this.getColumnName());
                break;
            default:
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public EnumQueryOperator getOperator() {
        return operator;
    }

    public void setOperator(EnumQueryOperator operator) {
        this.operator = operator;
    }
}