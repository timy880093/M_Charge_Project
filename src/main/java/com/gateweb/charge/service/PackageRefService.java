package com.gateweb.charge.service;

import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.orm.charge.entity.PackageRef;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public interface PackageRefService {

    PackageRef mergePackageRefEntityFromMap(Map<String, Object> map, Optional<PackageRef> packageRefOptional);

    PackageRef mergePackageRefFromVo(PackageRef vo, Optional<PackageRef> packageRefOptional);

    Set<PackageRef> getPackageRefSet(Set<PackageRef> packageRefSet, PaidPlan paidPlan, ChargePlan chargePlan);
}
