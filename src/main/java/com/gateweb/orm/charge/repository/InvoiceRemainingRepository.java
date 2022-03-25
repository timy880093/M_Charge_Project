package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.InvoiceRemaining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface InvoiceRemainingRepository extends JpaRepository<InvoiceRemaining, Long> {

    Optional<InvoiceRemaining> findTopInvoiceRemainingByCompanyIdOrderByInvoiceDateDesc(Long companyId);

    Optional<InvoiceRemaining> findTopInvoiceRemainingByCompanyIdAndContractIdOrderByCreateDateDesc(Long companyId, Long contractId);

    Optional<InvoiceRemaining> findTopInvoiceRemainingByCompanyIdAndContractIdAndRemainingLessThanOrderByInvoiceDate(Long companyId, Long contractId, int remaining);

    List<InvoiceRemaining> findByCompanyIdAndInvoiceDateGreaterThanOrderByInvoiceDate(Long companyId, String invoiceDate);

    Optional<InvoiceRemaining> findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDate(Long companyId, Long contractId, String invoiceDate);

    Optional<InvoiceRemaining> findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDateIsGreaterThanOrderByInvoiceDateAsc(
            Long companyId, Long contractId, String invoiceDate
    );

    Optional<InvoiceRemaining> findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDateLessThanOrderByInvoiceDateDesc(
            Long companyId, Long contractId, String invoiceDate
    );

    Optional<InvoiceRemaining> findTopInvoiceRemainingByCompanyIdAndInvoiceDateLessThanOrderByInvoiceDateDesc(
            Long companyId, String invoiceDate
    );

    Set<InvoiceRemaining> findByContractId(Long contractId);

    List<InvoiceRemaining> findByCompanyId(Long companyId);

    Optional<InvoiceRemaining> findTopInvoiceRemainingByInvoiceRemainingIdBeforeAndCompanyIdIsAndContractIdIsAndRemainingIsGreaterThan(
            Long invoiceRemainingId
            , Long companyId
            , Long contractId
            , Integer value
    );

    Optional<InvoiceRemaining> findTopByCompanyIdAndContractIdOrderByCreateDateAsc(Long companyId, Long contractId);

    @Query(value = "select * from invoice_remaining ir " +
            " where ir.invoice_date is not null " +
            " and ir.modify_date < to_date(invoice_date,'yyyyMMdd')+ interval '5 day' " +
            " and ir.company_id = :companyId " +
            " and ir.contract_id = :contractId ", nativeQuery = true)
    Collection<InvoiceRemaining> findUnstableRemainingCountByCompanyIdAndContractId(
            @Param("companyId") Long companyId
            , @Param("contractId") Long contractId
    );

    //先用三個月做為限制，避免一次查詢太多記錄
    @Query(value = "select * from invoice_remaining ir " +
            " where ir.invoice_date is not null " +
            " and CONCAT(to_char(current_timestamp + interval '3 month','yyyyMM'),'01') > invoice_date ", nativeQuery = true)
    Collection<InvoiceRemaining> findUnstableRemainingCount();

    //先用三個月做為限制，避免一次查詢太多記錄
    @Query(value = "select * from invoice_remaining ir " +
            " where ir.invoice_date is not null " +
            " and ir.company_id = :companyId" +
            " and CONCAT(to_char(current_timestamp + interval '3 month','yyyyMM'),'01') > invoice_date ", nativeQuery = true)
    Collection<InvoiceRemaining> findUnstableRemainingCountByCompanyId(@Param("companyId") Long companyId);

    Optional<InvoiceRemaining> findTopByContractIdOrderByInvoiceRemainingIdAsc(Long contractId);

    Optional<InvoiceRemaining> findTopByCompanyIdOrderByInvoiceRemainingIdAsc(Long companyId);

    Optional<InvoiceRemaining> findTopByCompanyIdAndInvoiceRemainingIdLessThanOrderByInvoiceRemainingIdDesc(Long companyId, Long invoiceRemainingId);

    Optional<InvoiceRemaining> findTopByCompanyIdAndContractIdAndInvoiceDateOrderByInvoiceDateDesc(Long companyId, Long contractId, String invoiceDate);

    Optional<InvoiceRemaining> findTopByCompanyIdAndContractIdOrderByInvoiceDate(Long companyId, Long contractId);
}
