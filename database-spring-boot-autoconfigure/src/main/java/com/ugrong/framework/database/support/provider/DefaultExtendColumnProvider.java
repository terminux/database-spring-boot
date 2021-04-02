package com.ugrong.framework.database.support.provider;

import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.ugrong.framework.database.config.properties.DatabaseProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultExtendColumnProvider extends AbstractExtendColumnProvider {

    private final static Map<String, Map<String, ColumnCache>> MAPPING_POOL = new ConcurrentHashMap<>();

    public DefaultExtendColumnProvider(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }

    @Override
    public void put(Class<?> clazz, Map<String, ColumnCache> mapping) {
        MAPPING_POOL.put(clazz.getName(), mapping);
    }

    @Override
    public Map<String, ColumnCache> get(Class<?> clazz) {
        return MAPPING_POOL.get(clazz.getName());
    }
}
