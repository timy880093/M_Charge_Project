package com.gateweb.charge.dsl;

import com.querydsl.core.support.QueryBase;

import java.util.Map;

public interface DslSearchConditionRender {
    void searchConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap);
}
