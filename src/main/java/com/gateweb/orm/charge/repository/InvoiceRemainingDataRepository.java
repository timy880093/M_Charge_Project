package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.InvoiceRemainingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRemainingDataRepository extends JpaRepository<InvoiceRemainingData, Long> {

    @Query(nativeQuery = true)
    List<InvoiceRemainingData> findInvoiceRemainingData(
            @Param("companyId") Long companyId
    );

}
