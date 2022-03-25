SELECT DISTINCT CASE 
		WHEN carrier_type IS NOT NULL
			THEN 1
		WHEN carrier_type IS NULL
			THEN 0
		END AS have_carrier_type
	,CASE 
		WHEN carrier_id1 IS NOT NULL
			THEN 1
		WHEN carrier_id1 IS NULL
			OR carrier_id1 = ''
			THEN 0
		END AS carrier_id1
	,CASE 
		WHEN carrier_id2 IS NOT NULL
			THEN 1
		WHEN carrier_id2 IS NULL
			OR carrier_id2 = ''
			THEN 0
		END AS carrier_id2
	,CASE 
		WHEN random_number IS NOT NULL
			THEN 1
		WHEN random_number IS NULL
			THEN 0
		END AS have_random_number
	,CASE 
		WHEN print_mark = 'Y'
			THEN 1
		WHEN print_mark = 'N'
			THEN 0
		WHEN print_mark IS NULL
			THEN 0
		WHEN print_mark = ''
			THEN 0
		END AS print_mark
	,CASE 
		WHEN invoice_type = '07'
			THEN 1
		WHEN invoice_type != '07'
			THEN 0
		END AS invoice_type_is_07
	,CASE 
		WHEN donate_mark = '1'
			THEN 1
		WHEN donate_mark = '0'
			THEN 0
		END AS donate_mark
	,CASE 
		WHEN npoban IS NOT NULL
			AND npoban != ''
			THEN 1
		WHEN npoban IS NULL
			OR npoban = ''
			THEN 0
		END AS npoban
	,CASE 
		WHEN mig_type IS NOT NULL
			THEN mig_type
		WHEN mig_type IS NULL
			THEN 'C0401'
		END AS mig_type
	,CASE 
		WHEN carrier_type = '3J0002'
			THEN 1
		ELSE 0
		END AS carrier_type_is_3J0002
	,CASE 
		WHEN carrier_id1 = carrier_id2
			THEN 1
		ELSE 0
		END AS carrier_id1_equals_to_carrier_id2
	,CASE 
		WHEN buyer = '0000000000'
			THEN 1
		ELSE 0
		END AS buyer_is_0000000000
		, tax_type 
FROM invoice_main im
WHERE im.c_year_month = '10810'
