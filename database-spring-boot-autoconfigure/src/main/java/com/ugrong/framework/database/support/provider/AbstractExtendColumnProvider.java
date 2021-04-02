package com.ugrong.framework.database.support.provider;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.ugrong.framework.database.config.properties.DatabaseProperties;
import com.ugrong.framework.database.model.LongModel;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractExtendColumnProvider implements ExtendColumnProvider {

    protected final DatabaseProperties databaseProperties;

    public AbstractExtendColumnProvider(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @PostConstruct
    protected void init() throws Exception {
        ClassGraph graph = new ClassGraph().enableClassInfo().enableAnnotationInfo();
        String[] entityPackages = databaseProperties.getEntityPackages();
        if (ArrayUtils.isNotEmpty(entityPackages)) {
            //扫描指定包
            graph.acceptPackages(entityPackages);
        }
        ScanResult result = graph.scan();
        List<Class<LongModel>> classes = result.getClassesWithAnnotation(TableName.class.getName()).loadClasses(LongModel.class);
        if (ObjectUtils.isNotEmpty(classes)) {
            classes.forEach(clazz -> {
                Map<String, ColumnCache> mapping = this.getMapping(clazz);
                if (ObjectUtils.isNotEmpty(mapping)) {
                    this.put(clazz, mapping);
                }
            });
        }
    }

    protected Map<String, ColumnCache> getMapping(Class<?> clazz) {
        Map<String, ColumnCache> mapping = new HashMap<>();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            if (ArrayUtils.isNotEmpty(declaredFields)) {
                for (Field field : declaredFields) {
                    try {
                        TableField tableField = field.getAnnotation(TableField.class);
                        if (tableField == null || tableField.exist() || StringUtils.isBlank(tableField.value())) {
                            continue;
                        }
                        String column = tableField.value();
                        mapping.put(field.getName().toUpperCase(), new ColumnCache(column, column));
                    } catch (Exception e) {
                        //ignore exception
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return mapping;
    }

    @Override
    public ColumnCache get(Class<?> clazz, String fieldName) {
        Map<String, ColumnCache> mapping = this.get(clazz);
        if (ObjectUtils.isNotEmpty(mapping) && StringUtils.isNotBlank(fieldName)) {
            return mapping.get(fieldName.toUpperCase());
        }
        return null;
    }

    @Override
    public Collection<ColumnCache> getAll(Class<?> clazz) {
        Map<String, ColumnCache> mapping = this.get(clazz);
        if (ObjectUtils.isNotEmpty(mapping)) {
            return mapping.values();
        }
        return null;
    }
}
