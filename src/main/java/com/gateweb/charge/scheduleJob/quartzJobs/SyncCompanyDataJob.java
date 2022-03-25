package com.gateweb.charge.scheduleJob.quartzJobs;

import com.gateweb.charge.scheduleJob.component.SyncCompanyDataComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class SyncCompanyDataJob implements Job {
    protected static final Logger logger = LogManager.getLogger(SyncCompanyDataComponent.class);

    @Autowired
    SyncCompanyDataComponent syncCompanyDataComponent;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        syncCompanyDataComponent.syncCompanyDataFromEinvDatabase();
    }
}
