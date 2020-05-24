package com.scut.bean;

import java.util.List;

public class Params{
    private Number pageNumber;
    private Number pageSize;
    private List<String> list;
    private Boolean tag;

    public Params(Integer pageNumber, Integer pageSize, List<String> list, Boolean tag) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.list = list;
        this.tag = tag;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "Parms{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", list=" + list +
                ", tag=" + tag +
                '}';
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public Number getPageNumber() {
        return pageNumber;
    }

    public Number getPageSize() {
        return pageSize;
    }

    public List<String> getList() {
        return list;
    }

    public Boolean getTag() {
        return tag;
    }

}
