package com.gateweb.charge.dsl.component;

import com.gateweb.charge.dsl.*;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.enumeration.DeductStatus;
import com.gateweb.charge.enumeration.DeductType;
import com.gateweb.orm.charge.entity.QDeduct;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;

@Component
public class DeductDslComponent implements SortableDslFieldRender, SortableDslDispatcher
        , DslSearchConditionRender, CriteriaPageableSearchBuilder {
    private Logger logger = LogManager.getLogger(this.getClass().getName());
    QDeduct qDeduct = QDeduct.deduct;

    @Autowired
    DslCommonComponent dslCommonService;
    @Autowired
    @Qualifier("chargeEntityManager")
    protected EntityManager chargeEntityManager;

    @Override
    public void orderByDslModifierDispatcher(QueryBase qb, PageInfo pageInfo) {
        dslCommonService.orderByDslModifier(this, qb, pageInfo);
    }

    @Override
    public void orderByDslModifierDispatcher(QueryBase qb, Map orderMap) {
        dslCommonService.orderByDslModifier(this, qb, orderMap);
    }

    @Override
    public void orderByDslModifierSwitch(QueryBase queryBase, String propertyName, String direction) {
        if (propertyName.equals("deductType")) {
            dslCommonService.orderByPath(queryBase, qDeduct.deductType, direction);
        }
        if (propertyName.equals("deductStatus")) {
            dslCommonService.orderByPath(queryBase, qDeduct.deductStatus, direction);
        }
        if (propertyName.equals("effectiveDate")) {
            dslCommonService.orderByPath(queryBase, qDeduct.effectiveDate, direction);
        }
    }

    @Override
    public QueryBase buildCriteriaPageableSearch(Map<String, Object> conditionMap) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(chargeEntityManager);
        QDeduct deduct = QDeduct.deduct;
        QueryBase qb = queryFactory.selectFrom(deduct);
        searchConditionModifier(qb, conditionMap);
        return qb;
    }

    @Override
    public void searchConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap) {
        try {
            if (conditionMap.containsKey("deductStatus")) {
                DeductStatus deductStatus = DeductStatus.valueOf(String.valueOf(conditionMap.get("deductStatus")));
                queryBase.where(qDeduct.deductStatus.eq(deductStatus));
            }
            if (conditionMap.containsKey("deductType")) {
                DeductType deductType = DeductType.valueOf(String.valueOf(conditionMap.get("deductType")));
                queryBase.where(qDeduct.deductType.eq(deductType));
            }
            if (conditionMap.containsKey("contractId")) {
                Long contractId = Long.valueOf(String.valueOf(conditionMap.get("contractId")));
                queryBase.where(qDeduct.contractId.eq(contractId));
            }
            if (conditionMap.containsKey("companyId")) {
                Long companyId = Double.valueOf(String.valueOf(conditionMap.get("companyId"))).longValue();
                queryBase.where(qDeduct.companyId.eq(companyId));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
