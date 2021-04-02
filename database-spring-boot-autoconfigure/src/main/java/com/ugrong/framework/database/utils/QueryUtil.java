package com.ugrong.framework.database.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ugrong.framework.database.vo.PageVO;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.function.Function;

public class QueryUtil {

    private QueryUtil() {

    }

    public static <P, T> PageVO<T> page(P param, Integer pageNo, Integer pageSize, Function<P, List<T>> listFunction) {
        PageVO<T> vo = new PageVO<>();
        List<T> data = initPage(param, pageNo, pageSize, vo, listFunction);
        vo.setData(data);
        return vo;
    }

    public static <P, T, R> PageVO<R> page(P param, Integer pageNo, Integer pageSize, Function<P, List<T>> listFunction, Function<List<T>, List<R>> convert) {
        PageVO<R> vo = new PageVO<>();
        List<T> data = initPage(param, pageNo, pageSize, vo, listFunction);
        Assert.notNull(convert, "convert不能为空");
        if (ObjectUtils.isNotEmpty(data)) {
            vo.setData(convert.apply(data));
        }
        return vo;
    }

    private static <P, T, R> List<T> initPage(P param, Integer pageNo, Integer pageSize,
                                              PageVO<R> vo, Function<P, List<T>> listFunction) {
        Assert.isTrue(ObjectUtils.allNotNull(param, listFunction), "缺少查询参数");
        Assert.isTrue(pageNo != null && pageNo > 0 && pageSize != null && pageSize > 0, "分页参数不合法");
        vo.setPageNo(pageNo);
        vo.setPageSize(pageSize);

        Page<T> page = PageHelper.startPage(vo.getPageNo(), vo.getPageSize())
                .doSelectPage(() -> listFunction.apply(param));

        vo.setTotalCount(page.getTotal());
        vo.setTotalPage(page.getPages());
        return page.getResult();
    }
}
