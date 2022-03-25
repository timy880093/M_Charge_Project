package com.gateweb.orm.charge.repository.impl;

import com.gateweb.charge.dsl.component.BillingItemDslComponent;
import com.gateweb.charge.dsl.component.ProductDslComponent;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.QBillingItem;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.BillingItemRepositoryCustom;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository("billingItemRepositoryCustom")
public class BillingItemRepositoryCustomImpl implements BillingItemRepositoryCustom {
    @Autowired
    BillingItemRepository billingItemRepository;

    @Autowired
    @Qualifier("chargeEntityManager")
    EntityManager chargeEntityManager;

    @Autowired
    BillingItemDslComponent billingItemDslService;
    @Autowired
    ProductDslComponent productDslService;

    QBillingItem billingItem = QBillingItem.billingItem;

    @Override
    public List<BillingItem> searchByVo(BillingItem billingItemVo) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        billingItemPredicateModifier(booleanBuilder, billingItemVo);
        return executePredicate(booleanBuilder);
    }

    @Override
    public void billingItemPredicateModifier(BooleanBuilder booleanBuilder, BillingItem vo) {
        if (vo.getBillingItemType() != null) {
            booleanBuilder.and(billingItem.billingItemType.eq(vo.getBillingItemType()));
        }
    }

    @Override
    public Page<BillingItem> executePredicate(BooleanBuilder booleanBuilder, Pageable pageable) {
        return billingItemRepository.findAll(booleanBuilder.getValue(), pageable);
    }

    @Override
    public List<BillingItem> executePredicate(BooleanBuilder booleanBuilder) {
        return Lists.newArrayList(billingItemRepository.findAll(booleanBuilder.getValue()));
    }

    /**
     * 用dsl進行太長in條件查詢會有問題。
     *
     * @param billingItemVo
     * @param billingSourceIdList
     * @param pageable
     * @return
     */
    @Override
    public List<BillingItem> searchByVoAndSourceListAndPageable(BillingItem billingItemVo, List<Long> billingSourceIdList, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        billingItemPredicateModifier(booleanBuilder, billingItemVo);
        List<BillingItem> resultList = billingItemRepository.findAll(booleanBuilder.getValue(), pageable).getContent();
        return resultList;
    }

}
