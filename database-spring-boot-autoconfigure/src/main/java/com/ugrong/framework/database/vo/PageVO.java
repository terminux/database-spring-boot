package com.ugrong.framework.database.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class PageVO<T> implements Serializable {

    /**
     * 页码（当前第几页）
     */
    @ApiModelProperty(value = "页码（当前第几页 默认1）", example = "1", position = 100)
    protected int pageNo = 1;

    /**
     * 页记录数
     */
    @ApiModelProperty(value = "页记录数（一页多少条 默认10）", example = "10", position = 101)
    protected int pageSize = 10;

    /**
     * 总记录条数
     */
    @ApiModelProperty(value = "总记录条数", example = "100", position = 102)
    private long totalCount;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数（总共多少页）", example = "10", position = 103)
    private int totalPage;

    /**
     * 返回数据List
     */
    @ApiModelProperty(value = "返回数据", position = 104)
    private List<T> data = Collections.emptyList();

    public PageVO() {

    }

    public PageVO(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageVO(List<T> data, long totalCount) {
        this.totalCount = totalCount;
        this.data = data;
    }


    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
