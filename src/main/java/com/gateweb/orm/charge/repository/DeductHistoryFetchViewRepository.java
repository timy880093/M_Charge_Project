package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.DeductHistoryFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeductHistoryFetchViewRepository extends JpaRepository<DeductHistoryFetchView, Long> {
}
