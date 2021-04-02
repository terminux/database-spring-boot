package com.ugrong.framework.database.utils;

import com.ugrong.framework.database.domain.ApplicationHolder;
import com.ugrong.framework.database.domain.PredefinedValHolder;

import java.io.Serializable;
import java.util.function.Function;

public class DataFillUtil {

    private DataFillUtil() {

    }

    public static <PK extends Serializable> PK getCurrentUserId() {
        return getPredefinedVal(PredefinedValHolder::getCurrentUserId);
    }

    public static <PK extends Serializable> PK getTenantId() {
        return getPredefinedVal(PredefinedValHolder::getTenantId);
    }

    public static <PK extends Serializable> PK getPredefinedVal(Function<PredefinedValHolder, PK> mapper) {
        PredefinedValHolder holder = ApplicationHolder.getBean(PredefinedValHolder.class);
        Assert.notNull(holder, "this predefined value holder is required; it must not be null");
        try {
            return mapper.apply(holder);
        } catch (Exception e) {
            // ignore exception
        }
        return null;
    }
}
