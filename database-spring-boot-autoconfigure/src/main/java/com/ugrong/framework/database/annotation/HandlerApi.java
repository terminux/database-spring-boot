package com.ugrong.framework.database.annotation;

import com.ugrong.framework.database.enums.EnumSqlType;
import com.ugrong.framework.database.sql.handler.DefaultSqlHandlerContainer;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The interface Handler api.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Inherited
public @interface HandlerApi {

    /**
     * Sql类型.
     * 如果指定了该值那么只有对应类型的SQL才能进入当前handler，多个类型的关系是或
     *
     * @return enum sql type [ ]
     */
    EnumSqlType[] type() default {};

    /**
     * 多个handler的处理顺序，数值越小优先处理.
     *
     * @return the order
     */
    int order() default DefaultSqlHandlerContainer.DEFAULT_ORDER;

}
