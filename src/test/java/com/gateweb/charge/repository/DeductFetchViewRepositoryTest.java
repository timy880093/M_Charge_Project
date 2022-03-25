package com.gateweb.charge.repository;

import com.gateweb.charge.config.EinvDatabaseConfig;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.view.DeductFetchView;
import com.gateweb.orm.charge.entity.view.DeductHistoryFetchView;
import com.gateweb.orm.charge.entity.DeductHistory;
import com.gateweb.orm.charge.repository.DeductFetchViewRepository;
import com.gateweb.orm.charge.repository.DeductHistoryFetchViewRepository;
import com.gateweb.orm.charge.repository.DeductHistoryRepository;
import com.gateweb.orm.charge.repository.DeductRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@Import(EinvDatabaseConfig.class)
public class DeductFetchViewRepositoryTest {

    @Autowired
    DeductFetchViewRepository deductFetchViewRepository;
    @Autowired
    DeductHistoryFetchViewRepository deductHistoryFetchViewRepository;
    @Autowired
    DeductRepository deductRepository;
    @Autowired
    DeductHistoryRepository deductHistoryRepository;

    Gson gson = new Gson();

    @Test
    public void findAllDeductFetchView() {
        List<DeductFetchView> deductPurchaseFetchViewList = deductFetchViewRepository.findAll();
        deductPurchaseFetchViewList.stream().forEach(deductPurchaseFetchView -> {
            System.out.println(gson.toJson(deductPurchaseFetchView));
        });
    }

    @Test
    public void findAllDeductHistoryFetchView() {
        List<DeductHistoryFetchView> deductHistoryFetchViewList = deductHistoryFetchViewRepository.findAll();
        deductHistoryFetchViewList.stream().forEach(deductHistoryFetchView -> {
            System.out.println(gson.toJson(deductHistoryFetchView));
        });
    }

    @Test
    public void findDeductHistoryByDeductId() {
        List<DeductHistory> deductHistoryList = deductHistoryRepository.findByDeductId((long) 14);
        for (DeductHistory deductHistory : deductHistoryList) {
            Optional<DeductHistoryFetchView> deductHistoryFetchViewOptional = deductHistoryFetchViewRepository.findById(deductHistory.getDeductHistoryId());
            if (deductHistoryFetchViewOptional.isPresent()) {
                System.out.println(deductHistoryFetchViewOptional.get());
            }
        }
    }

}
