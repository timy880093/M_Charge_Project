package com.gateweb.charge.scheduleJob.quartzJobs;

import com.gateweb.charge.scheduleJob.component.SyncUserDataComponent;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class SyncUserDataJob implements Job {
    @Autowired
    SyncUserDataComponent syncUserDataComponent;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        syncUserDataComponent.syncUserDataFromEinvDatabase();
    }
}
