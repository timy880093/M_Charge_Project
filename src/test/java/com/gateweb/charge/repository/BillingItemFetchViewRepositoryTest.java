package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.view.BillFetchView;
import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.repository.BillFetchViewRepository;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.orm.charge.repository.BillingItemFetchViewRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class BillingItemFetchViewRepositoryTest {

    @Autowired
    BillingItemFetchViewRepository billingItemFetchViewRepository;
    @Autowired
    BillFetchViewRepository billFetchViewRepository;
    @Autowired
    BillRepository billRepository;


    Gson gson = new Gson();

    @Test
    public void getAllBillingItemFetchView() {
        List<BillingItemFetchView> billingItemFetchViewList = billingItemFetchViewRepository.findAll();
        billingItemFetchViewList.stream().forEach(billingItemFetchView -> {
            System.out.println(gson.toJson(billingItemFetchView));
        });
    }

    @Test
    public void getBillFetchViewTest() {
        List<Bill> billList = billRepository.findByCompanyId(new Long(16));
        billList.stream().forEach(bill -> {
            Optional<BillFetchView> billFetchViewOptional = billFetchViewRepository.findById(bill.getBillId());
            if (billFetchViewOptional.isPresent()) {
                System.out.println(billFetchViewOptional.get());
            }
        });
    }
}
