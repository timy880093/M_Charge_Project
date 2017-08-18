package com.gate.web.beans;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/3/24
 * Time: 下午 5:25
 * To change this template use File | Settings | File Templates.
 */
public class QuerySettingVO {
    private String funcId;
    private String searchField;
    private String searchString;
    private String sidx;
    private String sord;
    private Integer page;
    private Integer rows;
    private Map SearchMap;

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Map getSearchMap() {
        return SearchMap;
    }

    public void setSearchMap(Map searchMap) {
        SearchMap = searchMap;
    }
}
