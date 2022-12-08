package com.gateweb.charge.service;

import com.gateweb.charge.chargeSource.service.ChargeSourceService;
import com.gateweb.charge.chargeSource.service.SyncIasrDataService;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.report.bean.ChargeSourceInvoiceCountDiffReport;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class ChargeSourceServiceTest {
    Gson gson = new Gson();
    @Autowired
    ChargeSourceService chargeSourceService;
    @Autowired
    SyncIasrDataService syncIasrDataServiceImpl;

    @Test
    public void chargeSourceDiffListTest() {
        List<ChargeSourceInvoiceCountDiffReport> chargeSourceInvoiceCountDiffReportList = chargeSourceService.getChargeSourceInvoiceCountDiffReport(
                "202011", true
        );
        chargeSourceInvoiceCountDiffReportList.stream().forEach(chargeSourceInvoiceCountDiffReport -> {
            System.out.println(gson.toJson(chargeSourceInvoiceCountDiffReport));
        });
    }

    @Test
    public void syncTest() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonthDate = LocalDate.now().minusMonths(1);
        String currentMonthStr = StringUtils.leftPad(
                String.valueOf(currentDate.getMonthValue()), 2, "0"
        );
        String prevMonthStr = StringUtils.leftPad(
                String.valueOf(previousMonthDate.getMonthValue()), 2, "0"
        );
        String prevYearMonth = currentDate.getYear() + prevMonthStr;
        String currentYearMonth = currentDate.getYear() + currentMonthStr;
        try {
            syncIasrDataServiceImpl.regenContractBasedIasrCount(prevYearMonth);
            syncIasrDataServiceImpl.regenContractBasedIasrCount(currentYearMonth);
        } catch (Exception e) {
            gson.toJson(e);
        }
    }
}
