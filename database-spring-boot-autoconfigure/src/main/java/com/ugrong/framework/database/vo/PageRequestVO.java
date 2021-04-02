package com.ugrong.framework.database.vo;

import io.swagger.annotations.ApiModelProperty;

public class PageRequestVO<T> extends RequestVO<T> {

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

    public PageRequestVO() {
    }

    public PageRequestVO(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageRequestVO(PageRequestVO<?> request, Class<T> clazz) {
        super(request, clazz);
        this.pageNo = request.getPageNo();
        this.pageSize = request.getPageSize();
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

    @Override
    public String toString() {
        return "PageRequestVO{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", search=" + search +
                ", sort=" + sort +
                ", availableFields=" + availableFields +
                '}';
    }
}
