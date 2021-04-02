package com.ugrong.framework.database.domain;

import java.io.Serializable;

/**
 * The interface Predefined val holder.
 */
public interface PredefinedValHolder {

    /**
     * 获取当前登录的用户id.
     *
     * @return the current user id
     */
    <PK extends Serializable> PK getCurrentUserId();

    /**
     * 生成主键.
     *
     * @return the pk
     */
    <PK extends Serializable> PK generatePrimaryKey();

    /**
     * 获取当前的租户id.
     *
     * @return the tenant
     */
    <PK extends Serializable> PK getTenantId();
}
