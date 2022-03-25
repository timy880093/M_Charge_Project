SELECT *
FROM contract con
JOIN company com ON (con.company_id = com.company_id)
WHERE effective_date > '2020-05-1'
	AND com.business_no = '24437714'
