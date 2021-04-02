package com.ugrong.framework.database.utils;


import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * The type Collection util.
 */
public class CollectionUtil {

    /**
     * 按字段分组
     *
     * @param <ID>     the type parameter
     * @param <T>      the type parameter
     * @param list     the list
     * @param function the function
     * @return map
     */
    public static <ID, T> Map<ID, List<T>> groupBy(Collection<T> list, Function<T, ID> function) {
        if (CollectionUtils.isEmpty(list) || function == null) {
            return new HashMap<>();
        }
        return list.stream().collect(groupingBy(function));
    }

    /**
     * 按字段分组 且只返回每组内的第一条数据
     *
     * @param <ID>     the type parameter
     * @param <T>      the type parameter
     * @param list     the list
     * @param function the function
     * @return map
     */
    public static <ID, T> Map<ID, T> singletonGroupBy(Collection<T> list, Function<T, ID> function) {
        return groupBy(list, function).entrySet()
                .parallelStream().filter(entry -> !CollectionUtils.isEmpty(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));
    }
}
