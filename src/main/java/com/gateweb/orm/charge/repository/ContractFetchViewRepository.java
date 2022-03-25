package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.ContractFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ContractFetchViewRepository extends JpaRepository<ContractFetchView, Long> {
    List<ContractFetchView> findByExpirationDateAfter(Timestamp timestamp);
}
