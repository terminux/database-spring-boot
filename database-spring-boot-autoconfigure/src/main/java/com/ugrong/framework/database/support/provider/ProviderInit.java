package com.ugrong.framework.database.support.provider;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ugrong.framework.database.config.properties.DatabaseProperties;
import com.ugrong.framework.database.model.LongModel;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

public class ProviderInit {

    private final DatabaseProperties databaseProperties;

    private final Provider<?>[] providers;

    public ProviderInit(DatabaseProperties databaseProperties, Provider<?>... providers) {
        this.databaseProperties = databaseProperties;
        this.providers = providers;
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
        if (CollectionUtils.isNotEmpty(classes)) {
            classes.forEach(clazz -> {
                Class<?> fieldClass = clazz;
                while (fieldClass != null) {
                    Field[] declaredFields = fieldClass.getDeclaredFields();
                    if (ArrayUtils.isNotEmpty(declaredFields)) {
                        for (Field field : declaredFields) {
                            for (Provider<?> provider : providers) {
                                try {
                                    provider.init(clazz, field);
                                } catch (Exception e) {
                                    //ignore exception
                                }
                            }
                        }
                    }
                    fieldClass = fieldClass.getSuperclass();
                }
            });
        }
    }

}
