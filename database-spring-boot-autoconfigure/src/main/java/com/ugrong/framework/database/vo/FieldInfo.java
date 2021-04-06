package com.ugrong.framework.database.vo;


import com.ugrong.framework.database.annotation.ChineseSupport;

import java.io.Serializable;
import java.lang.reflect.Field;

public class FieldInfo implements Serializable {

    private String fieldName;

    private Class<?> fieldType;

    private ChineseSupport chineseSupport;

    public FieldInfo() {
    }

    public FieldInfo(Field field) {
        this.fieldName = field.getName();
        this.fieldType = field.getType();
        this.chineseSupport = field.getAnnotation(ChineseSupport.class);
    }

    public boolean isChineseSort() {
        return this.fieldType == String.class && this.chineseSupport != null && this.chineseSupport.enable();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public ChineseSupport getChineseSupport() {
        return chineseSupport;
    }

    public void setChineseSupport(ChineseSupport chineseSupport) {
        this.chineseSupport = chineseSupport;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldType=" + fieldType +
                ", chineseSupport=" + chineseSupport +
                '}';
    }
}
