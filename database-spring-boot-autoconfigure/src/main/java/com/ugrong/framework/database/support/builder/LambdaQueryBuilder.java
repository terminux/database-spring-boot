package com.ugrong.framework.database.support.builder;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.ugrong.framework.database.support.QueryExtension;
import com.ugrong.framework.database.utils.Assert;
import com.ugrong.framework.database.vo.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;

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
        Assert.notBlank(alias, "表别名不能为空");
        //启用别名
        return this.getAliasColumn(this.resolveColumn(column), onlyColumn);
    }

    @Override
    public LambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> column) {
        return this.orderByAsc(true, column);
    }

    @SafeVarargs
    @Override
    public final LambdaQueryWrapper<T> orderByAsc(SFunction<T, ?>... columns) {
        return this.orderByAsc(true, columns);
    }

    @SafeVarargs
    @Override
    public final LambdaQueryWrapper<T> orderByAsc(boolean condition, SFunction<T, ?>... columns) {
        return this.orderBy(condition, true, columns);
    }

    @Override
    public final LambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column) {
        return this.orderByDesc(true, column);
    }

    @SafeVarargs
    @Override
    public final LambdaQueryWrapper<T> orderByDesc(SFunction<T, ?>... columns) {
        return this.orderByDesc(true, columns);
    }

    @SafeVarargs
    @Override
    public final LambdaQueryWrapper<T> orderByDesc(boolean condition, SFunction<T, ?>... columns) {
        return this.orderBy(condition, false, columns);
    }

    @Override
    @SafeVarargs
    public final LambdaQueryWrapper<T> orderBy(boolean condition, boolean isAsc, SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            SqlKeyword mode = isAsc ? ASC : DESC;
            for (SFunction<T, ?> column : columns) {
                this.doIt(condition, ORDER_BY, () -> this.getSortColumn(column), mode);
            }
        }
        return typedThis;
    }

    protected Pair<Class<?>, String> resolveColumn(SFunction<T, ?> column) {
        SerializedLambda lambda = LambdaUtils.resolve(column);
        Class<?> lambdaClass = lambda.getInstantiatedType();
        this.tryInitCache(lambdaClass, alias);
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        return Pair.of(lambdaClass, fieldName);
    }

    protected String getSortColumn(SFunction<T, ?> column) {
        Pair<Class<?>, String> pair = this.resolveColumn(column);
        String aliasColumn = this.getAliasColumn(pair, true);
        return QueryExtension.supportChineseSort(pair.getLeft(), pair.getRight(), aliasColumn);
    }

    private String getAliasColumn(Pair<Class<?>, String> pair, boolean onlyColumn) {
        ColumnCache columnCache = this.getColumnCache(pair.getLeft(), pair.getRight());
        return onlyColumn ? columnCache.getColumn() : columnCache.getColumnSelect();
    }

    private void tryInitCache(Class<?> lambdaClass, String alias) {
        if (!this.initColumnMap) {
            Class<T> entityClass = this.getEntityClass();
            if (entityClass != null) {
                lambdaClass = entityClass;
            }
            this.columnMap = QueryExtension.getAliasColumnMap(lambdaClass, alias);
            this.initColumnMap = true;
        }
    }

    private ColumnCache getColumnCache(Class<?> lambdaClass, String fieldName) {
        ColumnCache column = this.columnMap.get(LambdaUtils.formatKey(fieldName));
        Assert.notNull(column, String.format("Can not find lambda cache for this property [%s] of entity [%s]", fieldName, lambdaClass.getName()));
        return column;
    }
}
