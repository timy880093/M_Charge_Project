package com.gateweb.charge.dsl.component;

import com.gateweb.charge.dsl.DslSearchConditionRender;
import com.gateweb.charge.dsl.SortableDslFieldRender;
import com.gateweb.orm.charge.entity.QChargeRule;
import com.querydsl.core.support.QueryBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChargeRuleDslComponent implements SortableDslFieldRender, DslSearchConditionRender {
    private Logger logger = LogManager.getLogger(this.getClass().getName());

    QChargeRule qChargeRule = QChargeRule.chargeRule;
    @Autowired
    DslCommonComponent dslCommonService;

    private static final String CHARGE_RULE_CATEGORY_ID = "chargeRuleCategoryId";

    @Override
    public void orderByDslModifierSwitch(QueryBase queryBase, String propertyName, String direction) {
        if (propertyName.equals(CHARGE_RULE_CATEGORY_ID)) {
            dslCommonService.orderByPath(queryBase, qChargeRule.productCategoryId, direction);
        }
    }

    @Override
    public void searchConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap) {
        try {
            if (conditionMap.containsKey(CHARGE_RULE_CATEGORY_ID)) {
                Long chargeRuleCategoryId = Long.valueOf(String.valueOf(conditionMap.get(CHARGE_RULE_CATEGORY_ID)));
                queryBase.where(qChargeRule.productCategoryId.eq(chargeRuleCategoryId));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
