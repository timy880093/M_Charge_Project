package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, QuerydslPredicateExecutor<Bill> {

    List<Bill> findByCreateDateGreaterThanAndCreateDateLessThan(Timestamp startTimestamp, Timestamp endTimestamp);

    List<Bill> findByCompanyId(Long companyId);

    List<Bill> findByBillYm(String ym);

    Collection<Bill> findByBillRemark(String billRemark);

    @Query(
            value = "select distinct bill_ym from bill where bill_ym is not null order by bill_ym desc limit :number ;"
            , nativeQuery = true
    )
    Collection<String> findBillYmByTopLimit(@Param("number") Integer num);

    @Query(
            value = "select sum(bl.tax_included_amount) from bill bl " +
                    "where bill_ym = :billYm " +
                    "and bill_status = :billStatus ;"
            , nativeQuery = true
    )
    Optional<BigDecimal> findSumByBillYm(@Param("billYm") String billYm, @Param("billStatus") String billStatus);

    @Query(
            value = " select * from bill bl " +
                    " join billing_item bi on(bl.bill_id=bi.bill_id) " +
                    " where bi.deduct_id is not null and deduct_id = :deductId ;"
            , nativeQuery = true
    )
    Optional<Bill> findBillByDeductId(@Param("deductId") Long deductId);

}

