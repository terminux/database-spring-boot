package com.ugrong.framework.database.support.builder;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static com.ugrong.framework.database.support.builder.LambdaQueryBuilder.FIELD_NAME_SQL_SELECT;


public class QueryBuilder<T> extends QueryWrapper<T> {

    private final String alias;

    public QueryBuilder(T entity, String alias) {
        super(entity);
        this.alias = alias;
    }

    @Override
    public LambdaQueryWrapper<T> lambda() {
        Field sqlSelectField = ReflectionUtils.findField(this.getClass(), FIELD_NAME_SQL_SELECT, SharedString.class);
        SharedString sqlSelect;
        if (sqlSelectField != null) {
            ReflectionUtils.makeAccessible(sqlSelectField);
            sqlSelect = (SharedString) ReflectionUtils.getField(sqlSelectField, this);
        } else {
            sqlSelect = new SharedString();
        }
        return new LambdaQueryBuilder<>(this.getEntity(), this.getEntityClass(), sqlSelect, this.paramNameSeq, this.paramNameValuePairs, this.expression, this.lastSql, this.sqlComment, this.sqlFirst, alias);
    }
}
