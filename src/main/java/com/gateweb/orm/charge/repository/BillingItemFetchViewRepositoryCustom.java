package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import com.gateweb.orm.charge.entity.BillingItem;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.HashMap;
import java.util.List;

@Deprecated
@NoRepositoryBean
public interface BillingItemFetchViewRepositoryCustom {

    List<BillingItemFetchView> searchBySearchCondition(HashMap<String, String> parameterMap);

    List<BillingItemFetchView> searchByVo(BillingItem billingItemVo);
}
