package com.gateweb.charge.frontEndIntegration.bean;

import com.gateweb.charge.frontEndIntegration.enumeration.PageableQueryStatus;

import java.util.List;

public class PageableQueryResponse {
    PageableQueryStatus pageableQueryStatus;
    List data;
    long totalCount;

    public PageableQueryResponse() {
    }

    public PageableQueryResponse(PageableQueryStatus pageableQueryStatus, List data, long totalCount) {
        this.pageableQueryStatus = pageableQueryStatus;
        this.data = data;
        this.totalCount = totalCount;
    }

    public PageableQueryStatus getPageableQueryStatus() {
        return pageableQueryStatus;
    }

    public void setPageableQueryStatus(PageableQueryStatus pageableQueryStatus) {
        this.pageableQueryStatus = pageableQueryStatus;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }


}
