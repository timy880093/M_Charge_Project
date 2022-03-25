package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.NotMatchCountStatisticsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotMatchCountStatisticsViewRepository extends JpaRepository<NotMatchCountStatisticsView, Long> {

}
