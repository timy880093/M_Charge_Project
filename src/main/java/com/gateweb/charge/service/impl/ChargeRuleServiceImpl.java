package com.gateweb.charge.service.impl;

import com.gateweb.charge.component.annotated.ModifierAndCreatorUtils;
import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.exception.ItemHaveBeenOccupiedException;
import com.gateweb.charge.exception.MissingRequiredPropertiesException;
import com.gateweb.charge.mapper.ChargeRuleMapper;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.service.ChargeRuleService;
import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.gateweb.orm.charge.repository.SimpleUserViewRepository;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChargeRuleServiceImpl implements ChargeRuleService {
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    SimpleUserViewRepository simpleUserViewRepository;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    PackageRefRepository productReferenceRepository;
    @Autowired
    ChargeRuleMapper chargeRuleMapper;
    @Autowired
    ModifierAndCreatorUtils modifierAndCreatorUtils;
    @Autowired
    PackageRefRepository packageRefRepository;

    @Override
    public void saveOrUpdateChargeRuleByMap(Map<String, Object> map, CallerInfo callerInfo) {
        //根據map產生vo
        ChargeRule chargeRuleVo = beanConverterUtils.mapToBean(map, ChargeRule.class);
        Optional<ChargeRule> chargeModeOptional = Optional.empty();
        if (chargeRuleVo.getChargeRuleId() != null) {
            chargeModeOptional = chargeRuleRepository.findById(chargeRuleVo.getChargeRuleId());
        }
        if (chargeRuleVo.getCalculateCycleType() != null
                || chargeRuleVo.getChargeCycleType() != null) {
            chargeRuleVo.setChargePlan(ChargePlan.PERIODIC);
        }
        if (chargeRuleVo.getChargeByRemainingCount() == null) {
            chargeRuleVo.setChargeByRemainingCount(false);
        }
        if (chargeModeOptional.isPresent()) {
            chargeRuleMapper.updateChargeRuleFromVo(chargeRuleVo, chargeModeOptional.get());
            modifierAndCreatorUtils.signEntityWithCallerInfo(chargeModeOptional.get(), callerInfo);
        } else {
            modifierAndCreatorUtils.signEntityWithCallerInfo(chargeRuleVo, callerInfo);
            chargeRuleVo.setEnabled(true);
        }
        chargeRuleRepository.save(chargeRuleVo);
    }

    @Override
    public void transactionDeleteChargeRule(Long chargeModeId, CallerInfo callerInfo) throws MissingRequiredPropertiesException, ItemHaveBeenOccupiedException {
        Optional<ChargeRule> chargeModeOptional = chargeRuleRepository.findByChargeRuleId(chargeModeId);
        List<PackageRef> packageRefList = packageRefRepository.findByToChargeRuleId(chargeModeId);
        if (!chargeModeOptional.isPresent()) {
            throw new MissingRequiredPropertiesException();
        }
        if (!packageRefList.isEmpty()) {
            throw new ItemHaveBeenOccupiedException();
        }
        if (chargeModeOptional.isPresent()) {
            chargeRuleRepository.delete(chargeModeOptional.get());
        }
    }

}
