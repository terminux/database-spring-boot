package com.ugrong.framework.database.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ugrong.framework.database.condition.Condition;
import com.ugrong.framework.database.condition.impl.QueryCondition;
import com.ugrong.framework.database.condition.impl.SortCondition;
import com.ugrong.framework.database.enums.EnumQueryOperator;
import com.ugrong.framework.database.model.AbstractModel;
import com.ugrong.framework.database.service.IBaseService;
import com.ugrong.framework.database.support.builder.LambdaQueryBuilder;
import com.ugrong.framework.database.support.builder.QueryBuilder;
import com.ugrong.framework.database.utils.Assert;
import com.ugrong.framework.database.utils.QueryUtil;
import com.ugrong.framework.database.vo.PageRequestVO;
import com.ugrong.framework.database.vo.PageVO;
import com.ugrong.framework.database.vo.RequestVO;
import com.ugrong.framework.database.vo.SqlConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 抽象Service 实现类
 *
 * @param <PK>     the type parameter
 * @param <Entity> the type parameter
 */
public abstract class AbstractBaseService<Mapper extends BaseMapper<Entity>, Entity extends AbstractModel<PK>, PK extends Serializable>
        extends ServiceImpl<Mapper, Entity> implements IBaseService<Entity, PK> {

    @Override
    public QueryWrapper<Entity> wrapper() {
        return new QueryWrapper<>();
    }

    @Override
    public LambdaQueryWrapper<Entity> lambdaWrapper() {
        return new LambdaQueryWrapper<>();
    }

    @Override
    public QueryWrapper<Entity> wrapper(RequestVO<Entity> request) {
        return this.toWrapper(request, null);
    }

    @Override
    public QueryWrapper<Entity> aliasWrapper(RequestVO<Entity> request) {
        return this.aliasWrapper(request, SqlConstants.DEFAULT_TABLE_ALIAS_NAME);
    }

    @Override
    public QueryWrapper<Entity> aliasWrapper(RequestVO<Entity> request, String alias) {
        return this.toWrapper(request, alias);
    }

    private QueryWrapper<Entity> toWrapper(RequestVO<Entity> request, String alias) {
        QueryWrapper<Entity> wrapper;
        Map<String, ColumnCache> columnMap;
        Class<Entity> entityClass = this.getEntityClass();
        if (StringUtils.isBlank(alias)) {
            wrapper = this.wrapper();
            columnMap = LambdaUtils.getColumnMap(entityClass);
        } else {
            wrapper = new QueryBuilder<>(null, alias);
            columnMap = LambdaQueryBuilder.getAliasColumnMap(entityClass, alias);
        }
        this.parseCondition(wrapper, columnMap, request.getSearch());
        this.parseCondition(wrapper, columnMap, request.getSort());
        return wrapper;
    }

    @Override
    public LambdaQueryWrapper<Entity> aliasLambdaWrapper() {
        return this.aliasLambdaWrapper(SqlConstants.DEFAULT_TABLE_ALIAS_NAME);
    }

    @Override
    public LambdaQueryWrapper<Entity> aliasLambdaWrapper(String alias) {
        return new LambdaQueryBuilder<>(this.getEntityClass(), alias);
    }

    @Override
    public LambdaQueryWrapper<Entity> aliasLambdaWrapper(RequestVO<Entity> request) {
        return this.aliasLambdaWrapper(request, SqlConstants.DEFAULT_TABLE_ALIAS_NAME);
    }

    @Override
    public LambdaQueryWrapper<Entity> aliasLambdaWrapper(RequestVO<Entity> request, String alias) {
        return this.aliasWrapper(request, alias).lambda();
    }

    @Override
    public Wrapper<Entity> singleton(Wrapper<Entity> wrapper) {
        return this.top(wrapper, 1);
    }

    @Override
    public Wrapper<Entity> top(Wrapper<Entity> wrapper, Integer end) {
        return this.limit(wrapper, 0, end);
    }

    @Override
    public Wrapper<Entity> limit(Wrapper<Entity> wrapper, Integer start, Integer end) {
        Assert.isTrue(ObjectUtils.allNotNull(start, end), "缺少 limit 参数");
        if (wrapper instanceof Join) {
            ((Join<?>) wrapper).last(SqlConstants.SQL_KEYWORD_LIMIT.concat(StringUtils.SPACE).concat(start.toString()).concat(",").concat(end.toString()));
        }
        return wrapper;
    }

    private <C extends Condition> void parseCondition(QueryWrapper<Entity> wrapper, Map<String, ColumnCache> columnMap, List<C> conditions) {
        if (CollectionUtils.isNotEmpty(conditions)) {
            for (C condition : conditions) {
                this.validCondition(condition, columnMap);
                condition.wrapper(wrapper);
            }
        }
    }

    private <C extends Condition> void validCondition(C condition, Map<String, ColumnCache> columnMap) {
        condition.setColumnName(this.validField(condition.getField(), columnMap));
        if (condition instanceof QueryCondition) {
            QueryCondition query = (QueryCondition) condition;
            EnumQueryOperator operator = query.getOperator();
            Assert.notNull(operator, "要执行的查询操作不能为空");

            Object value = query.getValue();
            if (operator != EnumQueryOperator.ISNULL && operator != EnumQueryOperator.NOTNULL) {
                Assert.notEmpty(value, "查询值不能为空");
                if (operator == EnumQueryOperator.IN || operator == EnumQueryOperator.NIN) {
                    Assert.isTrue(value instanceof Collection, "操作类型为[IN]或[NIN]时，[value]只能为集合类型");
                }
            }
        } else if (condition instanceof SortCondition) {
            SortCondition sort = (SortCondition) condition;
            Assert.notNull(sort.getType(), "排序类型不能为空");
        }
    }

    private String validField(String field, Map<String, ColumnCache> columnMap) {
        Assert.notBlank(field, "要操作的字段名不能为空");
        ColumnCache column = columnMap.get(field.toUpperCase());
        Assert.isTrue(column != null && StringUtils.isNotBlank(column.getColumn()), String.format("不支持的字段[%s]", field));
        return column.getColumn();
    }

    @Override
    public Optional<Entity> findOne(Wrapper<Entity> wrapper) {
        return Optional.ofNullable(this.getOne(this.singleton(wrapper)));
    }

    @Override
    public Optional<Entity> findById(PK id) {
        return Optional.ofNullable(this.getById(id));
    }

    @Override
    public boolean existById(PK id) {
        return this.exist(wrapper().eq(TableInfoHelper.getTableInfo(this.getEntityClass()).getKeyColumn(), id));
    }

    @Override
    public boolean exist(Wrapper<Entity> wrapper) {
        if (wrapper != null) {
            return this.count(this.singleton(wrapper)) > 0;
        }
        return false;
    }

    @Override
    public PageVO<Entity> page(PageRequestVO<Entity> request) {
        return this.page(request, this::list);
    }

    @Override
    public PageVO<Entity> page(PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction) {
        return this.page(this.wrapper(request), request, listFunction);
    }

    @Override
    public PageVO<Entity> page(Wrapper<Entity> wrapper, PageRequestVO<Entity> request) {
        return this.page(wrapper, request, this::list);
    }

    @Override
    public PageVO<Entity> page(Wrapper<Entity> wrapper, Integer pageNo, Integer pageSize) {
        return this.page(wrapper, pageNo, pageSize, this::list);
    }

    @Override
    public <R> PageVO<R> page(PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction, Function<List<Entity>, List<R>> convert) {
        return this.page(this.wrapper(request), request, listFunction, convert);
    }

    @Override
    public PageVO<Entity> page(Wrapper<Entity> wrapper, PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction) {
        return this.page(wrapper, request.getPageNo(), request.getPageSize(), listFunction);
    }

    @Override
    public PageVO<Entity> page(Wrapper<Entity> wrapper, Integer pageNo, Integer pageSize, Function<Wrapper<Entity>, List<Entity>> listFunction) {
        return QueryUtil.page(wrapper, pageNo, pageSize, listFunction);
    }

    @Override
    public <R> PageVO<R> page(Wrapper<Entity> wrapper, PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction, Function<List<Entity>, List<R>> convert) {
        return this.page(wrapper, request.getPageNo(), request.getPageSize(), listFunction, convert);
    }

    @Override
    public <R> PageVO<R> page(Wrapper<Entity> wrapper, Integer pageNo, Integer pageSize, Function<Wrapper<Entity>, List<Entity>> listFunction, Function<List<Entity>, List<R>> convert) {
        return QueryUtil.page(wrapper, pageNo, pageSize, listFunction, convert);
    }
}
