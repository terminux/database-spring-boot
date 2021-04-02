package com.ugrong.framework.database.enums;

/**
 * 排序方式
 */
public enum EnumSortType {

    ASC("升序"), DESC("降序");

    private final String desc;

    EnumSortType(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return this.desc;
    }
}
