package com.gateweb.charge.dsl.component;

import com.gateweb.charge.dsl.SortableDslDispatcher;
import com.gateweb.charge.dsl.SortableDslFieldRender;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.dsl.CriteriaPageableSearchBuilder;
import com.gateweb.orm.charge.entity.QProduct;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;

@Component
public class ProductDslComponent implements SortableDslFieldRender, SortableDslDispatcher
        , CriteriaPageableSearchBuilder {
    private QProduct qProduct = QProduct.product;

    @Autowired
    DslCommonComponent dslCommonService;
    @Autowired
    @Qualifier("chargeEntityManager")
    protected EntityManager chargeEntityManager;

    @Override
    public void orderByDslModifierSwitch(QueryBase queryBase, String propertyName, String direction) {
        if (propertyName.equals("productId")) {
            dslCommonService.orderByPath(queryBase, qProduct.productId, direction);
        }
        if (propertyName.equals("productName")) {
            dslCommonService.orderByPath(queryBase, qProduct.productName, direction);
        }
        if (propertyName.equals("productCategoryId")) {
            dslCommonService.orderByPath(queryBase, qProduct.productCategoryId, direction);
        }
    }

    @Override
    public QueryBase buildCriteriaPageableSearch(Map<String, Object> conditionMap) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(chargeEntityManager);
        JPAQuery jpaQuery = queryFactory
                .selectFrom(qProduct);
        QueryBase qb = jpaQuery;
        return qb;
    }

    @Override
    public void orderByDslModifierDispatcher(QueryBase qb, PageInfo pageInfo) {
        dslCommonService.orderByDslModifier(this, qb, pageInfo);
    }

    @Override
    public void orderByDslModifierDispatcher(QueryBase qb, Map orderMap) {
        dslCommonService.orderByDslModifier(this, qb, orderMap);
    }
}