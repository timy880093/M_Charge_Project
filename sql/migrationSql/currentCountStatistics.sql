CREATE VIEW current_service_count_statistics
AS
SELECT ROW_NUMBER () OVER () as id
	,com.business_no
	,to_char(calculate_from_date, 'YYYYMM') AS year_month
	,sum(bi.count) AS counter
FROM billing_item bi
JOIN billing_source bs ON (bs.billing_source_id = bi.billing_source_id)
JOIN company com ON (com.company_id = bs.company_id)
WHERE bi.product_type = 'SERVICE'
GROUP BY com.business_no
	,bi.calculate_from_date;
