package com.gateweb.charge.service.impl;

import com.gateweb.utils.bean.BeanConverterUtils;
import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.charge.mapper.PackageRefMapper;
import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.gateweb.charge.service.PackageRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PackageRefServiceImpl implements PackageRefService {
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    PackageRefMapper packageRefMapper;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;

    @Override
    public PackageRef mergePackageRefEntityFromMap(Map<String, Object> map, Optional<PackageRef> packageRefOptional) {
        PackageRef packageRefVo = beanConverterUtils.mapToBean(map, PackageRef.class);
        return mergePackageRefFromVo(packageRefVo, packageRefOptional);
    }

    @Override
    public PackageRef mergePackageRefFromVo(PackageRef vo, Optional<PackageRef> packageRefOptional) {
        PackageRef result = null;
        if (packageRefOptional.isPresent()) {
            packageRefMapper.updatePackageRefToVo(vo, packageRefOptional.get());
            result = packageRefOptional.get();
        } else {
            PackageRef packageRef = new PackageRef();
            packageRefMapper.createPackageRefFromVo(vo, packageRef);
            //不採用該packageRefId
            packageRef.setPackageRefId(null);
            result = packageRef;
        }
        return result;
    }

    @Override
    public Set<PackageRef> getPackageRefSet(Set<PackageRef> packageRefSet, PaidPlan paidPlan, ChargePlan chargePlan) {
        return packageRefSet.stream().filter(packageRef -> {
            if (packageRef.getToChargeRuleId() != null) {
                Optional<ChargeRule> chargeModeOptional = chargeRuleRepository.findByChargeRuleId(packageRef.getToChargeRuleId());
                if (chargeModeOptional.isPresent()
                        && chargeModeOptional.get().getPaidPlan().equals(paidPlan)
                        && chargeModeOptional.get().getChargePlan().equals(chargePlan)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }).collect(Collectors.toSet());
    }
}
