package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.TerminationClause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminationClauseRepository extends JpaRepository<TerminationClause, Long> {
    
}
