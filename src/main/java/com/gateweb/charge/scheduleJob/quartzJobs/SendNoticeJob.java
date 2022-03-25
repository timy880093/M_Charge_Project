package com.gateweb.charge.scheduleJob.quartzJobs;

import com.gateweb.charge.enumeration.NoticeStatus;
import com.gateweb.charge.service.impl.NoticeService;
import com.gateweb.orm.charge.entity.Notice;
import com.gateweb.orm.charge.repository.NoticeRepository;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@DisallowConcurrentExecution
public class SendNoticeJob implements Job {

    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeService noticeService;

    /**
     * 查出所有Notice未寄送的，丟到service裡
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Set<Notice> noticeSet = new HashSet<>(noticeRepository.findByNoticeStatusIsNot(NoticeStatus.FINISH));
        noticeSet.stream().forEach(notice -> {
            noticeService.sendNotice(notice);
        });
    }
}
