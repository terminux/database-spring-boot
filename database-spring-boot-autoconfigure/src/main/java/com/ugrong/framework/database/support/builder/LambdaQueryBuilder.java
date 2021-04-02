package com.ugrong.framework.database.support.builder;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.ugrong.framework.database.domain.ApplicationHolder;
import com.ugrong.framework.database.support.provider.ExtendColumnProvider;
import com.ugrong.framework.database.utils.Assert;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LambdaQueryBuilder<T> extends LambdaQueryWrapper<T> {

    private final String alias;

    private Map<String, ColumnCache> columnMap = null;

    private boolean initColumnMap = false;

    public static final String FIELD_NAME_SQL_SELECT = "sqlSelect";

    public LambdaQueryBuilder(Class<T> entityClass, String alias) {
        super(entityClass);
        this.alias = alias;
    }

    public LambdaQueryBuilder(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql, SharedString sqlComment, SharedString sqlFirst, String alias) {
        Field sqlSelectField = ReflectionUtils.findField(this.getClass(), FIELD_NAME_SQL_SELECT, SharedString.class);
        if (sqlSelectField != null) {
            ReflectionUtils.makeAccessible(sqlSelectField);
            ReflectionUtils.setField(sqlSelectField, this, new SharedString());
        }
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.alias = alias;
        if (sqlSelectField != null) {
            ReflectionUtils.setField(sqlSelectField, this, sqlSelect);
        }
    }

    @Override
    protected String columnToString(SFunction<T, ?> column, boolean onlyColumn) {
        if (StringUtils.isBlank(alias)) {
            return super.columnToString(column, onlyColumn);
        }
        //启用别名
        return this.getAliasColumn(LambdaUtils.resolve(column), onlyColumn, alias);
    }

    private String getAliasColumn(SerializedLambda lambda, boolean onlyColumn, String alias) {
        Class<?> lambdaClass = lambda.getInstantiatedType();
        this.tryInitCache(lambdaClass, alias);
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        ColumnCache columnCache = this.getColumnCache(fieldName, lambdaClass);
        return onlyColumn ? columnCache.getColumn() : columnCache.getColumnSelect();
    }

    private void tryInitCache(Class<?> lambdaClass, String alias) {
        if (!this.initColumnMap) {
            Class<T> entityClass = this.getEntityClass();
            if (entityClass != null) {
                lambdaClass = entityClass;
            }
            this.columnMap = getAliasColumnMap(lambdaClass, alias);
            this.initColumnMap = true;
        }
    }

    private ColumnCache getColumnCache(String fieldName, Class<?> lambdaClass) {
        ColumnCache column = this.columnMap.get(LambdaUtils.formatKey(fieldName));
        Assert.notNull(column, String.format("Can not find lambda cache for this property [%s] of entity [%s]", fieldName, lambdaClass.getName()));
        return column;
    }

    public static Map<String, ColumnCache> getAliasColumnMap(Class<?> entityClass, String alias) {
        Map<String, ColumnCache> columnMap = new HashMap<>();

        Map<String, ColumnCache> cacheColumnMap = LambdaUtils.getColumnMap(entityClass);
        if (cacheColumnMap == null) {
            columnMap = new HashMap<>();
        } else if (!cacheColumnMap.isEmpty()) {
            String prefix = alias.concat(".");
            columnMap = cacheColumnMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                ColumnCache cache = entry.getValue();
                String aliasColumn = prefix.concat(cache.getColumn());
                return new ColumnCache(aliasColumn, cache.getColumnSelect());
            }));
        }

        ExtendColumnProvider provider = ApplicationHolder.getBean(ExtendColumnProvider.class);
        if (provider != null) {
            Map<String, ColumnCache> extendColumnMap = provider.get(entityClass);
            if (ObjectUtils.isNotEmpty(extendColumnMap)) {
                columnMap.putAll(extendColumnMap);
            }
        }
        Assert.isNotTrue(columnMap.isEmpty(), String.format("Can not find lambda cache for this entity [%s]", entityClass.getName()));
        return columnMap;
    }
}
