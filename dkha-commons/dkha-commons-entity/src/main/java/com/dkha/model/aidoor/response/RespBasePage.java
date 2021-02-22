package com.dkha.model.aidoor.response;

import java.util.List;

public class RespBasePage<T> {
    private int totalCount;
    private int pageIndex;
    private int pageCount;
    private int pageSize;
    private List<T> dataInfo;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(List<T> dataInfo) {
        this.dataInfo = dataInfo;
    }
}
