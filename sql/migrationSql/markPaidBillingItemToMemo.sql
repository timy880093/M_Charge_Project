--不加上已付的原因是因為理論上出帳不管有沒有入帳都應該交由舊系統處理了。
SELECT DISTINCT bi.*
FROM billing_item bi
JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)
JOIN package_ref pr ON (pr.package_ref_id = bs.package_ref_id)
JOIN charge_mode cm ON (cm.charge_mode_id = pr.to_charge_mode_id)
JOIN company com ON (com.company_id = bs.company_id)
JOIN cash_detail cd ON (cd.company_id = com.company_id)
JOIN bill_cycle bc ON (bc.cash_out_over_id = cd.cash_detail_id)
WHERE is_memo = false
	AND cm.charge_mode_category_id = 3
	AND bc.year_month = TO_CHAR(bi.calculate_from_date, 'YYYYMM')
	AND bi.bill_id IS NULL
	AND cd.STATUS = 1
	--	AND cd.bill_type = 2
	AND bi.billing_item_type != 'MIGRATION'
	AND cd.cash_master_id IS NOT NULL;

SELECT DISTINCT bi.*
FROM billing_item bi
JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)
JOIN package_ref pr ON (pr.package_ref_id = bs.package_ref_id)
JOIN charge_mode cm ON (cm.charge_mode_id = pr.to_charge_mode_id)
JOIN company com ON (bs.company_id = com.company_id)
JOIN cash_detail cd ON (com.company_id = cd.company_id)
WHERE bi.bill_id IS NULL
	AND cm.charge_mode_category_id = 2
	AND cd.out_ym = TO_CHAR(bi.expected_out_date, 'YYYYMM')
	AND cd.STATUS = 1
	AND bi.is_memo = false
	AND cd.bill_type = 1
	AND bi.billing_item_type != 'MIGRATION'
	AND cd.cash_master_id IS NOT NULL;
