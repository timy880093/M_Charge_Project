SELECT DISTINCT com.*
FROM company com
WHERE company_id IN (
		SELECT company_id
		FROM bill_cycle
		WHERE STATUS = '1'
			AND cnt IS NULL
			AND year_month IN (
				'202003'
				,'202004'
				)
		)
	AND verify_status != 9;
