package com.gateweb.charge.service.dataGateway;

import com.gateweb.charge.dsl.component.DeductDslComponent;
import com.gateweb.charge.dsl.component.DslCommonComponent;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.service.dataGateway.DeductDataGateway;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.view.DeductFetchView;
import com.gateweb.orm.charge.repository.DeductFetchViewRepository;
import com.google.gson.Gson;
import com.querydsl.core.support.QueryBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class DeductDataGateway {
    private Logger logger = LogManager.getLogger(this.getClass().getName());
    Gson gson = new Gson();

    private DeductDslComponent deductDslServiceImpl;
    private DslCommonComponent dslCommonService;
    private DeductFetchViewRepository deductFetchViewRepository;

    public DeductDataGateway(DeductDslComponent deductDslServiceImpl, DslCommonComponent dslCommonService, DeductFetchViewRepository deductFetchViewRepository) {
        this.deductDslServiceImpl = deductDslServiceImpl;
        this.dslCommonService = dslCommonService;
        this.deductFetchViewRepository = deductFetchViewRepository;
    }

    public List<Deduct> searchListByConditionMap(Map<String, Object> conditionMap) {
        QueryBase queryBase = deductDslServiceImpl.buildCriteriaPageableSearch(conditionMap);
        Map<String, Object> billingItemResultMap = dslCommonService.fetchData(queryBase);
        return (List<Deduct>) billingItemResultMap.get("data");
    }

    public List<Deduct> searchListByPageInfo(PageInfo pageInfo) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = deductDslServiceImpl.buildCriteriaPageableSearch(pageInfo.getCondition());
        List<Deduct> deductList = dslCommonService.fetchPageData(deductDslServiceImpl, queryBase, pageInfo);
        return deductList;
    }

    public List<DeductFetchView> searchBySearchCondition(HashMap<String, Object> parameterMap) {
        logger.debug("DeductSearchServiceImpl searchWithCondition vo: " + gson.toJson(parameterMap));
        List<DeductFetchView> resultList = new ArrayList<>();

        List<Deduct> deductList = searchListByConditionMap(parameterMap);
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        deductList.stream().forEach(deduct -> {
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                Optional<DeductFetchView> deductFetchViewOptional = deductFetchViewRepository.findById(deduct.getDeductId());
                if (deductFetchViewOptional.isPresent()) {
                    resultList.add(deductFetchViewOptional.get());
                }
            }));
        });
        completableFutureList.stream().forEach(completableFuture -> {
            try {
                completableFuture.get();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                logger.error(e.getMessage());
            }
        });

        return resultList;
    }

}
