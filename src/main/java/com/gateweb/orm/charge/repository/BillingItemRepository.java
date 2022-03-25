package com.gateweb.orm.charge.repository;

import com.gateweb.charge.enumeration.BillingItemType;
import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.orm.charge.entity.BillingItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillingItemRepository extends JpaRepository<BillingItem, Long>, QuerydslPredicateExecutor<BillingItem>, PagingAndSortingRepository<BillingItem, Long> {

    List<BillingItem> findByBillingItemTypeIs(BillingItemType billingItemType);

    List<BillingItem> findByBillIdIsNull();

    Optional<BillingItem> findByBillIdIsNullAndBillingItemIdIs(Long itemId);

    @Query(
            value = "SELECT max(billing_item_id) FROM billing_item bi ",
            nativeQuery = true)
    Optional<Long> findMaximumItemId();

    List<BillingItem> findByPackageRefIdInAndBillIdIsNull(List<Long> packageRefIdList, Pageable pageable);

    Collection<BillingItem> findByPackageRefId(Long packageRefId);

    List<BillingItem> findByCompanyIdAndPackageRefIdAndDeductIdIsNull(Long companyId, Long packageRefId);

    Collection<BillingItem> findByBillId(Long billId);

    @Query(value = "select * from billing_item bi\n" +
            "where bi.contract_id = ?1 and bi.mode_reference_id = ?2 \n" +
            "order by calculate_to_date desc\n" +
            "limit 1", nativeQuery = true)
    Optional<BillingItem> findTopByContractIdAndModeReferenceIdOrderByCalculateToDateDesc(Long contractId, Long modeReferenceId);

    List<BillingItem> findByCompanyIdAndPackageRefIdAndPaidPlanAndIsMemoIsFalseAndExpectedOutDateBefore(
            Long companyId, Long packageRefId, PaidPlan paidPlan, LocalDateTime expectedOutDate
    );

    List<BillingItem> findByExpectedOutDateBeforeAndBillIdIsNullAndIsMemoIsFalse(LocalDateTime expectedOutDate);

    List<BillingItem> findByExpectedOutDateBeforeAndBillIdIsNullAndIsMemoIsFalseAndPaidPlan(LocalDateTime expectedOutDate, PaidPlan paidPlan);

    @Query(value = "SELECT " +
            " bi.billing_item_id  " +
            " ,bi.company_id  " +
            " ,bi.product_category_id " +
            " ,bi.contract_id " +
            " ,bi.package_ref_id " +
            " ,bi.expected_out_date  " +
            " ,bi.calculate_from_date  " +
            " ,bi.calculate_to_date  " +
            " ,bi.count  " +
            " ,'MIGRATION' as billing_item_type  " +
            " ,bi.paid_plan  " +
            " ,bi.charge_plan  " +
            " ,bi.tax_rate  " +
            " ,bi.tax_amount " +
            " ,bi.tax_excluded_amount " +
            " ,bi.tax_included_amount " +
            " ,bi.is_memo  " +
            " ,cm2.create_date" +
            " ,bi.bill_id " +
            " ,bi.deduct_id " +
            " ,bi.product_purchase_id " +
            " ,cm2.cash_master_id as remark " +
            "FROM billing_item bi " +
            "JOIN package_ref pr ON (pr.package_ref_id = bi.package_ref_id) " +
            "JOIN charge_rule cm ON (cm.charge_rule_id = pr.to_charge_rule_id) " +
            "JOIN company com ON (bi.company_id = com.company_id) " +
            "JOIN cash_detail cd ON (com.company_id = cd.company_id) " +
            "JOIN cash_master cm2 ON (cm2.cash_master_id = cd.cash_master_id) " +
            "WHERE bi.bill_id IS NULL " +
            "\tAND cm.product_category_id = 2 " +
            "\tAND cd.out_ym = TO_CHAR(bi.expected_out_date, 'YYYYMM') " +
            "\tAND cd.STATUS = 1 " +
            "\tAND bi.is_memo = false " +
            "\tAND cm2.out_date is not null  ;"
            , nativeQuery = true)
    Collection<BillingItem> findPaidPrepaymentRentalBillingItem();

    Collection<BillingItem> findByCalculateFromDateIsAndCalculateToDateIs(
            LocalDateTime calculateFromDate
            , LocalDateTime calculateToDate
    );

    @Query(value = "select bi.* from billing_item bi\n" +
            " where company_id = :companyId " +
            " and calculate_from_date = :from " +
            " and calculate_to_date = :to ", nativeQuery = true)
    Collection<BillingItem> findByCompanyIdIsAndCalculateFromDateIsAndCalculateToDateIs(
            @Param("companyId") Long companyId
            , @Param("from") LocalDateTime calculateFromDate
            , @Param("to") LocalDateTime calculateToDate
    );

    Collection<BillingItem> findByCompanyIdAndPackageRefIdAndPaidPlanAndChargePlanAndIsMemoIsFalseAndBillIdIsNull(
            Long companyId
            , Long packageRefId
            , PaidPlan paidPlan
            , ChargePlan chargePlan
    );

    Optional<BillingItem> findByDeductId(Long deductId);

    Collection<BillingItem> findByDeductIdAndBillIdIsNull(Long deductId);

    Collection<BillingItem> findByCompanyIdAndPackageRefIdAndCalculateFromDateAndCalculateToDate(
            Long companyId
            , Long packageRefId
            , LocalDateTime from
            , LocalDateTime to
    );

    Collection<BillingItem> findByCompanyIdAndBillingItemTypeAndProductCategoryId(Long companyId, BillingItemType billingItemType, Long productCategoryId);

    Collection<BillingItem> findByContractId(Long contractId);

    Collection<BillingItem> findByContractIdAndBillIdIsNull(Long contractId);

    Collection<BillingItem> findByCalculateToDateBeforeAndBillIdIsNullAndBillingItemTypeIsAndProductCategoryIdIs(
            LocalDateTime localDateTime
            , BillingItemType billingItemType
            , Long productCategoryId
    );

    Collection<BillingItem> findByContractIdAndPaidPlan(Long contractId, PaidPlan paidPlan);

    Optional<BillingItem> findTopByPrevInvoiceRemainingIdIsNotNullAndContractId(Long contractId);
}
