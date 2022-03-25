package com.gateweb.charge.dsl;

import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.Map;

public interface SortableDslDispatcher {

    void orderByDslModifierDispatcher(QueryBase<? extends JPAQuery> qb, PageInfo pageInfo);

    void orderByDslModifierDispatcher(QueryBase<? extends JPAQuery> qb, Map orderMap);
}
