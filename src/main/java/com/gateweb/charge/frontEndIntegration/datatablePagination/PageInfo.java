package com.gateweb.charge.frontEndIntegration.datatablePagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageInfo {
    int page;
    int start;
    int length;
    long totalCount;
    List<Order> order;
    List<ColumnDetail> columns;

    Map<String, Object> condition = new HashMap<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public List<ColumnDetail> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDetail> columns) {
        this.columns = columns;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(HashMap<String, Object> condition) {
        this.condition = condition;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
