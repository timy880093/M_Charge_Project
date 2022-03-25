package com.gateweb.charge.chargePolicy.grade.service;

import com.gateweb.orm.charge.entity.RootGradeFetchView;

import java.util.List;

public interface RootGradeSearchService {

    List<RootGradeFetchView> findAllRootGradeFetchView();
}
