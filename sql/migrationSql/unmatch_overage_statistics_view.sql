SELECT ROW_NUMBER() OVER () AS id
	,com.name
	,cscs.business_no
	,cscs.year_month
	,cscs.counter AS cscs_counter
	,pscs.cnt_over as pscs_cnt_over
--	,pscs.counter AS pscs_counter
	,pscs.cnt_limit as pscs_cnt_limit
	,iasrJoin.counter AS iasr_counter
FROM (
	SELECT com.business_no
		,to_char(calculate_from_date, 'YYYYMM') AS year_month
		,sum(bi.count) AS counter
	FROM billing_item bi
	JOIN billing_src bs ON (bs.billing_src_id = bi.billing_src_id)
	join package_ref pr on (bs.package_ref_id = pr.package_ref_id )
	join charge_mode cm on (pr.to_charge_mode_id = cm.charge_mode_id )
	join product_category pc on (pc.product_category_id = cm.charge_mode_category_id )
	JOIN company com ON (com.company_id = bs.company_id)
	where bi.billing_item_type  = 'SERVICE' and bi.is_memo = false and pc.product_category_id = 3
	GROUP BY com.business_no
		,bi.calculate_from_date
	) AS cscs
LEFT JOIN company com ON (com.business_no = cscs.business_no)
LEFT JOIN (
	SELECT business_no
		,year_month
		,cnt::BIGINT AS counter
		,cnt_over
		,cnt_limit as cnt_limit
		,cnt_limit-cnt as diff_cnt
	FROM bill_cycle bc
	JOIN company c ON (c.company_id = bc.company_id)
	WHERE c.business_no IN (
			SELECT DISTINCT seller
			FROM invoice_amount_summary_report iasr
			)
		AND bc.STATUS = '1'
		AND cnt IS NOT NULL
	) AS pscs ON (
		cscs.business_no = pscs.business_no
		AND cscs.year_month = pscs.year_month
		)
LEFT JOIN (
	SELECT DISTINCT seller AS business_no
		,substring(iasr.invoice_date FROM 0 FOR 7) AS year_month
		,sum(amount) AS counter
	FROM invoice_amount_summary_report iasr
	WHERE invoice_date LIKE (substring(iasr.invoice_date FROM 0 FOR 7) || '%')
	GROUP BY seller
		,substring(iasr.invoice_date FROM 0 FOR 7)
	) AS iasrJoin ON (
		iasrJoin.business_no = cscs.business_no
		AND iasrJoin.year_month = cscs.year_month
		)
where cscs.counter != (pscs.cnt_over)
order by business_no;
