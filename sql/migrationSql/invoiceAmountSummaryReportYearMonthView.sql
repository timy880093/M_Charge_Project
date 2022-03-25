create view invoice_amount_summary_report_year_month as SELECT
    row_number() OVER () as id
    ,seller
	,(substring(invoice_date FROM 0 FOR 7)) AS yearMonth 
	,sum(amount)
	FROM invoice_amount_summary_report
GROUP BY seller,(substring(invoice_date FROM 0 FOR 7));