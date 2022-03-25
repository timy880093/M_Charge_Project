package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.BillFetchView;
import com.gateweb.charge.frontEndIntegration.bean.BillSearchCondition;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BillFetchViewRepositoryCustom {

    List<BillFetchView> searchBySearchCondition(BillSearchCondition billSearchCondition);
}
