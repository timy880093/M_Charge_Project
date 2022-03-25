create temp table package_overview (
	package_id BIGINT
	, package_name character varying(60)
	, enabled boolean
	, create_date TIMESTAMP without TIME zone
	, rental_fee_by_month NUMERIC(32, 9)
	, rental_grade_name CHARACTER VARYING(40)
	, overage_grade_name CHARACTER VARYING(40)
	, overage_grade_level INTEGER
	, overage_grade_cnt_start INTEGER
	, overage_grade_cnt_end INTEGER
	, overage_grade_fix_price NUMERIC(32, 9)
	, overage_grade_unit_price NUMERIC(32, 9)
	, overage_grade_accumulation boolean 
	, overage_grade_circulation boolean
	, overage_grade_maximum_charge NUMERIC(32, 9)
);


insert into package_overview(
	package_id
	, package_name
	, enabled
	, create_date
	, rental_fee_by_month 
	, rental_grade_name
)
SELECT cp.package_id 
	, cp.name as package_name
	,cp.enabled
	,cp.create_date
	,ng.fix_price AS rental_fee_by_month
	,ng.name AS rental_grade_name
FROM charge_package cp
JOIN package_ref pr ON (pr.from_package_id = cp.package_id)
JOIN charge_rule cr ON (pr.to_charge_rule_id = cr.charge_rule_id)
JOIN new_grade ng ON (
		ng.grade_id = cr.grade_id
		AND ng."level" = 0
		)
WHERE paid_plan = 'PRE_PAID';


insert into package_overview(
	package_id
	, package_name
	, overage_grade_name
	, overage_grade_level
	, overage_grade_cnt_start
	, overage_grade_cnt_end
	, overage_grade_fix_price
	, overage_grade_unit_price
	, overage_grade_accumulation
	, overage_grade_circulation
	, overage_grade_maximum_charge
)
select cp.package_id
	, cp.name as package_name
	, ng.name as overage_grade_name
	, ng.level as overage_grade_level
	, ng.cnt_start as overage_grade_cnt_start
	, ng.cnt_end as overage_grade_cnt_end
	, ng.fix_price as overage_grade_fix_price
	, ng.unit_price as overage_grade_unit_price
	, cr.accumulation as overage_grade_accumulation
	, cr.circulation as overage_grade_circulation
	, cr.maximum_charge as overage_grade_maximum_charge
from charge_package cp 
join package_ref pr on (pr.from_package_id= cp.package_id)
join charge_rule cr on (pr.to_charge_rule_id= cr.charge_rule_id)
join new_grade ng on (
		ng.grade_id = cr.grade_id or ng.root_id =cr.grade_id 
)
where paid_plan = 'POST_PAID'
order by package_id,level;

select * from package_overview order by package_id,rental_grade_name,overage_grade_level;



