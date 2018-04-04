package com.bov.mt.utils.page;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

    private int currentPage;//当前页

    private int totalPage;//总页数

    private int totalCount;//总记录数

    private int offset; //偏移量

    private int everyPage ; //每页记录数

    private List<T> data = new ArrayList<>();//分页集合数据

    private boolean hasNextPage; //是否有后一页

    private boolean hasPrevious; //是否有前一页

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;

    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        if (totalCount % everyPage == 0)
            totalPage = totalCount / everyPage;
        else
            totalPage = totalCount / everyPage + 1;
        this.totalCount = totalCount;
    }

    public int getOffset() {
        offset = (this.currentPage-1) * everyPage;
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getEveryPage() {
        return everyPage;
    }

    public void setEveryPage(int everyPage) {
        this.everyPage = everyPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isHasNextPage() {
        if (currentPage < totalPage )
            hasNextPage = true;
        else
            hasNextPage = false;
        return hasNextPage;
    }

    public boolean isHasPrevious() {
        if (currentPage == 1)
            hasPrevious = false;
        else
            hasPrevious = true;
        return hasPrevious;
    }

}
