package com.ugrong.framework.database.support.provider.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.ugrong.framework.database.config.properties.DatabaseProperties;
import com.ugrong.framework.database.support.provider.ExtendColumnProvider;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class DefaultExtendColumnProvider extends AbstractProvider<ColumnCache> implements ExtendColumnProvider {

    public DefaultExtendColumnProvider(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }

    @Override
    protected boolean isSupport(TableField field) {
        return field != null && !field.exist() && StringUtils.isNotBlank(field.value());
    }

    @Override
    protected ColumnCache convert(TableField tableField, Field field) {
        String column = tableField.value();
        return new ColumnCache(column, column);
    }
}
