SELECT distinct bc.bill_id,com."name"
	,com.business_no
	,bc.year_month
	,bc.cnt
	,bc.cnt_over
	,bc.price_over
FROM bill_cycle bc
JOIN company com ON (bc.company_id = com.company_id)
join package_mode pm on(pm.company_id = bc.company_id and bc.package_id = pm.package_id )
join charge_mode_cycle_add cmca on (cmca.addition_id  = pm.addition_id )
WHERE cash_out_over_id IS NULL
	AND pay_over IS NOT NULL
	AND cnt_over IS NOT NULL
	AND cnt_over != 0
	AND price_over != 0
	AND bc.STATUS != '2'
	AND com.verify_status != 9
ORDER BY business_no;

--月租年繳的資料與超額的資料混在一起的項目，這些項目也要倒到的新的系統當中
--這樣產生出來的月租年繳才能與這些項目一起被出帳
select bc.* from cash_master cm 
join company com on(cm.company_id = com.company_id )
join cash_detail cd on(cd.cash_master_id  = cm.cash_master_id )
join bill_cycle bc on (bc.cash_out_over_id =cd.cash_detail_id )
where cm.out_ym in ('202003','202004')
and cash_out_over_id is NOT NULL
	AND pay_over IS NOT NULL
	AND cnt_over IS NOT NULL
	AND cnt_over != 0
	AND price_over != 0
	and cash_type = 2
	and cd.bill_type = 1
	AND bc.STATUS != '2'
	AND com.verify_status != 9
	order by business_no;