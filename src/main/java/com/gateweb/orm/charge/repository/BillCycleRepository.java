/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.BillCycleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author pkliu
 * <p>
 * This class provides methods to populate DB Table of BillCycle
 */
@Repository("billCycle")
public interface BillCycleRepository extends JpaRepository<BillCycleEntity, Integer>
        , QuerydslPredicateExecutor<BillCycleEntity> {

    public BillCycleEntity findByBillId(Integer billId);

    public List<BillCycleEntity> findAll();

    public List<BillCycleEntity> findByYearMonthAndCompanyIdIs(String yearMonth, Integer companyId);

    public List<BillCycleEntity> findByYearMonthIs(String yearMonth);

    public List<BillCycleEntity> findByCashOutOverId(Integer cashOutOverId);

    /**
     * 這個查詢是因為佳佳的使用習慣上，算完後不會馬上出帳，他會留著下次。
     * 但因為billCycle根本沒有記錄是否計算過的欄位，所以只能用這三個一定會寫的欄位進行判斷。
     * 他們會在下次計算超額的時候被一併併入計算。
     *
     * @return
     */
    public List<BillCycleEntity> findByCntOverIsNotNullAndPriceOverIsNotNullAndPayOverIsNotNullAndCashOutOverIdIsNullAndCompanyIdIsAndStatusIsNot(Integer companyId, String status);

    List<BillCycleEntity> findByCashOutOverIdOrderByYearMonth(Integer cashOutOverId);

    Optional<BillCycleEntity> findByPackageIdAndYearMonth(Integer packageId, String yearMonth);

    List<BillCycleEntity> findByPackageIdAndYearMonthBefore(Integer packageId, String yearMonth);

    /**
     * 取得還未出帳的清單
     */
    @Query(value = "SELECT DISTINCT com.business_no " +
            " ,bc.* " +
            " FROM bill_cycle bc " +
            " JOIN company com ON (" +
            "    com.company_id = bc.company_id" +
            "    AND com.verify_status != 9" +
            "    )" +
            " LEFT JOIN cash_detail cd ON (cd.cash_detail_id = bc.cash_out_over_id) " +
            " LEFT JOIN cash_master cm ON (cm.cash_master_id = cd.cash_master_id) " +
            " WHERE cnt_over IS NOT NULL " +
            "  AND cnt_over != 0 " +
            "  AND price_over != 0 " +
            "  AND bc.STATUS != '2' " +
            "  AND cm.tax_inclusive_amount IS NULL ;", nativeQuery = true)
    Set<BillCycleEntity> findUnBilledBillCycleList();

    @Query(value = "SELECT distinct bc.*\n" +
            "FROM bill_cycle bc\n" +
            "JOIN company com ON (bc.company_id = com.company_id)\n" +
            "JOIN cash_detail cd ON (bc.cash_out_over_id = cd.cash_detail_id)\n" +
            "WHERE com.verify_status != 9\n" +
            "\tAND bc.STATUS != '2'\n" +
            "\tAND cd.out_ym IN ( :ym1 ,:ym2);", nativeQuery = true)
    Set<BillCycleEntity> findBilledOverageOverlapBillCycleList(@Param("ym1") String ym1, @Param("ym2") String ym2);

    @Query(value = "SELECT distinct bc.*\n" +
            "FROM bill_cycle bc\n" +
            "JOIN company com ON (bc.company_id = com.company_id)\n" +
            "JOIN cash_detail cd ON (bc.cash_out_month_id = cd.cash_detail_id)\n" +
            "JOIN package_mode pm on (pm.package_id = bc.package_id)\n" +
            "JOIN charge_mode_cycle_add cmca on (cmca.addition_id = pm.addition_id) " +
            "WHERE com.verify_status != 9\n" +
            "\tAND bc.STATUS != '2'\n" +
            "\tAND cmca.end_date < :limitDate " +
            "\tAND cd.out_ym IN ( :ym1 ,:ym2);", nativeQuery = true)
    Set<BillCycleEntity> findBilledMonthOverlapBillCycleList(@Param("ym1") String ym1, @Param("ym2") String ym2, @Param("limitDate") LocalDate limitDate);

    @Query(value = "select bc.* from cash_master cm \n" +
            "join company com on(cm.company_id = com.company_id )\n" +
            "join cash_detail cd on(cd.cash_master_id  = cm.cash_master_id )\n" +
            "join bill_cycle bc on (bc.cash_out_over_id =cd.cash_detail_id )\n" +
            "where cm.out_ym in (:outYm1,:outYm2)\n" +
            "and cash_out_over_id is NOT NULL\n" +
            "\tAND pay_over IS NOT NULL\n" +
            "\tAND cnt_over IS NOT NULL\n" +
            "\tAND cnt_over != 0\n" +
            "\tAND price_over != 0\n" +
            "\tand cash_type = 2\n" +
            "\tand cd.bill_type = 1\n" +
            "\tAND bc.STATUS != '2'\n" +
            "\tAND com.verify_status != 9\n" +
            "\torder by business_no;", nativeQuery = true)
    List<BillCycleEntity> findCauseByRenderYearlyPayOverage(@Param("outYm1") String outYm1, @Param("outYm2") String outYm2);

    @Query(value = "SELECT bc.* \n" +
            "FROM bill_cycle bc\n" +
            "JOIN company com ON (bc.company_id = com.company_id)\n" +
            "JOIN cash_detail cdo ON (bc.cash_out_over_id = cdo.cash_detail_id)\n" +
            "WHERE " +
            " cdo.out_ym in ( :outYm1,:outYm2) \n" +
            " AND com.company_id = :companyId \n", nativeQuery = true)
    List<BillCycleEntity> findEscapeOverageBillCycleList(@Param("outYm1") String outYm1, @Param("outYm2") String outYm2, @Param("companyId") Long companyId);

    @Query(value = "SELECT bc.* \n" +
            "FROM bill_cycle bc\n" +
            "JOIN company com ON (bc.company_id = com.company_id)\n" +
            "JOIN cash_detail cdm ON (bc.cash_out_month_id = cdm.cash_detail_id)\n" +
            "WHERE " +
            " cdm.out_ym in ( :outYm1,:outYm2) " +
            " AND com.company_id = :companyId ", nativeQuery = true)
    List<BillCycleEntity> findEscapeMonthBillCycleList(@Param("outYm1") String outYm1, @Param("outYm2") String outYm2, @Param("companyId") Long companyId);

    Set<BillCycleEntity> findByCompanyId(Integer companyId);

    List<BillCycleEntity> findByCashOutMonthIdOrderByYearMonth(Integer cashOutMonthId);
}

