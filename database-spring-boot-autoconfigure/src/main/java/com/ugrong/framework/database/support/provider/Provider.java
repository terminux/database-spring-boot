package com.ugrong.framework.database.support.provider;

import java.lang.reflect.Field;
import java.util.Map;

public interface Provider<T> {

    void init(Class<?> clazz, Field field);

    void put(Class<?> clazz, Map<String, T> mapping);

    Map<String, T> get(Class<?> clazz);

    T get(Class<?> clazz, String fieldName);
}
