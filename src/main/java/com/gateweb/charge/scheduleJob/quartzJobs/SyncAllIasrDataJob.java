package com.gateweb.charge.scheduleJob.quartzJobs;

import com.gateweb.charge.chargeSource.service.SyncIasrDataService;
import com.gateweb.charge.scheduleJob.component.ScheduleJobUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class SyncAllIasrDataJob implements Job {
    protected static final Logger logger = LogManager.getLogger(SyncAllIasrDataJob.class);
    @Autowired
    SyncIasrDataService syncIasrDataServiceImpl;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("syncAllIasrProcess Start");

        ScheduleJobUtils.getPreviousAndCurrentYearMonthStrList().stream().forEach(yearMonth -> {
            try {
                syncIasrDataServiceImpl.regenIasrCountByInvoiceDateAndYm(yearMonth);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });

        logger.info("syncAllIasrProcess End");
    }

}
