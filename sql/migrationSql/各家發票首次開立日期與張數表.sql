SELECT *
FROM crosstab(
	'
	select 
	seller,invoice_date,concat(invoice_date,''_'',sum(amount))::varchar(12) as value
	from invoice_amount_summary_report iasr 
	where seller in (
		SELECT seller
		FROM (
			SELECT seller
				,count(invoice_date) AS dateCount
			FROM invoice_amount_summary_report
			GROUP BY seller
			) AS dateCountTable
		WHERE dateCount <= 5
	)
	group by seller, invoice_date 	
	'
) AS ctr(
	seller VARCHAR(8)
	, invoice_date1_count VARCHAR(12)
	, invoice_date2_count VARCHAR(12)
	, invoice_date3_count VARCHAR(12)
	, invoice_date4_count VARCHAR(12)
	, invoice_date5_count VARCHAR(12)
) order by seller;