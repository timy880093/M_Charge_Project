package com.gateweb.charge.dsl.component;

import com.gateweb.charge.dsl.DslSearchConditionRender;
import com.gateweb.orm.charge.entity.QProductCategory;
import com.querydsl.core.support.QueryBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductCategoryDslComponent implements DslSearchConditionRender {
    protected final Logger logger = LogManager.getLogger(getClass());
    QProductCategory qProductCategory = QProductCategory.productCategory;

    @Override
    public void searchConditionModifier(QueryBase queryBase, Map<String, Object> conditionMap) {
        try {
            if (conditionMap.containsKey("productCategoryId")) {
                Long chargeRuleCategoryId = Double.valueOf(String.valueOf(conditionMap.get("productCategoryId"))).longValue();
                queryBase.where(qProductCategory.productCategoryId.eq(chargeRuleCategoryId));
            }
            if (conditionMap.containsKey("productCategoryName")) {
                String chargeRuleCategoryName = String.valueOf(conditionMap.get("productCategoryName"));
                queryBase.where(qProductCategory.categoryName.eq(chargeRuleCategoryName));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
