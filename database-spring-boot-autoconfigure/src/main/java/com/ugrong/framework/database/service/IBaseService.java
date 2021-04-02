package com.ugrong.framework.database.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ugrong.framework.database.model.AbstractModel;
import com.ugrong.framework.database.vo.PageRequestVO;
import com.ugrong.framework.database.vo.PageVO;
import com.ugrong.framework.database.vo.RequestVO;
import com.ugrong.framework.database.vo.SqlConstants;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 基础Service接口.
 *
 * @param <Entity> the type parameter
 * @param <PK>     the type parameter
 */
public interface IBaseService<Entity extends AbstractModel<PK>, PK extends Serializable> extends IService<Entity> {

    /**
     * new QueryWrapper<>().
     *
     * @return the query wrapper
     */
    QueryWrapper<Entity> wrapper();

    /**
     * RequestVO 转 QueryWrapper.
     *
     * @param request the request
     * @return the query wrapper
     */
    QueryWrapper<Entity> wrapper(RequestVO<Entity> request);

    /**
     * RequestVO 转 QueryWrapper 并且支持联表查询 使用默认的 {@link SqlConstants#DEFAULT_TABLE_ALIAS_NAME} 表别名.
     *
     * @param request the request
     * @return the query wrapper
     */
    QueryWrapper<Entity> aliasWrapper(RequestVO<Entity> request);

    /**
     * RequestVO 转 QueryWrapper 并且支持联表查询 使用自定义的表别名.
     *
     * @param request the request
     * @param alias   the alias
     * @return the query wrapper
     */
    QueryWrapper<Entity> aliasWrapper(RequestVO<Entity> request, String alias);

    /**
     * new LambdaQueryWrapper<>().
     *
     * @return the query wrapper
     */
    LambdaQueryWrapper<Entity> lambdaWrapper();

    /**
     * 支持联表查询的LambdaQueryWrapper 使用默认的 {@link SqlConstants#DEFAULT_TABLE_ALIAS_NAME} 表别名.
     *
     * @return the lambda query wrapper
     */
    LambdaQueryWrapper<Entity> aliasLambdaWrapper();

    /**
     * 支持联表查询的LambdaQueryWrapper 使用自定义的表别名.
     *
     * @param alias the alias
     * @return the lambda query wrapper
     */
    LambdaQueryWrapper<Entity> aliasLambdaWrapper(String alias);

    /**
     * RequestVO 转 LambdaQueryWrapper 并且支持联表查询 使用默认的 {@link SqlConstants#DEFAULT_TABLE_ALIAS_NAME} 表别名.
     *
     * @param request the request
     * @return the lambda query wrapper
     */
    LambdaQueryWrapper<Entity> aliasLambdaWrapper(RequestVO<Entity> request);

    /**
     * RequestVO 转 LambdaQueryWrapper 并且支持联表查询 使用自定义的表别名.
     *
     * @param request the request
     * @param alias   the alias
     * @return the lambda query wrapper
     */
    LambdaQueryWrapper<Entity> aliasLambdaWrapper(RequestVO<Entity> request, String alias);

    /**
     * 将指定Wrapper转换为只匹配一条记录的Wrapper.
     *
     * @param wrapper 查询条件
     * @return the wrapper
     */
    Wrapper<Entity> singleton(Wrapper<Entity> wrapper);

    /**
     * 查询指定前几条记录 limit {end}.
     *
     * @param wrapper the wrapper
     * @param end     the end
     * @return the wrapper
     */
    Wrapper<Entity> top(Wrapper<Entity> wrapper, Integer end);

    /**
     * 给指定Wrapper追加limit.
     *
     * @param wrapper the wrapper
     * @param start   the start
     * @param end     the end
     * @return the wrapper
     */
    Wrapper<Entity> limit(Wrapper<Entity> wrapper, Integer start, Integer end);

