package com.gateweb.orm.charge.repository;


import com.gateweb.orm.charge.entity.MigEventRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository("migEventRecord")
public interface MigEventRecordRepository extends JpaRepository<MigEventRecordEntity, Long>
        , QuerydslPredicateExecutor<MigEventRecordEntity>,MigEventRecordRepositoryCustom{

    Optional<MigEventRecordEntity> findById(Long eventId);

    @Query(
            value = "SELECT max(create_date) FROM mig_event_record mer ",
            nativeQuery = true)
    Optional<Timestamp> findMaximumSyncDate();
    
}
