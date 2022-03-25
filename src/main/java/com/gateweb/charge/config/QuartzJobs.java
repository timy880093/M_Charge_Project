package com.gateweb.charge.config;

import com.gateweb.charge.scheduleJob.quartzJobs.*;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzJobs {
    private static final String CRON_EVERY_MINUTE = "0 */1 * * * ?";
    private static final String CRON_EVERY_5_MINUTE = "0 */5 * * * ?";
    private static final String CRON_EVERY_10_MINUTE = "0 */10 * * * ?";
    private static final String CRON_EVERY_30_MINUTE = "0 */30 * * * ?";
    private static final String CRON_EVERY_DAY_AT_1_AM = "0 0 1 ? * * *";
    private static final String CRON_EVERY_THU_AT_1_AM = "0 0 12 ? * THU *";
    private static final String CRON_EVERY_SAT_AT_1_AM = "0 0 12 ? * SAT *";
    private static final String CRON_EVERY_ODD_MONTH_16TH_AT_1_AM = "0 0 12 16 1,3,5,7,9,11 ?";
    private static final String CRON_EVERY_WEEK_AT_SUNDAY = "0 0 0 ? * SUN";
    private static final String CRON_EVERY_MONTH_15_20 = "0 0 0 15,16,17,18,19,20 * ? ";

    @Bean(name = "contractAutomationStats")
    public JobDetailFactoryBean contractAutomationJobDetail() {
        return QuartzConfig.createJobDetail(ContractAutomationJob.class, "contractAutomation");
    }

    @Bean(name = "contractAutomationTrigger")
    public CronTriggerFactoryBean triggerContractAutomation(@Qualifier("contractAutomationStats") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_DAY_AT_1_AM, "contractAutomationTrigger");
    }

    @Bean(name = "syncUserDataStats")
    public JobDetailFactoryBean syncUserDataJobDetail() {
        return QuartzConfig.createJobDetail(SyncUserDataJob.class, "syncUserDataStats");
    }

    @Bean(name = "syncUserDataTrigger")
    public CronTriggerFactoryBean triggerSyncUserData(@Qualifier("syncUserDataStats") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_THU_AT_1_AM, "syncUserDataTrigger");
    }

    @Bean(name = "syncCompanyDataStats")
    public JobDetailFactoryBean syncCompanyJobDetail() {
        return QuartzConfig.createJobDetail(SyncCompanyDataJob.class, "syncCompanyDataStats");
    }

    @Bean(name = "syncCompanyDataTrigger")
    public CronTriggerFactoryBean triggerSyncCompanyData(@Qualifier("syncCompanyDataStats") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_SAT_AT_1_AM, "syncCompanyDataTrigger");
    }

    @Bean(name = "syncIasrDataStats")
    public JobDetailFactoryBean syncIasrJobDetail() {
        return QuartzConfig.createJobDetail(SyncIasrDataJob.class, "syncIasrDataStats");
    }

    @Bean(name = "syncIasrDataTrigger")
    public CronTriggerFactoryBean triggerSyncIasrData(@Qualifier("syncIasrDataStats") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_MONTH_15_20, "syncIasrDataTrigger");
    }

    @Bean(name = "sendNoticeDataStats")
    public JobDetailFactoryBean sendNoticeJobDetail() {
        return QuartzConfig.createJobDetail(SendNoticeJob.class, "sendNoticeDataStats");
    }

    @Bean(name = "sendNoticeTrigger")
    public CronTriggerFactoryBean sendNoticeTrigger(@Qualifier("sendNoticeDataStats") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_MINUTE, "sendNoticeTrigger");
    }

    @Bean(name = "remainingCountRecorderStats")
    public JobDetailFactoryBean remainingCountRecorderJobDetail() {
        return QuartzConfig.createJobDetail(RemainingCountMonitorJob.class, "remainingCountRecorderStats");
    }

    @Bean(name = "remainingCountRecorderTrigger")
    public CronTriggerFactoryBean remainingCountRecorderTrigger(@Qualifier("remainingCountRecorderStats") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_30_MINUTE, "remainingCountRecorderTrigger");
    }
}
