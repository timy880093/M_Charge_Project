package com.gateweb.charge.service.dataGateway;

import com.gateweb.charge.dsl.component.BillingItemDslComponent;
import com.gateweb.charge.dsl.component.DslCommonComponent;
import com.gateweb.charge.frontEndIntegration.bean.BillingItemSearchCondition;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import com.gateweb.orm.charge.repository.BillingItemFetchViewRepository;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BillingItemDataGateway {
    protected final Logger logger = LogManager.getLogger(getClass());
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();
    @Autowired
    DslCommonComponent dslCommonService;
    @Autowired
    BillingItemDslComponent billingItemDslServiceImpl;
    @Autowired
    BillingItemFetchViewRepository billingItemFetchViewRepository;

    public List<BillingItemFetchView> searchBillingItemFetchViewListByPageInfo(PageInfo pageInfo) {
        //依照客製的搜尋條件進行過濾
        QueryBase<? extends JPAQuery> queryBase = billingItemDslServiceImpl.buildCriteriaPageableSearch(pageInfo.getCondition());
        List<BillingItem> billingItemList = dslCommonService.fetchPageData(billingItemDslServiceImpl, queryBase, pageInfo);
        List<BillingItemFetchView> billingItemFetchViewList = new ArrayList<>();
        billingItemList.stream().forEach(billingItem -> {
            Optional<BillingItemFetchView> billingItemFetchViewOptional = billingItemFetchViewRepository.findById(billingItem.getBillingItemId());
            if (billingItemFetchViewOptional.isPresent()) {
                billingItemFetchViewList.add(billingItemFetchViewOptional.get());
            }
        });
        return billingItemFetchViewList;
    }

    public List<BillingItem> searchListByConditionObject(BillingItemSearchCondition billingItemSearchCondition) {
        List<BillingItem> resultList = new ArrayList<>();
        try {
            HashMap<String, Object> conditionMap = (HashMap<String, Object>) beanConverterUtils.beanToMap(billingItemSearchCondition);
            return searchListByConditionMap(conditionMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return resultList;
    }

    public List<BillingItem> searchListByConditionMap(Map<String, Object> conditionMap) {
        QueryBase<? extends JPAQuery> queryBase = billingItemDslServiceImpl.buildCriteriaPageableSearch(conditionMap);
        Map<String, Object> billingItemResultMap = dslCommonService.fetchData(queryBase);
        return (List<BillingItem>) billingItemResultMap.get("data");
    }

    public List<BillingItemFetchView> searchViewListByConditionMap(Map<String, Object> conditionMap) {
        List<BillingItemFetchView> billingItemFetchViewList = new ArrayList<>();
        QueryBase<? extends JPAQuery> queryBase = billingItemDslServiceImpl.buildCriteriaPageableSearch(conditionMap);
        Map<String, Object> billingItemResultMap = dslCommonService.fetchData(queryBase);
        List<BillingItem> billingItemList = (List<BillingItem>) billingItemResultMap.get("data");
        billingItemList.stream().forEach(billingItem -> {
            Optional<BillingItemFetchView> billingItemFetchViewOptional = billingItemFetchViewRepository.findById(billingItem.getBillingItemId());
            if (billingItemFetchViewOptional.isPresent()) {
                billingItemFetchViewList.add(billingItemFetchViewOptional.get());
            }
        });
        return billingItemFetchViewList;
    }

}
