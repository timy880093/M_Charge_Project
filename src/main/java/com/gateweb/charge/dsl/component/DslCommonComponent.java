package com.gateweb.charge.dsl.component;

import com.gateweb.charge.dsl.SortableDslDispatcher;
import com.gateweb.charge.dsl.SortableDslFieldRender;
import com.gateweb.charge.frontEndIntegration.datatablePagination.Order;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
/**
 * Dsl的核心方法，用於分離藕合，把與介面相關的工作交付給commonService就可以避免有限繼承的困境
 * 但要指定Interface進行處理，也可以做為未來新增時的檢查
 */
public class DslCommonComponent {

    public void orderByPath(QueryBase queryBase, Path path, String direction) {
        ComparableExpressionBase comparableExpressionBase = (ComparableExpressionBase) path;
        if (direction.toUpperCase().equals("ASC")) {
            queryBase.orderBy(comparableExpressionBase.asc());
        }
        if (direction.toUpperCase().equals("DESC")) {
            queryBase.orderBy(comparableExpressionBase.desc());
        }
    }

    public void orderByDslModifier(SortableDslFieldRender nativeModelDslService, QueryBase queryBase, PageInfo pageInfo) {
        for (Order order : pageInfo.getOrder()) {
            if (pageInfo.getColumns().get(order.getColumn()).sortBy == null) {
                String columnName = pageInfo.getColumns().get(order.getColumn()).getName();
                String direction = order.getDir();
                nativeModelDslService.orderByDslModifierSwitch(queryBase, columnName, direction);
            } else {
                int targetColumn = pageInfo.getColumns().get(order.getColumn()).sortBy;
                String columnName = pageInfo.getColumns().get(targetColumn).getName();
                nativeModelDslService.orderByDslModifierSwitch(queryBase, columnName, order.getDir());
            }
        }
    }

    public void orderByDslModifier(SortableDslFieldRender nativeModelDslService, QueryBase queryBase, Map<String, Object> orderMap) {
        orderMap.keySet().stream().forEach(key -> {
            String direction = (String) orderMap.get(key);
            nativeModelDslService.orderByDslModifierSwitch(queryBase, key, direction);
        });
    }

    public Map<String, Object> fetchData(QueryBase qb) {
        HashMap<String, Object> resultMap = new HashMap<>();
        Long totalCount = ((JPAQuery) qb).fetchCount();
        List<Object> resultList = ((JPAQuery) qb).fetch();
        resultMap.put("totalCount", totalCount);
        resultMap.put("data", resultList);
        return resultMap;
    }

    public Map<String, Object> fetchPageData(
            SortableDslDispatcher sortableDslService
            , QueryBase qb
            , int page
            , int length
            , Map<String, Object> orderMap) {
        Map<String, Object> resultMap = new HashMap<>();
        Long totalCount = ((JPAQuery) qb).fetchCount();
        if (orderMap != null) {
            qb.offset(page * length);
            sortableDslService.orderByDslModifierDispatcher(qb, orderMap);
            qb.limit(length);
        }
        List<Object> resultList = ((JPAQuery) qb).fetch();
        resultMap.put("totalCount", totalCount);
        resultMap.put("data", resultList);
        return resultMap;
    }

    public List fetchPageData(
            SortableDslDispatcher sortableDslService
            , QueryBase qb
            , PageInfo pageInfo) {
        if (pageInfo != null) {
            Long totalCount = ((JPAQuery) qb).fetchCount();
            pageInfo.setTotalCount(totalCount);
            qb.offset(pageInfo.getPage() * pageInfo.getLength());
            sortableDslService.orderByDslModifierDispatcher(qb, pageInfo);
            qb.limit(pageInfo.getLength());
        }
        return ((JPAQuery) qb).fetch();
    }

}
