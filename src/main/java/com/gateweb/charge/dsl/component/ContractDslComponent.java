package com.gateweb.charge.dsl.component;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.dsl.CriteriaPageableSearchBuilder;
import com.gateweb.charge.dsl.DslSearchConditionRender;
import com.gateweb.charge.dsl.SortableDslDispatcher;
import com.gateweb.charge.dsl.SortableDslFieldRender;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.QContract;
import com.gateweb.utils.CustomIntervalUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
public class ContractDslComponent implements SortableDslFieldRender, DslSearchConditionRender
        , SortableDslDispatcher, CriteriaPageableSearchBuilder {
    QContract contract = QContract.contract;

    @Autowired
    DslCommonComponent dslCommonService;
    @Autowired
    @Qualifier("chargeEntityManager")
    protected EntityManager chargeEntityManager;

    @Override
    public void orderByDslModifierSwitch(QueryBase queryBase, String propertyName, String direction) {
        if (propertyName.equals("contractId")) {
            dslCommonService.orderByPath(queryBase, contract.contractId, direction);
        }
        if (propertyName.equals("effectiveDate")) {
            dslCommonService.orderByPath(queryBase, contract.effectiveDate, direction);
        }
        if (propertyName.equals("expirationDate")) {
            dslCommonService.orderByPath(queryBase, contract.expirationDate, direction);
        }
        if (propertyName.equals("autoRenew")) {
            dslCommonService.orderByPath(queryBase, contract.autoRenew, direction);
        }
        if (propertyName.equals("status")) {
            dslCommonService.orderByPath(queryBase, contract.status, direction);
        }
        if (propertyName.equals("name")) {
            dslCommonService.orderByPath(queryBase, contract.name, direction);
        }
        if (propertyName.equals("installationDate")) {
            dslCommonService.orderByPath(queryBase, contract.installationDate, direction);
        }
    }

    @Override
    public QueryBase buildCriteriaPageableSearch(Map<String, Object> conditionMap) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(chargeEntityManager);
        JPAQuery jpaQuery = queryFactory
                .selectFrom(contract);

        QueryBase qb = jpaQuery;
        searchConditionModifier(qb, conditionMap);
        return qb;
    }

    @Override
    public void searchConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap) {
        try {
            if (conditionMap.containsKey("companyId")) {
                Long companyId = Double.valueOf(String.valueOf(conditionMap.get("companyId"))).longValue();
                queryBase.where(contract.companyId.eq(companyId));
            }
            if (conditionMap.containsKey("contractStatus")) {
                ContractStatus status = ContractStatus.valueOf(String.valueOf(conditionMap.get("contractStatus")));
                queryBase.where(contract.status.eq(status));
            }
            if (conditionMap.containsKey("displayExpire") && !String.valueOf(conditionMap.get("displayExpire")).isEmpty()) {
                Boolean displayExpire = Boolean.valueOf(String.valueOf(conditionMap.get("displayExpire")));
                if (!displayExpire) {
                    queryBase.where(contract.expirationDate.after(LocalDateTime.now()));
                }
            }
            if (conditionMap.containsKey("effectiveDateFrom")) {
                String effectiveDateStr = String.valueOf(conditionMap.get("effectiveDateFrom"));
                if (!effectiveDateStr.isEmpty()) {
                    Optional<LocalDate> effectiveDateFromOpt = LocalDateTimeUtils.parseLocalDateFromString(
                            effectiveDateStr
                            , "yyyy/MM/dd"
                    );
                    if (effectiveDateFromOpt.isPresent()) {
                        queryBase.where(
                                contract.effectiveDate.after(effectiveDateFromOpt.get().atStartOfDay().minusSeconds(1))
                        );
                    }
                }
            }
            if (conditionMap.containsKey("effectiveDateTo")) {
                String effectiveDateStr = String.valueOf(conditionMap.get("effectiveDateTo"));
                if (!effectiveDateStr.isEmpty()) {
                    Optional<LocalDate> effectiveDateToOpt = LocalDateTimeUtils.parseLocalDateFromString(
                            effectiveDateStr
                            , "yyyy/MM/dd"
                    );
                    if (effectiveDateToOpt.isPresent()) {
                        queryBase.where(
                                contract.effectiveDate.before(effectiveDateToOpt.get().plusDays(1).atStartOfDay())
                        );
                    }
                }
            }
            if (conditionMap.containsKey("expirationYM")) {
                String expirationYm = String.valueOf(conditionMap.get("expirationYM"));
                if (!expirationYm.isEmpty()) {
                    CustomInterval yearMonthInterval = CustomIntervalUtils.generateCustomIntervalByYearMonth(expirationYm);
                    queryBase.where(
                            contract.expirationDate.between(
                                    yearMonthInterval.getStartLocalDateTime()
                                    , yearMonthInterval.getEndLocalDateTime()
                            )
                    );
                }
            }
            if (conditionMap.containsKey("almostExpireYM")) {
                String almostExpireYm = String.valueOf(conditionMap.get("almostExpireYM"));
                if (!almostExpireYm.isEmpty()) {
                    CustomInterval yearMonthInterval = CustomIntervalUtils.generateCustomIntervalByYearMonth(almostExpireYm);
                    //logic:因快到期是以出帳方向做為判定，也隔天出帳為原則，會在8/31出帳到9月，以及9/30出帳到10月，所以實際上應該往前移動一天
                    yearMonthInterval = new CustomInterval(
                            yearMonthInterval.getStartLocalDateTime().minusDays(1)
                            , yearMonthInterval.getEndLocalDateTime().minusDays(1)
                    );
                    queryBase.where(
                            contract.expirationDate.between(
                                    yearMonthInterval.getStartLocalDateTime()
                                    , yearMonthInterval.getEndLocalDateTime()
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
