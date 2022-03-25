SELECT com.name
	,com.business_no
	,bl.tax_included_amount AS total
	,bl.tax_excluded_amount
FROM bill bl
JOIN company com ON (com.company_id = bl.company_id)
WHERE bill_status = 'C';

SELECT com.name
	,com.business_no
	,tax_inclusive_amount
FROM cash_master cm
JOIN company com ON (com.company_id = cm.company_id)
WHERE out_ym = '202102';
