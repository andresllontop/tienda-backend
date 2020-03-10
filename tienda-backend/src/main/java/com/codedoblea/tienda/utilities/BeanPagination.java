package com.codedoblea.tienda.utilities;


import java.util.List;

public class BeanPagination {

    private Long count_filter;
    private List<?> list;
  

    public Long getCount_filter() {
        return count_filter;
    }

    public void setCount_filter(Long count_filter) {
        this.count_filter = count_filter;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BeanPagination{" + "count_filter=" + count_filter + ", list=" + list + '}';
    }

}
