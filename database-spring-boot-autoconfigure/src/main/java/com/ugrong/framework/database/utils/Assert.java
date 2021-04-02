package com.ugrong.framework.database.utils;

import com.ugrong.framework.database.exceptions.ApiBizException;
import com.ugrong.framework.database.exceptions.ApiException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;


/**
 * The type Assert.
 */
public class Assert {

    private Assert() {

    }

    /**
     * Not blank.
     *
     * @param in the in
     */
    public static void notBlank(String in) {
        if (StringUtils.isBlank(in)) {
            throw new ApiBizException();
        }
    }

    /**
     * Not blank.
     *
     * @param in       the in
     * @param errorMsg the error msg
     */
    public static void notBlank(String in, String errorMsg) {
        if (StringUtils.isBlank(in)) {
            throw new ApiBizException(errorMsg);
        }
    }

    /**
     * Not blank.
     *
     * @param in        the in
     * @param errorCode the error code
     */
    public static void notBlank(String in, int errorCode) {
        if (StringUtils.isBlank(in)) {
            throw new ApiException(errorCode);
        }
    }

    /**
     * Not blank.
     *
     * @param in        the in
     * @param errorMsg  the error msg
     * @param errorCode the error code
     */
    public static void notBlank(String in, String errorMsg, int errorCode) {
        if (StringUtils.isBlank(in)) {
            throw new ApiException(errorCode, errorMsg);
        }
    }

    /**
     * Not blank.
     *
     * @param in       the in
     * @param errorMsg the error msg
     * @param result   the result
     */
    public static void notBlank(String in, String errorMsg, Object result) {
        if (StringUtils.isBlank(in)) {
            throw new ApiBizException(errorMsg, result);
        }
    }

    /**
     * Not null.
     *
     * @param in the in
     */
    public static void notNull(Object in) {
        Optional.ofNullable(in).orElseThrow(ApiBizException::new);
    }

    /**
     * Not null.
     *
     * @param in        the in
     * @param errorCode the error code
     */
    public static void notNull(Object in, int errorCode) {
        Optional.ofNullable(in).orElseThrow(() -> new ApiException(errorCode));
    }

    /**
     * Not null.
     *
     * @param in       the in
     * @param errorMsg the error msg
     */
    public static void notNull(Object in, String errorMsg) {
        Optional.ofNullable(in).orElseThrow(() -> new ApiBizException(errorMsg));
    }

    /**
     * Not null.
     *
     * @param in       the in
     * @param errorMsg the error msg
     * @param result   the result
     */
    public static void notNull(Object in, String errorMsg, Object result) {
        Optional.ofNullable(in).orElseThrow(() -> new ApiBizException(errorMsg, result));
    }

    /**
     * Not null.
     *
     * @param in        the in
     * @param errorMsg  the error msg
     * @param errorCode the error code
     */
    public static void notNull(Object in, String errorMsg, int errorCode) {
        Optional.ofNullable(in).orElseThrow(() -> new ApiException(errorCode, errorMsg));
    }

    /**
     * Is true.
     *
     * @param condition the condition
     */
    public static void isTrue(boolean condition) {
        if (!condition) {
            throw new ApiBizException();
        }
    }

    /**
     * Is true.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void isTrue(boolean condition, int errorCode) {
        if (!condition) {
            throw new ApiException(errorCode);
        }
    }

    /**
     * Is true.
     *
     * @param condition the condition
     * @param errorMsg  the error msg
     */
    public static void isTrue(boolean condition, String errorMsg) {
        if (!condition) {
            throw new ApiBizException(errorMsg);
        }
    }

    /**
     * Is true.
     *
     * @param condition the condition
     * @param errorMsg  the error msg
     * @param errorCode the error code
     */
    public static void isTrue(boolean condition, String errorMsg, int errorCode) {
        if (!condition) {
            throw new ApiException(errorCode, errorMsg);
        }
    }

    /**
     * Is not true.
     *
     * @param condition the condition
     */
    public static void isNotTrue(Boolean condition) {
        if (condition != null && condition) {
            throw new ApiBizException();
        }
    }

    /**
     * Is not true.
     *
     * @param condition the condition
     * @param errorMsg  the error msg
     */
    public static void isNotTrue(Boolean condition, String errorMsg) {
        if (condition != null && condition) {
            throw new ApiBizException(errorMsg);
        }
    }

    /**
     * Is not true.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void isNotTrue(Boolean condition, int errorCode) {
        if (condition != null && condition) {
            throw new ApiException(errorCode);
        }
    }

    /**
     * Is not true.
     *
     * @param condition the condition
     * @param errorMsg  the error msg
     * @param result    the result
     */
    public static void isNotTrue(Boolean condition, String errorMsg, Object result) {
        if (condition != null && condition) {
            throw new ApiBizException(errorMsg, result);
        }
    }

    /**
     * Is not true.
     *
     * @param condition the condition
     * @param errorMsg  the error msg
     * @param errorCode the error code
     */
    public static void isNotTrue(Boolean condition, String errorMsg, int errorCode) {
        if (condition != null && condition) {
            throw new ApiException(errorCode, errorMsg);
        }
    }

    /**
     * object array collection map
     *
     * @param object the object
     */
    public static void notEmpty(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            throw new ApiBizException();
        }
    }

    /**
     * object array collection map
     *
     * @param object    the object
     * @param errorCode the error code
     */
    public static void notEmpty(Object object, int errorCode) {
        if (ObjectUtils.isEmpty(object)) {
            throw new ApiException(errorCode);
        }
    }

    /**
     * object array collection map
     *
     * @param object   the object
     * @param errorMsg the error msg
     */
    public static void notEmpty(Object object, String errorMsg) {
        if (ObjectUtils.isEmpty(object)) {
            throw new ApiBizException(errorMsg);
        }
    }

    /**
     * object array collection map
     *
     * @param object   the object
     * @param errorMsg the error msg
     * @param result   the result
     */
    public static void notEmpty(Object object, String errorMsg, Object result) {
        if (ObjectUtils.isEmpty(object)) {
            throw new ApiBizException(errorMsg, result);
        }
    }

    /**
     * object array collection map
     *
     * @param object    the object
     * @param errorMsg  the error msg
     * @param errorCode the error code
     */
    public static void notEmpty(Object object, String errorMsg, int errorCode) {
        if (ObjectUtils.isEmpty(object)) {
            throw new ApiException(errorCode, errorMsg);
        }
    }
}
