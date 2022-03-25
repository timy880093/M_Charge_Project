package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.BillFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillFetchViewRepository extends JpaRepository<BillFetchView, Long> {


}
