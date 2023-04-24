package com.gateweb.charge.service;

import com.gate.web.facades.UserService;
import com.gateweb.charge.chargePolicy.cycle.ChargeCycleInstanceProvider;
import com.gateweb.charge.chargePolicy.cycle.builder.ChargeCycle;
import com.gateweb.charge.chargePolicy.cycle.service.CycleService;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.enumeration.CycleType;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.utils.CustomIntervalUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.map.MultiValueMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class BillServiceTest {

    CustomIntervalUtils customIntervalUtils = new CustomIntervalUtils();
    final ChargeCycleInstanceProvider chargeCycleInstanceProvider = new ChargeCycleInstanceProvider();

    @Autowired
    BillRepository billRepository;
    @Autowired
    SimpleUserViewRepository simpleUserViewRepository;
    @Autowired
    UserService userService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    BillingService billingService;
    @Autowired
    BillService billService;
    @Autowired
    CycleService cycleService;
    @Autowired
    CashDetailRepository cashDetailRepository;
    @Autowired
    ContractService contractService;
    @Autowired
    PackageRefRepository packageRefRepository;

    @Test
    @Deprecated
    /**
     * 公司出帳測試
     */
    public void outBillingTestByCompany() throws ParseException {
        Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(new Long(348));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        //取得日期
        CustomInterval startYearMonthInterval = customIntervalUtils.generateCustomIntervalByYearMonth("202005");
        CustomInterval endYearMonthInterval = customIntervalUtils.generateCustomIntervalByYearMonth("202006");

        //取得實際的區間
        CustomInterval totalInterval = new CustomInterval(startYearMonthInterval.getStartLocalDateTime(), endYearMonthInterval.getEndLocalDateTime());

        Optional<ChargeCycle> chargeCycleOptional = chargeCycleInstanceProvider.genGeneralChargeCycleInstance(CycleType.MONTH);

        if (chargeCycleOptional.isPresent()) {
            HashMap<Long, Company> companyHashMap = new HashMap<>();
            //根據該日期進行時間切片
            List<CustomInterval> partitionIntervalList = chargeCycleOptional.get().doPartitioning(totalInterval);
            partitionIntervalList.stream().forEach(partitionInterval -> {
                int month = partitionInterval.getStartDateTime().getMonthOfYear();
                LocalDateTime rentalLimitDate = partitionInterval.getStartLocalDateTime().withDayOfMonth(1).plusMonths(1).minusSeconds(1);
                LocalDateTime calLimitDateTime = partitionInterval.getStartLocalDateTime();
                List<BillingItem> targetBillingItemList = new ArrayList<>();
                //若為雙數月
                //月租與超額的出帳規則是不一樣的
                if (month % 2 == 0) {
                    //預期出帳日為該月月底前的所有項目
                    targetBillingItemList.addAll(billingItemRepository.findByExpectedOutDateBeforeAndBillIdIsNullAndIsMemoIsFalseAndPaidPlan(
                            rentalLimitDate
                            , PaidPlan.PRE_PAID
                    ));
                } else {
                    //預期出帳日該月第一天以前的所有項目
                    targetBillingItemList = billingItemRepository.findByExpectedOutDateBeforeAndBillIdIsNullAndIsMemoIsFalse(calLimitDateTime);
                    //預期出帳日為該月月底前的所有項目
                    targetBillingItemList.addAll(billingItemRepository.findByExpectedOutDateBeforeAndBillIdIsNullAndIsMemoIsFalseAndPaidPlan(
                            rentalLimitDate
                            , PaidPlan.PRE_PAID
                    ));
                }
                callerInfoOptional.get().setCurrentLocalDateTime(partitionInterval.getStartLocalDateTime());
                MultiValueMap resultMap = new MultiValueMap();
                targetBillingItemList.stream().forEach(billingItem -> {
                    Optional<Company> companyOptional = companyRepository.findByCompanyId(billingItem.getCompanyId().intValue());
                    if (companyOptional.isPresent()) {
                        resultMap.put(billingItem.getCompanyId(), billingItem);
                    }
                    if (!companyHashMap.containsKey(billingItem.getCompanyId())) {
                        companyHashMap.put(billingItem.getCompanyId(), companyOptional.get());
                    }
                });
                resultMap.keySet().stream().forEach(key -> {
                    Bill billVo = new Bill();
                    billVo.setBillRemark(partitionInterval.getStartLocalDateTime().format(formatter));
                    billVo.setBillRemark(billVo.getBillRemark());
                    billVo.setCompanyId(companyHashMap.get(key).getCompanyId().longValue());
                    List<BillingItem> oneCompanyBillingItemList = new ArrayList<>();
                    oneCompanyBillingItemList.addAll((List<BillingItem>) resultMap.get(key));
                    if (oneCompanyBillingItemList.size() > 0) {
                        billService.outBill(
                                billVo
                                , oneCompanyBillingItemList
                                , true
                                , callerInfoOptional.get().getUserEntity().getUserId().longValue()
                        );

                    }
                });
            });
        }
    }

}
