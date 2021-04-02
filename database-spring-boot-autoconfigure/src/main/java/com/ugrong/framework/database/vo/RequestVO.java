package com.ugrong.framework.database.vo;

import com.ugrong.framework.database.condition.impl.QueryCondition;
import com.ugrong.framework.database.condition.impl.SortCondition;
import com.ugrong.framework.database.utils.Assert;
import com.ugrong.framework.database.utils.CollectionUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApiModel(description = "公共请求参数格式")
public class RequestVO<T> implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(RequestVO.class);

    /**
     * 动态查询条件
     */
    @ApiModelProperty(value = "动态查询条件", position = 102)
    protected List<QueryCondition> search = new ArrayList<>();

    /**
     * 排序参数
     */
    @ApiModelProperty(value = "排序参数", position = 103)
    protected List<SortCondition> sort = new ArrayList<>();

    @ApiModelProperty(value = "该字段不作为请求参数，仅用于api文档展示，表示可选的查询或排序字段，对应 search -> field 和 sort -> field 的值", position = 104)
    protected T availableFields;

    public RequestVO() {
    }

    public RequestVO(RequestVO<?> request, Class<T> clazz) {
        this.search = request.getSearch();
        this.sort = request.getSort();
    }

    public List<QueryCondition> getSearch() {
        return search;
    }

    public void setSearch(List<QueryCondition> search) {
        this.search = search;
    }

    public List<SortCondition> getSort() {
        return sort;
    }

    public void setSort(List<SortCondition> sort) {
        this.sort = sort;
    }

    public T getAvailableFields() {
        return availableFields;
    }

    public void setAvailableFields(T availableFields) {
        this.availableFields = availableFields;
    }

    /**
     * 将 search 中的参数赋值给 availableFields 属性并且返回
     * （有些场景需要用到原始参数，从 search 中获取过于麻烦，因此可以调用此方法获取）
     *
     * @param clazz 可选字段的clazz
     * @return 原始参数
     */
    public T getOriginParam(Class<T> clazz) {
        Assert.notNull(clazz, "原参数的class不能为空");
        T originParam = this.getAvailableFields();
        if (originParam != null) {
            return originParam;
        }
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
            originParam = clazz.newInstance();
        } catch (Exception e) {
            log.error("Failed to get origin param.", e);
        }
        if (beanInfo != null && originParam != null) {
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            if (ArrayUtils.isNotEmpty(descriptors)) {
                List<QueryCondition> search = this.getSearch();
                if (CollectionUtils.isNotEmpty(search)) {
                    Map<String, QueryCondition> group = CollectionUtil.singletonGroupBy(search, QueryCondition::getField);
                    for (PropertyDescriptor descriptor : descriptors) {
                        String fieldName = descriptor.getName();
                        Method setter = descriptor.getWriteMethod();
                        if (setter == null) {
                            continue;
                        }
                        QueryCondition condition = group.get(fieldName);
                        if (condition == null || condition.getValue() == null) {
                            continue;
                        }
                        try {
                            setter.invoke(originParam, condition.getValue());
                        } catch (Exception e) {
                            log.error("Failed to invoke [{}] property.", fieldName, e);
                        }
                    }
                }
            }
            this.setAvailableFields(originParam);
        }
        return originParam;
    }

    @Override
    public String toString() {
        return "RequestVO{" +
                "search=" + search +
                ", sort=" + sort +
                ", availableFields=" + availableFields +
                '}';
    }
}
