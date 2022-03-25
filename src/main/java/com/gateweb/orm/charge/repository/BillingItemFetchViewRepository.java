package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.BillingItemFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingItemFetchViewRepository extends JpaRepository<BillingItemFetchView, Long>
        , QuerydslPredicateExecutor<BillingItemFetchView>
        , PagingAndSortingRepository<BillingItemFetchView, Long> {

    Optional<BillingItemFetchView> findByBillingItemIdAndBillIdIsNull(Long billingItemId);

    List<BillingItemFetchView> findByBillIdIsNull();

    @Query(value = "SELECT *\n" +
            "FROM billing_item bi\n" +
            "JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)\n" +
            "JOIN company com ON (bs.company_id = com.company_id)\n" +
            "JOIN product pro ON (pro.product_id = bs.product_id)\n" +
            "JOIN product_category pc ON (pro.product_category_id = pc.product_category_id)\n" +
            "WHERE com.company_id = ?1\n" +
            "\tAND pc.product_category_id = ?2\n" +
            "\t AND bi.bill_id is null ", nativeQuery = true)
    List<BillingItemFetchView> findByCompanyIdAndProductCategoryId(Long companyId, Long productCategoryId);

    @Query(value = "SELECT *\n" +
            "FROM billing_item bi\n" +
            "JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)\n" +
            "JOIN company com ON (bs.company_id = com.company_id)\n" +
            "JOIN product pro ON (pro.product_id = bs.product_id)\n" +
            "JOIN product_category pc ON (pro.product_category_id = pc.product_category_id)\n" +
            "WHERE pc.product_category_id = ?1\n" +
            "\t AND bi.bill_id is null ", nativeQuery = true)
    List<BillingItemFetchView> findByProductCategoryId(Long productCategoryId);

    @Query(value = "SELECT *\n" +
            "FROM billing_item bi\n" +
            "JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)\n" +
            "JOIN company com ON (bs.company_id = com.company_id)\n" +
            "WHERE com.company_id = ?1\n" +
            "\t AND bi.bill_id is null ", nativeQuery = true)
    List<BillingItemFetchView> findByCompanyId(Long companyId);

    @Query(value = "SELECT *\n" +
            "FROM billing_item bi\n" +
            "JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)\n" +
            "JOIN company com ON (bs.company_id = com.company_id)\n" +
            "WHERE bs.billing_src_id = ?1\n" +
            "\t AND bi.bill_id is null ", nativeQuery = true)
    List<BillingItemFetchView> findByBillingSourceIdAndBillIdIsNull(Long billingSourceId);
}
