package com.gateweb.orm.einv.repository;

import com.gateweb.orm.einv.entity.MigStateRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EinvMigStateRecordRepository extends JpaRepository<MigStateRecordEntity, Long>
        , QuerydslPredicateExecutor<MigStateRecordEntity>, EinvMigStateRecordRepositoryCustom {

    //在這裡進行的查詢都會有查不到的風險。
    List<MigStateRecordEntity> findTop100By();

    List<MigStateRecordEntity> findByCreateDateAfterAndCreateDateBefore(Timestamp from, Timestamp to);


}
