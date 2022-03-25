package com.gateweb.charge.dsl.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.constant.CompanyConstant;
import com.gateweb.charge.dsl.CriteriaPageableSearchBuilder;
import com.gateweb.charge.dsl.DslSearchConditionRender;
import com.gateweb.charge.dsl.SortableDslDispatcher;
import com.gateweb.charge.dsl.SortableDslFieldRender;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.utils.CustomIntervalUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BillingItemDslComponent implements SortableDslFieldRender, DslSearchConditionRender
        , SortableDslDispatcher, CriteriaPageableSearchBuilder {
    private QBillingItem qBillingItem = QBillingItem.billingItem;
    private QPackageRef qPackageRef = QPackageRef.packageRef;
    private QChargeRule qChargeRule = QChargeRule.chargeRule;
    private QProductCategory qProductCategory = QProductCategory.productCategory;

    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    DslCommonComponent dslCommonService;
    @Autowired
    ChargeRuleDslComponent chargeRuleDslService;
    @Autowired
    ProductCategoryDslComponent productCategoryDslServiceImpl;
    @Autowired
    @Qualifier("chargeEntityManager")
    protected EntityManager chargeEntityManager;

    @Override
    public QueryBase buildCriteriaPageableSearch(Map<String, Object> conditionMap) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(chargeEntityManager);
        JPAQuery<BillingItem> jpaQuery = queryFactory
                .selectFrom(qBillingItem)
                .leftJoin(qPackageRef)
                .on(qPackageRef.packageRefId.eq(qBillingItem.packageRefId))
                .leftJoin(qChargeRule)
                .on(qChargeRule.chargeRuleId.eq(qPackageRef.toChargeRuleId))
                .leftJoin(qProductCategory)
                .on(qProductCategory.productCategoryId.eq(qChargeRule.productCategoryId));

        QueryBase<? extends JPAQuery> qb = jpaQuery;
        searchConditionModifier(qb, conditionMap);
        privateConditionModifier(qb, conditionMap);
        productCategoryDslServiceImpl.searchConditionModifier(qb, conditionMap);
        return qb;
    }

    public void privateConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap) {
        if (conditionMap.containsKey("includeMemo")) {
            Optional<Boolean> includeMemoOptional = Optional.of(Boolean.valueOf(String.valueOf(conditionMap.get("includeMemo"))));
            if (!includeMemoOptional.orElse(false)) {
                queryBase.where(qBillingItem.isMemo.isFalse());
            }
        } else {
            queryBase.where(qBillingItem.isMemo.isFalse());
        }
        if (conditionMap.containsKey("includeBilledItem")) {
            Optional<Boolean> includeBilledItemOptional = Optional.of(Boolean.valueOf(String.valueOf(conditionMap.get("includeBilledItem"))));
            if (!includeBilledItemOptional.orElse(false)) {
                queryBase.where(qBillingItem.billId.isNull());
            }
        } else {
            queryBase.where(qBillingItem.billId.isNull());
        }
    }

    @Override
    public void searchConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap) {
        try {
            if (conditionMap.containsKey(CompanyConstant.COMPANY_ID)) {
                Long companyId = Double.valueOf(String.valueOf(conditionMap.get(CompanyConstant.COMPANY_ID))).longValue();
                queryBase.where(qBillingItem.companyId.eq(companyId));
            }
            if (conditionMap.containsKey("calYearMonth")) {
                String yearMonthStr = (String) conditionMap.get("calYearMonth");
                if (!yearMonthStr.isEmpty()) {
                    CustomInterval yearMonthInterval = CustomIntervalUtils.generateCustomIntervalByYearMonth(yearMonthStr);
                    queryBase.where(
                            qBillingItem.calculateFromDate.between(
                                    yearMonthInterval.getSqlStartTimestamp().toLocalDateTime()
                                    , yearMonthInterval.getSqlEndTimestamp().toLocalDateTime()
                            ).or(
                                    qBillingItem.calculateToDate.between(
                                            yearMonthInterval.getSqlStartTimestamp().toLocalDateTime()
                                            , yearMonthInterval.getSqlEndTimestamp().toLocalDateTime()
                                    )
                            )
                    );
                }
            }
            if (conditionMap.containsKey("expectedOutYearMonth")) {
                String expectedOutYearMonthStr = (String) conditionMap.get("expectedOutYearMonth");
                if (!expectedOutYearMonthStr.isEmpty()) {
                    CustomInterval expectedOutYearMonthInterval = CustomIntervalUtils.generateCustomIntervalByYearMonth(expectedOutYearMonthStr);
                    queryBase.where(
                            qBillingItem.expectedOutDate.between(
                                    expectedOutYearMonthInterval.getStartLocalDateTime()
                                    , expectedOutYearMonthInterval.getEndLocalDateTime()
                            )
                    );
                }
            }
            if (conditionMap.containsKey("expectedOutDateBefore")) {
                Optional<LocalDateTime> expectedOutDateBeforeOptional = Optional.ofNullable(beanConverterUtils.typeProcessor(LocalDateTime.class, conditionMap.get("expectedOutDateBefore")));
                if (expectedOutDateBeforeOptional.isPresent()) {
                    queryBase.where(
                            qBillingItem.expectedOutDate.before(
                                    LocalDateTime.from(expectedOutDateBeforeOptional.get())
                            )
                    );
                }
            }
            if (conditionMap.containsKey("gwBillingRuleYm")) {
                String yearMonthStr = (String) conditionMap.get("gwBillingRuleYm");
                if (!yearMonthStr.isEmpty()) {
                    CustomInterval yearMonthInterval = CustomIntervalUtils.generateCustomIntervalByYearMonth(yearMonthStr);
                    LocalDateTime rentalLimitDate = yearMonthInterval.getStartLocalDateTime().withDayOfMonth(1).plusMonths(1).minusSeconds(1);
                    //因為現在把每一種費用的出帳日期都確定了，因此只要直接撈日期就可以了
                    //確保每一個費用產生的預期出帳日都正確就不會有問題
                    queryBase.where(
                            qBillingItem.expectedOutDate.before(rentalLimitDate).or(qBillingItem.expectedOutDate.eq(rentalLimitDate))
                    );
                }
            }
            if (conditionMap.containsKey("billingItemIdList")) {
                Collection<Long> billingItemIdLongCollection = getBillingItemIdListFromParameterMap(conditionMap);
                billingItemIdLongCollection.stream().forEach(billingItemId -> {
                    queryBase.where(qBillingItem.billingItemId.in(billingItemIdLongCollection));
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Collection<Long> getBillingItemIdListFromParameterMap(Map parameterMap) {
        List<String> billingItemIdStrList = Arrays.asList(
                String.valueOf(parameterMap.get("billingItemIdList")).split(",")
        );
        billingItemIdStrList = billingItemIdStrList.stream().filter(billingItemId -> {
            return StringUtils.isNotEmpty(billingItemId);
        }).collect(Collectors.toList());
        return billingItemIdStrList.stream().mapToLong(str -> {
            return Long.valueOf(str);
        }).boxed().collect(Collectors.toList());
    }

    @Override
    public void orderByDslModifierSwitch(QueryBase queryBase, String propertyName, String direction) {
        if (propertyName.equals("billingItemId")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.billingItemId, direction);
        }
        if (propertyName.equals("calculateFromDate")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.calculateFromDate, direction);
        }
        if (propertyName.equals("calculateToDate")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.calculateToDate, direction);
        }
        if (propertyName.equals("createDate")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.createDate, direction);
        }
        if (propertyName.equals("expectedOutDate")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.expectedOutDate, direction);
        }
        if (propertyName.equals("count")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.count, direction);
        }
        if (propertyName.equals("productType")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.billingItemType, direction);
        }
        if (propertyName.equals("chargePlan")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.chargePlan, direction);
        }
        if (propertyName.equals("paidPlan")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.paidPlan, direction);
        }
        if (propertyName.equals("companyId")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.companyId, direction);
        }
        if (propertyName.equals("packageRefId")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.packageRefId, direction);
        }
        //限制投射對象為billingItem
        if (((JPAQuery) queryBase).getMetadata().getProjection().equals(qBillingItem)
                && propertyName.equals("taxExcludedAmount")) {
            dslCommonService.orderByPath(queryBase, qBillingItem.taxExcludedAmount, direction);

        }
    }

    @Override
    public void orderByDslModifierDispatcher(QueryBase qb, PageInfo pageInfo) {
        dslCommonService.orderByDslModifier(this, qb, pageInfo);
        dslCommonService.orderByDslModifier(chargeRuleDslService, qb, pageInfo);
    }

    @Override
    public void orderByDslModifierDispatcher(QueryBase qb, Map orderMap) {
        dslCommonService.orderByDslModifier(this, qb, orderMap);
        dslCommonService.orderByDslModifier(chargeRuleDslService, qb, orderMap);
    }
}
