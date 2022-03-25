CREATE TEMP TABLE bill_compare (
	bill_compare_id BIGSERIAL
	,business_no CHARACTER VARYING(30)
	,out_ym CHARACTER VARYING(30)
	,old_tax_amount NUMERIC(32, 9)
	,old_no_tax_inclusive_amount NUMERIC(32, 9)
	,new_tax_amount NUMERIC(32, 9)
	,new_no_tax_inclusive_amount NUMERIC(32, 9)
	);

INSERT INTO bill_compare (
	business_no
	,out_ym
	,old_tax_amount
	,old_no_tax_inclusive_amount
	)
SELECT DISTINCT com.business_no
	,cm.out_ym
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

INSERT INTO bill_compare (
	business_no
	,out_ym
	,new_tax_amount
	,new_no_tax_inclusive_amount
	)
SELECT DISTINCT com.business_no
	,TO_CHAR(bl.expected_entry_date, 'YYYYMM') AS out_ym
	,bl.business_tax_amount
	,bl.tax_excluded_amount
FROM bill bl
JOIN company com ON (bl.company_id = com.company_id)
JOIN contract con ON (con.company_id = bl.company_id)
LEFT JOIN bill_compare bc ON (
		bc.business_no = com.business_no
		AND bc.out_ym = TO_CHAR(bl.expected_entry_date, 'YYYYMM')
		)
WHERE tax_excluded_amount != 0
	AND con.STATUS = 'E'
	AND bc.bill_compare_id IS NULL
ORDER BY com.business_no;

UPDATE bill_compare bc
SET new_tax_amount = TEMP.business_tax_amount
	,new_no_tax_inclusive_amount = TEMP.tax_excluded_amount
FROM (
	SELECT DISTINCT com.business_no
		,TO_CHAR(bl.expected_entry_date, 'YYYYMM') AS out_ym
		,bl.business_tax_amount
		,bl.tax_excluded_amount
		,bill_compare_id
	FROM bill bl
	JOIN company com ON (bl.company_id = com.company_id)
	JOIN contract con ON (con.company_id = bl.company_id)
	LEFT JOIN bill_compare bc ON (
			bc.business_no = com.business_no
			AND bc.out_ym = TO_CHAR(bl.expected_entry_date, 'YYYYMM')
			)
	WHERE tax_excluded_amount != 0
		AND con.STATUS = 'E'
		AND bc.bill_compare_id IS NOT NULL
	ORDER BY com.business_no
	) AS TEMP
WHERE bc.business_no = TEMP.business_no
	AND bc.out_ym = TEMP.out_ym;
	
--DISCARD TEMP;
