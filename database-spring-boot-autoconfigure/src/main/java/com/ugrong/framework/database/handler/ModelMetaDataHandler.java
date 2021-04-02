package com.ugrong.framework.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ugrong.framework.database.utils.DataFillUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ModelMetaDataHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject object) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(object, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(object, "modifyTime", LocalDateTime.class, now);
        this.strictInsertFill(object, "deleted", Boolean.class, Boolean.FALSE);
        Serializable currentUserId = DataFillUtil.getCurrentUserId();
        this.strictInsertFill(object, "createBy", Serializable.class, currentUserId);
        this.strictInsertFill(object, "modifyBy", Serializable.class, currentUserId);
        this.strictInsertFill(object, "tenantId", Serializable.class, DataFillUtil.getTenantId());
    }

    @Override
    public void updateFill(MetaObject object) {
        this.setFieldValByName("modifyTime", LocalDateTime.now(), object);
        this.setFieldValByName("modifyBy", DataFillUtil.getCurrentUserId(), object);
    }


}