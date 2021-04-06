package com.ugrong.framework.database.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用于支持对中文字段的排序.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ChineseSupport {

    /**
     * @return 是否启用按照汉字拼音进行order by排序
     */
    boolean enable() default true;
}
