package com.ugrong.framework.database.support.provider;

import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;

import java.util.Collection;
import java.util.Map;

public interface ExtendColumnProvider {

    void put(Class<?> clazz, Map<String, ColumnCache> mapping);

    Map<String, ColumnCache> get(Class<?> clazz);

    ColumnCache get(Class<?> clazz, String fieldName);

    Collection<ColumnCache> getAll(Class<?> clazz);
}
