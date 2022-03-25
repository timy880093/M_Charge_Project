package com.gateweb.charge.service.dataGateway;

import com.gateweb.charge.dsl.component.BillDslComponent;
import com.gateweb.charge.dsl.component.DslCommonComponent;
import com.gateweb.charge.frontEndIntegration.bean.BillSearchCondition;
import com.gateweb.charge.frontEndIntegration.bean.OperationObject;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.view.BillFetchView;
import com.gateweb.orm.charge.repository.BillFetchViewRepository;
import com.gateweb.orm.charge.repository.BillFetchViewRepositoryCustom;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.querydsl.core.support.QueryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BillDataGateway {
    @Autowired
    BillDslComponent billDslServiceImpl;
    @Autowired
    DslCommonComponent dslCommonService;
    @Autowired
    BillFetchViewRepositoryCustom billFetchViewRepositoryCustom;
    @Autowired
    BillFetchViewRepository billFetchViewRepository;
    @Autowired
    BillRepository billRepository;
    BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    public List<Bill> searchListByPageInfo(PageInfo pageInfo) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = billDslServiceImpl.buildCriteriaPageableSearch(pageInfo.getCondition());
        List<Bill> billList = dslCommonService.fetchPageData(billDslServiceImpl, queryBase, pageInfo);
        return billList;
    }

    public List<Bill> searchListByCondition(Map<String, Object> conditionMap) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = billDslServiceImpl.buildCriteriaPageableSearch(conditionMap);
        Map<String, Object> billResultMap = dslCommonService.fetchData(queryBase);
        return (List<Bill>) billResultMap.get("data");
    }

    public List<BillFetchView> searchByCondition(BillSearchCondition billSearchCondition) {
        return billFetchViewRepositoryCustom.searchBySearchCondition(billSearchCondition);
    }

    public List<BillFetchView> fetchViews(List<Bill> billList) {
        List<BillFetchView> billFetchViewList = new ArrayList<>();
        billList.stream().forEach(bill -> {
            Optional<BillFetchView> billFetchViewOptional = billFetchViewRepository.findById(bill.getBillId());
            if (billFetchViewOptional.isPresent()) {
                billFetchViewList.add(billFetchViewOptional.get());
            }
        });
        return billFetchViewList;
    }

    public List<Bill> searchByIdList(List<Long> idList) {
        return idList.stream().map(billId -> {
            return billRepository.findById(billId);
        }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    public List<Bill> searchByOperationObj(OperationObject operationObject) {
        if (operationObject.getCondition().fields().hasNext()) {
            HashMap<String, Object> conditionMap = beanConverterUtils.convertJsonToMap(operationObject.getCondition().toString());
            return searchListByCondition(conditionMap);
        }
        if (!StringUtils.isEmpty(operationObject.getIdList())) {
            List<Long> idList = Arrays.asList(operationObject.getIdList().split(",")).stream().map(str -> {
                return Long.valueOf(str);
            }).collect(Collectors.toList());
            return searchByIdList(idList);
        }
        return new ArrayList<>();
    }

}
