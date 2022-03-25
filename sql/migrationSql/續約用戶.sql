SELECT DISTINCT com.*
FROM package_mode pm
JOIN charge_mode_cycle_add cmca ON (pm.addition_id = cmca.addition_id)
JOIN company com ON (com.company_id = pm.company_id)
JOIN (
	SELECT company_id
		,count(pm.package_id)
	FROM package_mode pm
	GROUP BY company_id
	) temp1 ON (temp1.company_id = com.company_id)
WHERE cmca.real_start_date > '2020-09-01'
	AND pm.STATUS = '1'
	AND temp1.count != 1;
