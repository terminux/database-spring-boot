package com.ugrong.framework.database.support.provider.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ugrong.framework.database.config.properties.DatabaseProperties;
import com.ugrong.framework.database.support.provider.Provider;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProvider<T> implements Provider<T> {

    private final Map<String, Map<String, T>> MAPPING_POOL = new ConcurrentHashMap<>();

    protected final DatabaseProperties databaseProperties;

    public AbstractProvider(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Override
    public void init(Class<?> clazz, Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        if (!this.isSupport(tableField)) {
            return;
        }
        Map<String, T> mapping = this.get(clazz);
        if (ObjectUtils.isEmpty(mapping)) {
            mapping = new HashMap<>();
        }
        mapping.put(field.getName().toUpperCase(), this.convert(tableField, field));
        this.put(clazz, mapping);
    }

    protected abstract boolean isSupport(TableField field);

    protected abstract T convert(TableField tableField, Field field);

    @Override
    public void put(Class<?> clazz, Map<String, T> mapping) {
        MAPPING_POOL.put(clazz.getName(), mapping);
    }

    @Override
    public Map<String, T> get(Class<?> clazz) {
        return MAPPING_POOL.get(clazz.getName());
    }

    @Override
    public T get(Class<?> clazz, String fieldName) {
        Map<String, T> mapping = this.get(clazz);
        if (ObjectUtils.isNotEmpty(mapping) && StringUtils.isNotBlank(fieldName)) {
            return mapping.get(fieldName.toUpperCase());
        }
        return null;
    }
}
