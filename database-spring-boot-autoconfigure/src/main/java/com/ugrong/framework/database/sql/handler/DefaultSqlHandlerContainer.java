package com.ugrong.framework.database.sql.handler;


import com.ugrong.framework.database.annotation.HandlerApi;
import com.ugrong.framework.database.enums.EnumSqlType;

import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toList;

public class DefaultSqlHandlerContainer implements SqlHandlerContainer {

    /**
     * 指定了类型的Handler+所有未指定类型的Handler
     */
    private final Map<EnumSqlType, List<SqlHandler>> definedTypeHandlers = new HashMap<>();

    /**
     * sqlType,map<handlerOrder, List<SqlHandler>>
     */
    private final Map<EnumSqlType, Map<Integer, List<SqlHandler>>> definedTypeHandlerMappings = new HashMap<>();

    /**
     * 所有未指定类型的Handler 将处理所有类型的SQL
     */
    private List<SqlHandler> undefinedTypeHandlers = new ArrayList<>();

    /**
     * map<handlerOrder, List<SqlHandler>>
     */
    private final Map<Integer, List<SqlHandler>> undefinedTypeHandlerMappings = new HashMap<>();

    public static final int DEFAULT_ORDER = Integer.MAX_VALUE;

    @Override
    public void put(SqlHandler handler) {
        if (handler == null) {
            return;
        }
        HandlerApi handlerApi = handler.getClass().getAnnotation(HandlerApi.class);
        EnumSqlType[] sqlTypes = null;
        int order = DEFAULT_ORDER;
        if (handlerApi != null) {
            sqlTypes = handlerApi.type();
            order = handlerApi.order();
        }
        if (sqlTypes == null || sqlTypes.length == 0) {
            this.putByDefault(order, handler);
            return;
        }
        for (EnumSqlType sqlType : sqlTypes) {
            this.putByType(order, sqlType, handler);
        }
    }

    private void putByDefault(int order, SqlHandler handler) {
        this.put(order, handler, undefinedTypeHandlerMappings);
        undefinedTypeHandlers = this.sortMapping(undefinedTypeHandlerMappings);
        this.syncToDefinedMappings(order, handler);
    }

    private void putByType(int order, EnumSqlType sqlType, SqlHandler handler) {
        Map<Integer, List<SqlHandler>> mappings = definedTypeHandlerMappings.get(sqlType);
        if (mappings == null) {
            mappings = new HashMap<>();
        }
        this.put(order, handler, mappings);
        definedTypeHandlerMappings.put(sqlType, mappings);
        definedTypeHandlers.put(sqlType, this.sortMapping(mappings));
    }

    private void syncToDefinedMappings(int order, SqlHandler handler) {
        Arrays.stream(EnumSqlType.values())
                .forEach(sqlType -> this.putByType(order, sqlType, handler));
    }

    private void put(int order, SqlHandler handler, Map<Integer, List<SqlHandler>> mappings) {
        List<SqlHandler> handlers = mappings.get(order);
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        handlers.add(handler);
        mappings.put(order, handlers);
    }

    private List<SqlHandler> sortMapping(Map<Integer, List<SqlHandler>> mappings) {
        return mappings.entrySet().stream()
                .sorted(comparingByKey())
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream).collect(toList());
    }

    @Override
    public List<SqlHandler> get(EnumSqlType sqlType) {
        if (sqlType != null) {
            return this.definedTypeHandlers.get(sqlType);
        }
        return this.undefinedTypeHandlers;
    }
}
