package com.gateweb.orm.einv.repository;

import com.gateweb.orm.einv.entity.MigStateRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EinvMigStateRecordRepositoryCustom extends JpaRepository<MigStateRecordEntity, Long>
        , QuerydslPredicateExecutor<MigStateRecordEntity>{



}
