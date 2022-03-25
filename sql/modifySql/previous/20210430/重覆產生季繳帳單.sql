SELECT *
FROM company
WHERE business_no IN (
		SELECT *
		FROM (
			SELECT com.business_no
				,count(bill_id)
			FROM contract con
			JOIN bill bl ON (
					bl.company_id = con.company_id
					AND bl.bill_status = 'C'
					)
			JOIN company com ON (con.company_id = com.company_id)
				AND con.expiration_date >= '2021-04-20'
				AND con.name LIKE '%季繳%'
			GROUP BY com.business_no
			) AS v1
		WHERE count > 1
		);
