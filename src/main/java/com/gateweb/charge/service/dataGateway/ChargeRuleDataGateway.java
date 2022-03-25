package com.gateweb.charge.service.dataGateway;

import com.gateweb.charge.report.bean.ChargeRuleServletView;
import com.gateweb.orm.charge.entity.view.ChargeRuleFetchView;
import com.gateweb.orm.charge.repository.ChargeRuleFetchViewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ChargeRuleDataGateway {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ChargeRuleFetchViewRepository chargeRuleFetchViewRepository;

    public List<ChargeRuleServletView> findAllChargeRuleServletView() {
        List<ChargeRuleFetchView> chargeRuleFetchViewList = chargeRuleFetchViewRepository.findAll();
        List<ChargeRuleServletView> chargeRuleServletViewList = new ArrayList<>();
        chargeRuleFetchViewList.stream().map(chargeRuleFetchView -> {
            return convert(chargeRuleFetchView);
        }).forEach(chargeRuleServletViewOpt -> {
            if (chargeRuleServletViewOpt.isPresent()) {
                chargeRuleServletViewList.add(chargeRuleServletViewOpt.get());
            }
        });
        return chargeRuleServletViewList;
    }

    public Optional<ChargeRuleServletView> findById(Long chargeRuleId) {
        Optional<ChargeRuleFetchView> chargeRuleFetchViewOpt = chargeRuleFetchViewRepository.findById(chargeRuleId);
        return chargeRuleFetchViewOpt.flatMap(chargeRuleFetchView -> {
            return convert(chargeRuleFetchView);
        });
    }

    public Optional<ChargeRuleServletView> convert(ChargeRuleFetchView chargeRuleFetchView) {
        try {
            ChargeRuleServletView view = new ChargeRuleServletView(
                    chargeRuleFetchView.getChargeRuleId()
                    , chargeRuleFetchView.getName()
                    , chargeRuleFetchView.getProductCategory().getProductCategoryId()
                    , chargeRuleFetchView.getProductCategory().getCategoryName()
                    , chargeRuleFetchView.getPaidPlan().name()
                    , chargeRuleFetchView.getPaidPlan().description
                    , chargeRuleFetchView.getChargePlan().name()
                    , chargeRuleFetchView.getChargePlan().description
                    , chargeRuleFetchView.getChargeBaseType().name()
                    , chargeRuleFetchView.getChargeBaseType().description
                    , chargeRuleFetchView.getRootGrade().getGradeId()
                    , chargeRuleFetchView.getRootGrade().getName()
                    , chargeRuleFetchView.getChargeCycleType().name()
                    , chargeRuleFetchView.getChargeCycleType().description
                    , chargeRuleFetchView.getCalculateCycleType().name()
                    , chargeRuleFetchView.getCalculateCycleType().description
                    , chargeRuleFetchView.getChargeByRemainingCount()
                    , chargeRuleFetchView.getEnabled()
                    , chargeRuleFetchView.getAccumulation()
                    , chargeRuleFetchView.getCirculation());
            return Optional.of(view);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return Optional.empty();
    }
}
