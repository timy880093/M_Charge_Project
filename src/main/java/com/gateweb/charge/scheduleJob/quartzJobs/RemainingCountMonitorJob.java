package com.gateweb.charge.scheduleJob.quartzJobs;

import com.gateweb.bridge.service.SyncIasrDataService;
import com.gateweb.charge.contract.remainingCount.component.RemainingContractRenewExecutioner;
import com.gateweb.charge.contract.remainingCount.component.RemainingRecordUpdateByInvoiceDate;
import com.gateweb.charge.contract.remainingCount.component.RemainingRecordWriterByInvoiceDate;
import com.gateweb.charge.contract.remainingCount.component.RemainingRecordWriterByUploadDate;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.*;

@Component
@DisallowConcurrentExecution
public class RemainingCountMonitorJob implements Job {
    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    RemainingRecordWriterByUploadDate remainingCountRecordWriter;
    @Autowired
    RemainingRecordWriterByInvoiceDate remainingRecordWriterByInvoiceDate;
    @Autowired
    RemainingContractRenewExecutioner remainingContractRenewExecutioner;
    @Autowired
    RemainingRecordUpdateByInvoiceDate remainingRecordUpdateByInvoiceDate;
    @Autowired
    SyncIasrDataService syncIasrDataService;
    @Autowired
    SyncIasrDataJob syncIasrDataJob;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Set<Company> companySet = companyRepository.findBillableChargeByRemainingCountCompanyIdList();
        syncRemainingCountInvoice(companySet);
        remainingRecordUpdateByInvoiceDate.executeUpdate(companySet);
        remainingRecordWriterByInvoiceDate.executeWriter(companySet);
        remainingContractRenewExecutioner.executeRenew(companySet);
    }

    public void syncRemainingCountInvoice(Set<Company> companySet) {
        companySet.stream().forEach(company -> {
            Set<String> yearMonthList = getTargetYearMonthListByRemainingRecord(company);
            logger.info("SyncIasrDataForRemainingCountCompanyJob:" + company.getBusinessNo());
            yearMonthList.stream().forEach(yearMonth -> {
                try {
                    syncIasrDataService.regenIasrCount(company.getBusinessNo(), yearMonth);
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                }
            });
            logger.info("SyncIasrDataForRemainingCountCompanyJob:" + company.getBusinessNo() + " End");
        });
    }

    /**
     * 查詢這些公司要查詢的年月區間yyyyMM，因為invoiceDate就是yyyyMMdd，因此直接用invoiceDate切割就好了
     *
     * @param company
     * @return
     */
    public Set<String> getTargetYearMonthListByRemainingRecord(Company company) {
        List<String> yearMonthList = new ArrayList<>();
        List<InvoiceRemaining> invoiceRemainingList = invoiceRemainingRepository.findByCompanyId(company.getCompanyId().longValue());
        invoiceRemainingList.stream().forEach(invoiceRemaining -> {
            yearMonthList.add(invoiceRemaining.getInvoiceDate().substring(0, 6));
        });
        addAdditionalYearMonth(yearMonthList);
        return new HashSet<>(yearMonthList);
    }

    public void addAdditionalYearMonth(List<String> yearMonthList) {
        Collections.sort(yearMonthList, Collections.reverseOrder());
        if (!yearMonthList.isEmpty()) {
            //取得最大的年月
            String maxYearMonth = yearMonthList.get(0);
            //轉成年月
            Optional<YearMonth> yearMonthOpt = LocalDateTimeUtils.parseYearMonthFromString(maxYearMonth, "yyyyMM");
            if (yearMonthOpt.isPresent()) {
                String nextYearMonth = LocalDateTimeUtils.yearMonthToString(yearMonthOpt.get().plusMonths(1), "yyyyMM");
                yearMonthList.add(nextYearMonth);
            }
        }
    }
}
