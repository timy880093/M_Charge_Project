package com.gateweb.charge.dsl.component;

import com.gateweb.charge.dsl.SortableDslDispatcher;
import com.gateweb.charge.dsl.SortableDslFieldRender;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.QCompany;
import com.querydsl.core.support.QueryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;

@Component
public class CompanyDslComponent implements SortableDslDispatcher, SortableDslFieldRender {
    QCompany qCompany = QCompany.company;

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
        if (propertyName.equals("companyId")) {
            dslCommonService.orderByPath(queryBase, qCompany.companyId, direction);
        }
        if (propertyName.equals("businessNo")) {
            dslCommonService.orderByPath(queryBase, qCompany.businessNo, direction);
        }
        if (propertyName.equals("companyName")) {
            dslCommonService.orderByPath(queryBase, qCompany.name, direction);
        }
    }
}