    /**
     * 根据条件查找一条数据信息
     *
     * @param wrapper 查询条件
     * @return 查询到的数据信息 optional
     */
    Optional<Entity> findOne(Wrapper<Entity> wrapper);

    /**
     * 根据主键查找
     *
     * @param id 主键
     * @return 查询到的数据信息 optional
     */
    Optional<Entity> findById(PK id);

    /**
     * 根据id判断是否存在匹配的记录
     *
     * @param id 主键
     * @return true :存在 false:不存在
     */
    boolean existById(PK id);

    /**
     * 根据构建条件判断是否存在匹配的记录
     *
     * @param wrapper 查询条件
     * @return true :存在 false:不存在
     */
    boolean exist(Wrapper<Entity> wrapper);

    /**
     * 分页查询 使用默认的 {@link IService#list(Wrapper)} 查询方法
     *
     * @param request the page request
     * @return the page vo
     */
    PageVO<Entity> page(PageRequestVO<Entity> request);

    /**
     * 分页查询 自定义查询方法.
     *
     * @param request      the page request
     * @param listFunction the list function
     * @return the page vo
     */
    PageVO<Entity> page(PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction);

    /**
     * Page page vo.
     *
     * @param wrapper the wrapper
     * @param request the request
     * @return the page vo
     */
    PageVO<Entity> page(Wrapper<Entity> wrapper, PageRequestVO<Entity> request);

    /**
     * 使用自定义构造的wrapper进行分页查询 并且使用默认的 {@link IService#list(Wrapper)} 查询方法.
     *
     * @param wrapper      the wrapper
     * @param request      the request
     * @param listFunction the list function
     * @return the page vo
     */
    PageVO<Entity> page(Wrapper<Entity> wrapper, PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction);

    /**
     * 使用自定义构造的wrapper进行分页查询 并且使用默认的 {@link IService#list(Wrapper)} 查询方法.
     *
     * @param wrapper  the wrapper
     * @param pageNo   the page no
     * @param pageSize the page size
     * @return the page vo
     */
    PageVO<Entity> page(Wrapper<Entity> wrapper, Integer pageNo, Integer pageSize);

    /**
     * 使用自定义构造的wrapper进行分页查询.
     *
     * @param wrapper      the wrapper
     * @param pageNo       the page no
     * @param pageSize     the page size
     * @param listFunction the list function
     * @return the page vo
     */
    PageVO<Entity> page(Wrapper<Entity> wrapper, Integer pageNo, Integer pageSize, Function<Wrapper<Entity>, List<Entity>> listFunction);

    /**
     * 分页查询 自定义查询方法 并且支持最终返回值的转换.
     *
     * @param <R>          the type parameter
     * @param request      the request
     * @param listFunction the list function
     * @param convert      the convert
     * @return the page vo
     */
    <R> PageVO<R> page(PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction, Function<List<Entity>, List<R>> convert);

    /**
     * 使用自定义构造的wrapper进行分页查询 并且支持最终返回值的转换.
     *
     * @param <R>          the type parameter
     * @param wrapper      the wrapper
     * @param request      the request
     * @param listFunction the list function
     * @param convert      the convert
     * @return the page vo
     */
    <R> PageVO<R> page(Wrapper<Entity> wrapper, PageRequestVO<Entity> request, Function<Wrapper<Entity>, List<Entity>> listFunction, Function<List<Entity>, List<R>> convert);

    /**
     * 使用自定义构造的wrapper进行分页查询 并且支持最终返回值的转换.
     *
     * @param <R>          the type parameter
     * @param wrapper      the wrapper
     * @param pageNo       the page no
     * @param pageSize     the page size
     * @param listFunction the list function
     * @param convert      the convert
     * @return the page vo
     */
    <R> PageVO<R> page(Wrapper<Entity> wrapper, Integer pageNo, Integer pageSize, Function<Wrapper<Entity>, List<Entity>> listFunction, Function<List<Entity>, List<R>> convert);
}
