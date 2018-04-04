package com.bov.mt.utils.page;

public class PageFactory {

    private PageFactory(){}

    public static <T> Page<T> getPage(int currentPage, int everyPage, int totalCount){
        return initPage(currentPage, everyPage, totalCount);
    }

    private static <T> Page<T> initPage(int currentPage, int everyPage, int totalCount){
        Page<T> page = new Page<T>();
        page.setCurrentPage(currentPage);
        page.setEveryPage(everyPage);
        page.setTotalCount(totalCount);
        return page;
    }

}
