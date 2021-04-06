package com.ugrong.framework.database.support;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;

import com.ugrong.framework.database.domain.ApplicationHolder;
import com.ugrong.framework.database.support.provider.EntityFieldProvider;
import com.ugrong.framework.database.support.provider.ExtendColumnProvider;
import com.ugrong.framework.database.utils.Assert;
import com.ugrong.framework.database.vo.FieldInfo;
import com.ugrong.framework.database.vo.SqlConstants;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * 查询扩展类.
 */
public class QueryExtension {

    private QueryExtension() {

    }

    /**
     * 支持字段按照汉字拼音进行order by排序.
     *
     * @param entityClass the entity class
     * @param fieldName   the field name
     * @param columnName  the column name
     * @return the string
     */
    public static String supportChineseSort(Class<?> entityClass, String fieldName, String columnName) {
        EntityFieldProvider provider = ApplicationHolder.getBean(EntityFieldProvider.class);
        if (provider != null) {
            FieldInfo fieldInfo = provider.get(entityClass, fieldName);
            if (fieldInfo != null && fieldInfo.isChineseSort()) {
                return String.format(SqlConstants.SQL_CHINESE_SORT, columnName);
            }
        }
        return columnName;
    }

    /**
     * 获取别名列以及扩展列信息.
     *
     * @param entityClass the entity class
     * @param alias       the alias
     * @return the alias column map
     */
    public static Map<String, ColumnCache> getAliasColumnMap(Class<?> entityClass, String alias) {
        Map<String, ColumnCache> columnMap = new HashMap<>();

        Map<String, ColumnCache> cacheColumnMap = LambdaUtils.getColumnMap(entityClass);
        if (cacheColumnMap == null) {
            columnMap = new HashMap<>();
        } else if (!cacheColumnMap.isEmpty()) {
            String prefix = alias.concat(".");
            columnMap = cacheColumnMap.entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> {
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
