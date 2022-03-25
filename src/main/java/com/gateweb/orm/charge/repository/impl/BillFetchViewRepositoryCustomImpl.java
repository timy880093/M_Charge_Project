package com.gateweb.orm.charge.repository.impl;

import com.gateweb.charge.frontEndIntegration.bean.BillSearchCondition;
import com.gateweb.orm.charge.entity.QBill;
import com.gateweb.orm.charge.entity.view.BillFetchView;
import com.gateweb.orm.charge.repository.BillFetchViewRepository;
import com.gateweb.orm.charge.repository.BillFetchViewRepositoryCustom;
import com.gateweb.orm.charge.repository.BillRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("billFetchViewRepositoryCustom")
public class BillFetchViewRepositoryCustomImpl implements BillFetchViewRepositoryCustom {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    BillRepository billRepository;
    @Autowired
    BillFetchViewRepository billFetchViewRepository;

    @Override
    public List<BillFetchView> searchBySearchCondition(BillSearchCondition billSearchCondition) {
        logger.debug("BillFetchViewRepositoryCustomImpl searchWithCondition vo: {}", billSearchCondition);
        BooleanBuilder builder = new BooleanBuilder();
        //用QBill找，再用fetchView找。
        QBill bill = QBill.bill;
        if (billSearchCondition.getCompanyId() != null) {
            builder.and(bill.companyId.eq(billSearchCondition.getCompanyId())); //java.lang.Integer
        }
        if (billSearchCondition.getBillStatus() != null) {
            builder.and(bill.billStatus.eq(billSearchCondition.getBillStatus()));
        }
        List<BillFetchView> resultList = new ArrayList<>();
        Lists.newArrayList(billRepository.findAll(builder.getValue())).stream().forEach(billResult -> {
            Optional<BillFetchView> contractFetchViewOptional = billFetchViewRepository.findById(billResult.getBillId());
            if (contractFetchViewOptional.isPresent()) {
                resultList.add(contractFetchViewOptional.get());
            }
        });
        return resultList;
    }

}
