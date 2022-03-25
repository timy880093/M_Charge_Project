package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ContractHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractHistoryRepository extends JpaRepository<ContractHistory, Long> {

}
