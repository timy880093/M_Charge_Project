SELECT DISTINCT bc.*
FROM bill_cycle bc
JOIN company com ON (bc.company_id = com.company_id)
JOIN cash_detail cd ON (bc.cash_out_over_id = cd.cash_detail_id)
join package_mode pm on (pm.package_id = bc.package_id)
join charge_mode_cycle_add cmca on (cmca.addition_id = pm.addition_id)
WHERE com.verify_status != 9
	AND bc.STATUS != '2'
	AND cd.out_ym IN (
		'202005'
		,'202006'
		)
--and cmca.end_date < '2020-07-13'
--and com.business_no  = '97169756'
and com.company_id  = 2300;

--new system 
SELECT com.name
	,com.business_no
	,bl.business_tax_amount + bl.tax_excluded_amount AS total
	,bl.business_tax_amount
	,bl.tax_excluded_amount
	,bl.remark
	,bl.expected_entry_date
	,bl.out_to_bank_date
FROM bill bl
JOIN company com ON (com.company_id = bl.company_id)
WHERE bill_status = 'C'
and remark = '202009';

	
