package com.gateweb.charge.dsl;

import com.querydsl.core.support.QueryBase;

import java.util.Map;

public interface CriteriaPageableSearchBuilder {
    QueryBase buildCriteriaPageableSearch(Map<String, Object> conditionMap);
}
