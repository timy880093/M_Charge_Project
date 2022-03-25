package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.RootGradeFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RootGradeFetchViewRepository extends JpaRepository<RootGradeFetchView, Long> {

}
