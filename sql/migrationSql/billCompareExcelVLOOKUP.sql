SELECT CONCAT (
		com.business_no
		,'_'
		,cm.out_ym
		)
	,cm.tax_amount
	,cm.no_tax_inclusive_amount AS tax_excluded_amount
FROM cash_master cm
JOIN company com ON (cm.company_id = com.company_id)
JOIN contract con ON (con.company_id = cm.company_id)
WHERE tax_amount != 0
	AND cm.out_ym >= TO_CHAR(con.effective_date, 'YYYYMM')
	AND con.STATUS = 'E'
ORDER BY com.business_no
	,cm.out_ym;

SELECT CONCAT (
		com.business_no
		,'_'
		,TO_CHAR(bl.expected_entry_date, 'YYYYMM')
		)
	,bl.business_tax_amount
	,bl.tax_excluded_amount
FROM bill bl
JOIN company com ON (bl.company_id = com.company_id)
JOIN contract con ON (con.company_id = bl.company_id)
WHERE tax_excluded_amount != 0
	AND con.STATUS = 'E'
ORDER BY com.business_no;
