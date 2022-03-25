package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.DeductFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeductFetchViewRepository extends JpaRepository<DeductFetchView, Long> {
    @Query(value = "select * from deduct where deduct_source_id = ?1 ", nativeQuery = true)
    List<DeductFetchView> findByDeductSourceId(Long deductSourceId);
}
