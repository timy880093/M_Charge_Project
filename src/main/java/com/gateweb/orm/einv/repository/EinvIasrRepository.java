package com.gateweb.orm.einv.repository;

import com.gateweb.charge.report.bean.PartsOfIasrReport;
import com.gateweb.orm.einv.entity.EinvIasrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinvIasrRepository extends JpaRepository<EinvIasrEntity, Long> {
    @Query(nativeQuery = true)
    List<PartsOfIasrReport> findNullSourceInvoiceAmountReport(
            String businessNo
            , String yearMonth
    );
}
