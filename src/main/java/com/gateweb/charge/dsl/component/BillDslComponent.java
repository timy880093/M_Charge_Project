package com.gateweb.charge.dsl.component;

import com.gateweb.charge.dsl.CriteriaPageableSearchBuilder;
import com.gateweb.charge.dsl.DslSearchConditionRender;
import com.gateweb.charge.dsl.SortableDslDispatcher;
import com.gateweb.charge.dsl.SortableDslFieldRender;
import com.gateweb.charge.enumeration.BillStatus;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.QBill;
import com.gateweb.orm.charge.entity.QCompany;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;

@Component
public class BillDslComponent implements SortableDslDispatcher, SortableDslFieldRender
        , DslSearchConditionRender, CriteriaPageableSearchBuilder {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    DslCommonComponent dslCommonComponent;
    @Autowired
    BillingItemDslComponent billingItemDslComponent;
    @Autowired
    CompanyDslComponent companyDslService;
    @Autowired
    @Qualifier("chargeEntityManager")
    protected EntityManager chargeEntityManager;

    QBill qBill = QBill.bill;
    QCompany qCompany = QCompany.company;

    @Override
    public void orderByDslModifierDispatcher(QueryBase<? extends JPAQuery> qb, PageInfo pageInfo) {
        dslCommonComponent.orderByDslModifier(this, qb, pageInfo);
        dslCommonComponent.orderByDslModifier(companyDslService, qb, pageInfo);
    }

    @Override
    public void orderByDslModifierDispatcher(QueryBase<? extends JPAQuery> qb, Map orderMap) {
        dslCommonComponent.orderByDslModifier(this, qb, orderMap);
        dslCommonComponent.orderByDslModifier(companyDslService, qb, orderMap);
    }

    @Override
    public void orderByDslModifierSwitch(QueryBase queryBase, String propertyName, String direction) {
        if (propertyName.equals("billId")) {
            dslCommonComponent.orderByPath(queryBase, qBill.billId, direction);
        }
        if (propertyName.equals("inDate")) {
            dslCommonComponent.orderByPath(queryBase, qBill.createDate, direction);
        }
        if (propertyName.equals("createDate")) {
            dslCommonComponent.orderByPath(queryBase, qBill.createDate, direction);
        }
        if (propertyName.equals("billStatus")) {
            dslCommonComponent.orderByPath(queryBase, qBill.billStatus, direction);
        }
        if (propertyName.equals("taxExcludedAmount")) {
            dslCommonComponent.orderByPath(queryBase, qBill.taxExcludedAmount, direction);
        }
        if (propertyName.equals("taxIncludedAmount")) {
            dslCommonComponent.orderByPath(queryBase, qBill.taxIncludedAmount, direction);
        }
        if (propertyName.equals("billYm")) {
            dslCommonComponent.orderByPath(queryBase, qBill.billYm, direction);
        }
    }

    @Override
    public QueryBase buildCriteriaPageableSearch(Map<String, Object> conditionMap) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(chargeEntityManager);
        JPAQuery<Bill> jpaQuery = queryFactory
                .selectFrom(qBill)
                .join(qCompany)
                .on(qCompany.companyId.longValue().eq(qBill.companyId));
        QueryBase<? extends JPAQuery> qb = jpaQuery;
        searchConditionModifier(qb, conditionMap);
        return qb;
    }

    @Override
    public void searchConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap) {
        try {
            if (conditionMap.containsKey("billStatus")) {
                BillStatus billStatus = BillStatus.valueOf(String.valueOf(conditionMap.get("billStatus")));
                queryBase.where(qBill.billStatus.eq(billStatus));
            }
            if (conditionMap.containsKey("companyId")) {
                Long companyId = Long.valueOf(String.valueOf(conditionMap.get("companyId")));
                queryBase.where(qBill.companyId.eq(companyId));
            }
            if (conditionMap.containsKey("outYearMonth")) {
                queryBase.where(qBill.billYm.eq(String.valueOf(conditionMap.get("outYearMonth"))));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
