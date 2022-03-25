package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.BillingItem;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BillingItemRepositoryCustom {

    List<BillingItem> searchByVo(BillingItem billingItemVo);

    void billingItemPredicateModifier(BooleanBuilder booleanBuilder, BillingItem vo);

    Page<BillingItem> executePredicate(BooleanBuilder booleanBuilder, Pageable pageable);

    List<BillingItem> executePredicate(BooleanBuilder booleanBuilder);

    List<BillingItem> searchByVoAndSourceListAndPageable(BillingItem billingItemVo, List<Long> billingSourceIdList, Pageable pageable);

}
