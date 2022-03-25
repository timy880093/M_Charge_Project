SELECT com.business_no
	,out_ym
	,org_price
	,tea
FROM cash_detail cd
JOIN company com ON (com.company_id = cd.company_id)
JOIN package_mode pm ON (
		pm.company_id = com.company_id
		AND cd.package_id = pm.package_id
		)
JOIN charge_mode_cycle_add cmca ON (cmca.addition_id = pm.addition_id)
LEFT JOIN (
	SELECT com.business_no
		,to_char(expected_out_date, 'YYYYMM') AS year_month
		,tax_excluded_amount AS tea
	FROM billing_item bi
	JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)
	JOIN package_ref pr ON (bs.package_ref_id = pr.package_ref_id)
	JOIN charge_mode cm ON (pr.to_charge_mode_id = cm.charge_mode_id)
	JOIN product_category pc ON (pc.product_category_id = cm.charge_mode_category_id)
	JOIN company com ON (com.company_id = bs.company_id)
	WHERE bi.is_memo = false
		AND pc.product_category_id = 2
	ORDER BY expected_out_date
	) AS nrr ON (
		nrr.business_no = com.business_no
		AND out_ym = year_month
		)
WHERE cd.bill_type = 2
	AND cash_type = 1
	AND cd.org_price > 0
	AND cmca.end_date > CURRENT_TIMESTAMP;
