package com.gateweb.orm.charge.repository;

import com.gateweb.charge.enumeration.NoticeStatus;
import com.gateweb.orm.charge.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Collection<Notice> findByNoticeStatusIsNot(NoticeStatus noticeStatus);
}
