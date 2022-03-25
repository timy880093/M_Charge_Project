package com.gateweb.charge.dsl;

import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;

public interface SortableDslFieldRender {
    void orderByDslModifierSwitch(QueryBase<? extends JPAQuery> queryBase, String propertyName, String direction);
}
