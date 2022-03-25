package com.gateweb.charge.scheduleJob.quartzJobs;

import com.gateweb.bridge.service.SyncIasrDataService;
import com.gateweb.charge.scheduleJob.component.SyncCompanyDataComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@DisallowConcurrentExecution
public class SyncIasrDataJob implements Job {
    protected static final Logger logger = LogManager.getLogger(SyncCompanyDataComponent.class);
    @Autowired
    SyncIasrDataService syncIasrDataServiceImpl;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("syncIasrProcess Start");

        getTargetYearMonthList().stream().forEach(yearMonth -> {
            try {
                syncIasrDataServiceImpl.regenIasrCount(yearMonth);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });

        logger.info("syncIasrProcess End");
    }

    public List<String> getTargetYearMonthList() {
        List<String> result = new ArrayList<>();
        try {
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
            result.add(prevYearMonth);
            result.add(currentYearMonth);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }
}
