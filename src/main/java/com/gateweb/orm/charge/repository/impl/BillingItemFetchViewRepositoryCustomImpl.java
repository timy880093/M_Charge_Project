package com.gateweb.orm.charge.repository.impl;

import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import com.gateweb.orm.charge.repository.BillingItemFetchViewRepository;
import com.gateweb.orm.charge.repository.BillingItemFetchViewRepositoryCustom;
import com.gateweb.orm.charge.repository.BillingItemRepositoryCustom;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository("BillingItemFetchViewRepositoryCustom")
public class BillingItemFetchViewRepositoryCustomImpl implements BillingItemFetchViewRepositoryCustom {
    protected final Logger logger = LogManager.getLogger(getClass());
    Gson gson = new Gson();

    @Autowired
    BillingItemFetchViewRepository billingItemFetchViewRepository;
    @Autowired
    BillingItemRepositoryCustom billingItemRepositoryCustom;

    @Override
    public List<BillingItemFetchView> searchBySearchCondition(HashMap<String, String> parameterMap) {
        logger.debug("BillingItemFetchViewRepositoryCustomImpl searchWithCondition vo: {}", parameterMap);
        List<BillingItemFetchView> resultList;

        Optional<String> companyIdOptional = Optional.ofNullable(parameterMap.get("companyId"));
        Optional<String> productCategoryIdOptional = Optional.ofNullable(parameterMap.get("productCategoryId"));
        if (companyIdOptional.isPresent() && productCategoryIdOptional.isPresent()) {
            Long companyId = new Long(companyIdOptional.get());
            Long productCategoryId = new Long(productCategoryIdOptional.get());
            resultList = billingItemFetchViewRepository.findByCompanyIdAndProductCategoryId(companyId, productCategoryId);
        } else if (companyIdOptional.isPresent() && !productCategoryIdOptional.isPresent()) {
            Long companyId = new Long(companyIdOptional.get());
            resultList = billingItemFetchViewRepository.findByCompanyId(companyId);
        } else if (productCategoryIdOptional.isPresent() && !companyIdOptional.isPresent()) {
            Long productCategoryId = new Long(productCategoryIdOptional.get());
            resultList = billingItemFetchViewRepository.findByProductCategoryId(productCategoryId);
        } else {
            resultList = billingItemFetchViewRepository.findByBillIdIsNull();
        }
        return resultList;
    }

    /**
     * 實驗結果證明用超長的in(id), postgres會壞掉
     *
     * @param billingItemVo
     * @return
     */
    @Override
    public List<BillingItemFetchView> searchByVo(BillingItem billingItemVo) {
        List<BillingItemFetchView> resultList = new ArrayList<>();
        List<BillingItem> billingItemList = billingItemRepositoryCustom.searchByVo(billingItemVo);
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
        billingItemList.stream().forEach(billingItem -> {
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                Optional<BillingItemFetchView> billingItemFetchViewOptional = billingItemFetchViewRepository.findById(billingItem.getBillingItemId());
                if (billingItemFetchViewOptional.isPresent()) {
                    resultList.add(billingItemFetchViewOptional.get());
                }
            }));
        });
        completableFutureList.stream().forEach(completableFuture -> {
            try {
                completableFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        return resultList;
    }
}
