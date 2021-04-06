package com.ugrong.framework.database.support.provider.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ugrong.framework.database.config.properties.DatabaseProperties;
import com.ugrong.framework.database.support.provider.EntityFieldProvider;
import com.ugrong.framework.database.vo.FieldInfo;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class DefaultEntityFieldProvider extends AbstractProvider<FieldInfo> implements EntityFieldProvider {

    public DefaultEntityFieldProvider(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }

    @Override
    protected boolean isSupport(TableField field) {
        return field == null || (!field.exist() && StringUtils.isNotBlank(field.value()));
    }

    @Override
    protected FieldInfo convert(TableField tableField, Field field) {
        return new FieldInfo(field);
    }
}
