CREATE VIEW previous_service_count_statistics AS
SELECT ROW_NUMBER () OVER () as id
	,business_no
	,year_month
	,cnt::BigInt AS counter
FROM bill_cycle bc
JOIN company c ON (c.company_id = bc.company_id)
WHERE c.business_no IN (
		SELECT DISTINCT seller
		FROM invoice_amount_summary_report iasr
		)
	AND bc.STATUS = '1'
	AND cnt IS NOT NULL
