package com.gateweb.charge.service.dataGateway;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.dsl.component.ContractDslComponent;
import com.gateweb.charge.dsl.component.DslCommonComponent;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.view.ContractFetchView;
import com.gateweb.orm.charge.repository.ContractFetchViewRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.querydsl.core.support.QueryBase;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SearchService層負責了不同回傳結果的處理任務
 * 包括list、map、總筆數、回傳更為複雜的物件都在此處理
 */
@Component
public class ContractDataGateway {

    private ContractDslComponent contractDslServiceImpl;
    private DslCommonComponent dslCommonService;
    private ContractFetchViewRepository contractFetchViewRepository;
    private ContractRepository contractRepository;

    public ContractDataGateway(ContractDslComponent contractDslServiceImpl, DslCommonComponent dslCommonService, ContractFetchViewRepository contractFetchViewRepository, ContractRepository contractRepository) {
        this.contractDslServiceImpl = contractDslServiceImpl;
        this.dslCommonService = dslCommonService;
        this.contractFetchViewRepository = contractFetchViewRepository;
        this.contractRepository = contractRepository;
    }

    public Optional<Contract> findByContractId(Long contractId) {
        return contractRepository.findById(contractId);
    }

    public Optional<Contract> findByContractIdAndStatus(Long contractId, ContractStatus contractStatus) {
        return contractRepository.findByContractIdAndStatus(contractId, contractStatus);
    }

    public List<Contract> findByCompanyIdAndContractStatusIn(Long companyId, ContractStatus... statusList) {
        List<Contract> contractList = new ArrayList<>(
                contractRepository.findByCompanyIdAndStatusIsIn(companyId, Arrays.asList(statusList))
        );
        return contractList;
    }

    public Set<Contract> findByCompanyIdAndContractStatusAndExpirationDateBetween(
            Long companyId, ContractStatus contractStatus, CustomInterval customInterval) {
        return contractRepository.findByStatusAndExpirationDateBetweenAndChargeByRemainingCountIsFalse(
                customInterval.getStartLocalDateTime()
                , customInterval.getEndLocalDateTime()
                , contractStatus.name()
        ).stream().filter(contract -> {
            return companyId.equals(contract.getCompanyId());
        }).collect(Collectors.toSet());
    }

    public Set<Contract> findByContractStatusAndExpirationDateBetween(
            ContractStatus contractStatus, CustomInterval customInterval) {
        return contractRepository.findByStatusAndExpirationDateBetweenAndChargeByRemainingCountIsFalse(
                customInterval.getStartLocalDateTime()
                , customInterval.getEndLocalDateTime()
                , contractStatus.name()
        );
    }

    public Set<Contract> findByContractStatusAndIntervalOverlaps(ContractStatus contractStatus, CustomInterval customInterval) {
        return contractRepository.findByStatusAndIntervalOverlaps(
                customInterval.getStartLocalDateTime()
                , customInterval.getEndLocalDateTime()
                , contractStatus.name()
        );
    }

    public Set<Contract> findByStatusNotDisableAndCompanyIdAndIntervalOverlaps(Long companyId, CustomInterval interval) {
        return contractRepository.findByCompanyIdAndIntervalOverlaps(
                interval.getStartLocalDateTime()
                , interval.getEndLocalDateTime()
                , companyId
        ).stream().filter(contract -> {
            return ContractStatus.D != contract.getStatus();
        }).collect(Collectors.toSet());
    }

    public List<Contract> searchListByPageInfo(PageInfo pageInfo) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = contractDslServiceImpl.buildCriteriaPageableSearch(pageInfo.getCondition());
        List<Contract> contractList = dslCommonService.fetchPageData(contractDslServiceImpl, queryBase, pageInfo);
        return contractList;
    }

    public Map<String, Object> searchListByCondition(Map<String, Object> conditionMap) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = contractDslServiceImpl.buildCriteriaPageableSearch(conditionMap);
        Map<String, Object> contractResultMap = dslCommonService.fetchData(queryBase);
        return contractResultMap;
    }

    public List<ContractFetchView> searchContractViewListByPageInfo(PageInfo pageInfo) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = contractDslServiceImpl.buildCriteriaPageableSearch(pageInfo.getCondition());
        List<Contract> contractList = dslCommonService.fetchPageData(contractDslServiceImpl, queryBase, pageInfo);
        List<ContractFetchView> contractFetchViewList = new ArrayList<>();
        contractList.stream().forEach(billingItem -> {
            Optional<ContractFetchView> billingItemFetchViewOptional = contractFetchViewRepository.findById(billingItem.getContractId());
            if (billingItemFetchViewOptional.isPresent()) {
                contractFetchViewList.add(billingItemFetchViewOptional.get());
            }
        });
        return contractFetchViewList;
    }

    public Collection<Contract> findCollectionByCondition(Map<String, Object> conditionMap) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = contractDslServiceImpl.buildCriteriaPageableSearch(conditionMap);
        Map<String, Object> contractResultMap = dslCommonService.fetchData(queryBase);
        return (Collection<Contract>) contractResultMap.get("data");
    }


}
