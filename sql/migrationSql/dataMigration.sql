CREATE TEMP TABLE overage_grade (
	source_id BIGINT
	,grade_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,LEVEL INTEGER
	,cnt_start INTEGER
	,cnt_end INTEGER
	,fix_price NUMERIC(32, 9)
	,unit_price NUMERIC(32, 9)
	,accumulation boolean
	,circulation boolean
	,maximum_charge NUMERIC(32, 9)
	);

CREATE TEMP TABLE rental_grade (
	source_id BIGINT
	,grade_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,LEVEL INTEGER
	,cnt_start INTEGER
	,cnt_end INTEGER
	,fix_price NUMERIC(32, 9)
	,unit_price NUMERIC(32, 9)
	,accumulation boolean
	,circulation boolean
	,maximum_charge NUMERIC(32, 9)
	);

CREATE TEMP TABLE without_table_grade_rental (
	source_id BIGINT
	,grade_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,LEVEL INTEGER
	,cnt_start INTEGER
	,cnt_end INTEGER
	,fix_price NUMERIC(32, 9)
	,unit_price NUMERIC(32, 9)
	,accumulation boolean
	,circulation boolean
	,maximum_charge NUMERIC(32, 9)
	);
	
CREATE TEMP TABLE without_table_grade_overage (
	source_id BIGINT
	,grade_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,LEVEL INTEGER
	,cnt_start INTEGER
	,cnt_end INTEGER
	,fix_price NUMERIC(32, 9)
	,unit_price NUMERIC(32, 9)
	,accumulation boolean
	,circulation boolean
	,maximum_charge NUMERIC(32, 9)
	);
	
CREATE TEMP TABLE with_table_grade_rental (
	source_id BIGINT
	,grade_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,LEVEL INTEGER
	,cnt_start INTEGER
	,cnt_end INTEGER
	,fix_price NUMERIC(32, 9)
	,unit_price NUMERIC(32, 9)
	,accumulation boolean
	,circulation boolean
	,maximum_charge NUMERIC(32, 9)
	);
	
CREATE TEMP TABLE with_table_grade_overage (
	source_id BIGINT
	,grade_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,LEVEL INTEGER
	,cnt_start INTEGER
	,cnt_end INTEGER
	,fix_price NUMERIC(32, 9)
	,unit_price NUMERIC(32, 9)
	,accumulation boolean
	,circulation boolean
	,maximum_charge NUMERIC(32, 9)
	);

CREATE TEMP TABLE new_grade_temp (
	source_id BIGINT
	,source CHARACTER VARYING(30)
	,grade_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,root_id BIGINT
	,LEVEL INTEGER
	,cnt_start INTEGER
	,cnt_end INTEGER
	,fix_price NUMERIC(32, 9)
	,unit_price NUMERIC(32, 9)
	,accumulation boolean
	,circulation boolean
	,maximum_charge NUMERIC(32, 9)
	);

CREATE TEMP TABLE charge_mode_temp (
	source_id BIGINT
	,source CHARACTER VARYING(30)
	,mode_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,STATUS varchar(30)
	,paid_plan varchar(30)
	,charge_plan varchar(30)
	,type CHARACTER VARYING(30)
	,product_category_id BIGINT
	);
	
CREATE TEMP TABLE charge_reference_temp(
	mode_id BIGINT
	,type CHARACTER VARYING(30)
	,calculate_cycle_type CHARACTER VARYING(30)
	,execute_cycle_type CHARACTER VARYING(30)
	,target_id BIGINT
	,maximum_charge BIGINT
	,accumulation boolean
	,circulation boolean
);

CREATE TEMP TABLE package_temp (
	source_id BIGINT
	,package_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,STATUS varchar(30)
	,package_type Integer
	,charge_id BIGINT
	);
	
CREATE TEMP TABLE package_temp_map (
	package_id BIGSERIAL
	,name CHARACTER VARYING(30)
	,STATUS varchar(30)
	,package_type INTEGER
	,charge_id BIGINT
	);

CREATE TEMP TABLE product_temp(
	product_id BIGSERIAL
	, package_id BIGINT
	, product_name CHARACTER VARYING(40)
	, product_source_id BIGINT
	, product_category_id BIGINT
	, paid_plan CHARACTER VARYING(30)
	, charge_plan CHARACTER VARYING(30)
	, product_type CHARACTER VARYING(30)
	, charge_cycle_type CHARACTER VARYING(30)
	, calculate_cycle_type CHARACTER VARYING(30)
);

CREATE TEMP TABLE mode_reference_temp(
	reference_id BIGSERIAL
	, package_id BIGINT
	, target_id BIGINT
	, modeReferenceType CHARACTER VARYING(30)
	, chargeReferenceType CHARACTER VARYING(30)
	, chargeModeTempType CHARACTER VARYING(30)
);

CREATE TEMP TABLE contract_temp (
	source_id BIGINT
	,contract_id BIGSERIAL
	,package_id BIGINT
	,name CHARACTER VARYING(50)
	,company_id INTEGER
	,STATUS "char" NOT NULL
	,sales_price NUMERIC(32, 9)
	,period_month int
	,effective_date TIMESTAMP without TIME zone
	,expiration_date TIMESTAMP without TIME zone
	,real_start_date TIMESTAMP without TIME zone
	,real_end_date TIMESTAMP without TIME zone
	,create_date TIMESTAMP without TIME zone
	);
	
INSERT INTO overage_grade (
	source_id
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,maximum_charge
	,accumulation
	,circulation
	,unit_price
	)
SELECT DISTINCT cmc.charge_id
	,package_name AS name
	,0 AS LEVEL
	,base_quantity + 1 AS cnt_start
	,999999 AS cnt_end
	,cmc.max_price - cmc.sales_price AS maximum_charge
	,false AS accumalate
	,false AS circulation
	,cmc.single_price AS unit_price
FROM charge_mode_cycle cmc
JOIN package_mode pm ON (pm.charge_id = cmc.charge_id)
ORDER BY charge_id;

--ignore suspend package or disabled bill_cycle data
INSERT INTO rental_grade (
	source_id
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,maximum_charge
	,accumulation
	,circulation
	,fix_price
	)
SELECT DISTINCT cmc.charge_id
	,package_name AS name
	,0 AS LEVEL
	,0 AS cnt_start
	,cmc.base_quantity AS cnt_end
	,cmc.max_price AS maximum_charge
	,true AS accumalate
	,false AS circulation
	,cmc.sales_price AS fix_price
FROM charge_mode_cycle cmc
JOIN package_mode pm ON (pm.charge_id = cmc.charge_id);

--without_table_grade的循環級距是從第二階開始的
INSERT INTO without_table_grade_rental (
	source_id
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,accumulation
	,circulation
	)
SELECT distinct cmg.charge_id
	,package_name AS name
	,0 AS LEVEL
	,0 AS cnt_start
	,base_quantity AS cnt_end
	,grade_price AS fix_price
	,false AS accumulation
	,false AS circulation
FROM charge_mode_grade cmg
JOIN package_mode pm ON (pm.charge_id = cmg.charge_id and pm.package_type = 2)
WHERE has_grade = 'N';

INSERT INTO without_table_grade_overage (
	source_id
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,accumulation
	,circulation
	)
SELECT distinct cmg.charge_id
	,package_name AS name
	,0 AS LEVEL
	,base_quantity+1 AS cnt_start
	,base_quantity*2 AS cnt_end
	,grade_price AS fix_price
	,true AS accumulation
	,true AS circulation
FROM charge_mode_grade cmg
JOIN package_mode pm ON (pm.charge_id = cmg.charge_id and pm.package_type = 2)
WHERE has_grade = 'N';

--with_table_grade的級距是從第二階開始的
INSERT INTO with_table_grade_rental (
	source_id
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,accumulation
	,circulation
	)
SELECT distinct cmg.charge_id
	,package_name AS name
	,0 AS LEVEL
	,0 AS cnt_start
	,base_quantity AS cnt_end
	,sales_price AS fix_price
	,false AS accumulation
	,false AS circulation
FROM charge_mode_grade cmg
JOIN package_mode pm ON (pm.charge_id = cmg.charge_id and pm.package_type =2)
JOIN grade g ON (g.charge_id = cmg.charge_id)
where has_grade = 'Y' and cnt_start=0;

INSERT INTO with_table_grade_overage (
	source_id
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,accumulation
	,circulation
	)
SELECT DISTINCT grade_id
	,package_name AS name
	,0 AS LEVEL
	,g.cnt_start
	,g.cnt_end
	,grade_price AS fix_price
	,true AS accumulation
	,false AS circulation
FROM charge_mode_grade cmg
JOIN package_mode pm ON (pm.charge_id = cmg.charge_id and pm.package_type =2)
JOIN grade g ON (g.charge_id = cmg.charge_id)
where has_grade = 'Y' and cnt_start!=0;

INSERT INTO new_grade_temp (
	source_id
	,source
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT source_id
	,'OVERAGE' AS source
	,CONCAT (
		name
		,'_overage'
		) AS name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
FROM overage_grade;

INSERT INTO new_grade_temp (
	source_id
	,source
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT source_id
	,'RENTAL' AS source
	,CONCAT (
		name
		,'_rental'
		) AS name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
FROM rental_grade;

INSERT INTO new_grade_temp (
	source_id
	,source
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT source_id
	,'without_rental' AS source
	,CONCAT (
		name
		,'_rental'
		) AS name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
FROM without_table_grade_rental;

INSERT INTO new_grade_temp (
	source_id
	,source
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT source_id
	,'without_overage' AS source
	,CONCAT (
		name
		,'_overage'
		) AS name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
FROM without_table_grade_overage;

INSERT INTO new_grade_temp (
	source_id
	,source
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT source_id
	,'with_rental' AS source
	,CONCAT (
		name
		,'_rental'
		) AS name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
FROM with_table_grade_rental;

INSERT INTO new_grade_temp (
	source_id
	,source
	,name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT source_id
	,'with_overage' AS source
	,CONCAT (
		name
		,'_overage'
		) AS name
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,maximum_charge
	,accumulation
	,circulation
FROM with_table_grade_overage;

--update grade 
UPDATE new_grade_temp
SET fix_price = price
FROM grade g
WHERE g.grade_id = source_id
	AND source = 'with_overage';
	
--next level fix for has grade table 
UPDATE new_grade_temp
SET fix_price = next_level_price
FROM (
	SELECT grade_id
		,LEVEL
		,(
			SELECT fix_price
			FROM new_grade_temp ngt2
			WHERE ngt2.grade_id = ngt1.grade_id + 1
			) next_level_price
	FROM new_grade_temp ngt1
	WHERE source = 'with_overage'
	) AS nlp
WHERE source = 'with_overage';

--level and root_id update
UPDATE new_grade_temp
SET root_id = cl.root_id
	,LEVEL = cl.LEVEL
FROM (
	SELECT grade_id
		,name
		,ROW_NUMBER() OVER (
			PARTITION BY name
			,maximum_charge ORDER BY name
				,maximum_charge
				,cnt_start
			) - 1 AS LEVEL
		,CASE 
			WHEN maximum_charge IS NOT NULL
				THEN (
						SELECT grade_id
						FROM new_grade_temp ng2
						WHERE ng.name = ng2.name
							AND cnt_start = 0
							AND source = 'with_overage'
						)
			WHEN maximum_charge IS NULL
				THEN (
						SELECT grade_id
						FROM new_grade_temp ng2
						WHERE ng.name = ng2.name
							AND cnt_start = 0
							AND source = 'with_overage'
						)
			END AS root_id
	FROM new_grade_temp ng
	WHERE source = 'with_overage'
	) cl
WHERE new_grade_temp.grade_id = cl.grade_id;

-- only available for type overage
UPDATE new_grade_temp
SET unit_price = NULL
WHERE source != 'OVERAGE';

--if source not with then root_id is it selfs
UPDATE new_grade_temp
SET root_id = grade_id
WHERE source IN (
		'RENTAL'
		,'OVERAGE'
		,'without_rental'
		,'without_overage'
		, 'with_rental'
		);
		
--with table overage root_id update
UPDATE new_grade_temp ngt2
SET root_id = ngtg.grade_id
FROM (
	SELECT *
	FROM new_grade_temp ngt1
	WHERE root_id IS NULL
		AND LEVEL = 0
		AND ngt1.source = 'with_overage'
	) ngtg
WHERE ngtg.source = ngt2.source
	AND ngtg.name = ngt2.name;

INSERT INTO new_grade (
	grade_id
	,name
	,root_id
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,enabled
	,creator_id
	,create_date
	,modifier_id
	,modify_date
	)
SELECT grade_id
	,name
	,root_id
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,true as enabled
	,69 AS creator_id
	,now()::TIMESTAMP
	,69 AS modifier_id
	,now()::TIMESTAMP
FROM new_grade_temp;

--insert default category data
INSERT INTO "public"."product_category"("product_category_id","category_name", "parent_category_id") VALUES(1,'服務', NULL);
INSERT INTO "public"."product_category"("product_category_id","category_name", "parent_category_id") VALUES(2,'月租', 1);
INSERT INTO "public"."product_category"("product_category_id","category_name", "parent_category_id") VALUES(3,'超額', 1);
INSERT INTO "public"."product_category"("product_category_id","category_name", "parent_category_id") VALUES(4,'扣抵', 1);

--rental
INSERT INTO charge_mode_temp (
	source_id
	,source
	,name
	,STATUS
	,paid_plan
	,charge_plan
	,type
	,product_category_id
	)
SELECT charge_id AS source_id
	,'cycle' AS source
	,package_name AS name
	,'E' AS STATUS
	,'PRE_PAID'
	,'PERIODIC'
	,'RENTAL' AS type
	, 2
FROM charge_mode_cycle
where charge_id in (select charge_id from package_mode where status!='2');

--overage
INSERT INTO charge_mode_temp (
	source_id
	,source
	,name
	,STATUS
	,paid_plan
	,charge_plan
	,type
	,product_category_id
	)
SELECT charge_id AS source_id
	,'cycle' AS source
	,CONCAT (
		package_name
		,'_overage'
		) AS name
	,'E' AS STATUS
	,'POST_PAID'
	,'PERIODIC'
	,'OVERAGE' AS type
	,3
FROM charge_mode_cycle
where charge_id in (select charge_id from package_mode where status!='2');

--withoutgrade_rental
INSERT INTO charge_mode_temp (
	source_id
	,source
	,name
	,STATUS
	,paid_plan
	,charge_plan
	,type
	,product_category_id
	)
SELECT charge_id AS source_id
	,'grade' AS source
	,CONCAT (
		package_name
		,'_rental'
		) AS name
	,'E' AS STATUS
	,'PRE_PAID'
	,'PERIODIC'
	,'without_rental' AS type
	,2
FROM charge_mode_grade cmg
where charge_id in (select charge_id from package_mode where status!='2') and cmg.has_grade ='N';

--withoutgrade_overage
INSERT INTO charge_mode_temp (
	source_id
	,source
	,name
	,STATUS
	,paid_plan
	,charge_plan
	,type
	,product_category_id
	)
SELECT charge_id AS source_id
	,'grade' AS source
	,CONCAT (
		package_name
		,'_overage'
		) AS name
	,'E' AS STATUS
	,'POST_PAID'
	,'PERIODIC'
	,'without_overage' AS type
	,3
FROM charge_mode_grade cmg
where charge_id in (select charge_id from package_mode where status!='2') and cmg.has_grade = 'N';

INSERT INTO charge_mode_temp (
	source_id
	,source
	,name
	,STATUS
	,paid_plan
	,charge_plan
	,type
	,product_category_id
	)
SELECT charge_id AS source_id
	,'grade' AS source
	,CONCAT (
		package_name
		,'_rental'
		) AS name
	,'E' AS STATUS
	,'PRE_PAID'
	,'PERIODIC'
	,'with_rental' AS type
	,2
FROM charge_mode_grade cmg
where charge_id in (select charge_id from package_mode where status!='2') and cmg.has_grade = 'Y';

INSERT INTO charge_mode_temp (
	source_id
	,source
	,name
	,STATUS
	,paid_plan
	,charge_plan
	,type
	,product_category_id
	)
SELECT charge_id AS source_id
	,'grade' AS source
	,CONCAT (
		package_name
		,'_overage'
		) AS name
	,'E' AS STATUS
	,'POST_PAID'
	,'PERIODIC'
	,'with_overage' AS type
	,3
FROM charge_mode_grade cmg
where charge_id in (select charge_id from package_mode where status!='2') and cmg.has_grade = 'Y';

--insert charge_reference and new_grade mappings (with)
INSERT INTO charge_reference_temp (
	mode_id
	,type
	,target_id
	,calculate_cycle_type
	,execute_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT DISTINCT cmt.mode_id AS mode_id
	,'grade' AS type
	,ngt.grade_id AS target_id
	,CASE 
			WHEN cmt.name like '%年繳%'
				THEN 'GW_RENTAL_CAL'
			WHEN cmt.name like '%季繳%'
				THEN 'GW_RENTAL_CAL'
			ELSE 
				'GW_RENTAL_CAL'
			END AS calculate_cycle_type
	,CASE 
			WHEN cmt.name like '%年繳%'
				THEN 'ANY'
			WHEN cmt.name like '%季繳%'
				THEN 'SEASON'
			ELSE 
				'ANY'
			END AS execute_cycle_type
	,ngt.maximum_charge
	,ngt.accumulation
	,ngt.circulation
FROM charge_mode_temp cmt
JOIN charge_mode_grade cmg ON (cmt.source_id = cmg.charge_id)
JOIN new_grade_temp ngt ON (
		ngt.source_id = cmt.source_id
		AND ngt.source = 'with_rental'
		AND ngt.LEVEL = 0
		)
WHERE cmt.source = 'grade'
	AND cmg.has_grade = 'Y'
	AND cmt.type = 'with_rental';

--insert charge_reference and new_grade mappings (with)
INSERT INTO charge_reference_temp (
	mode_id
	,type
	,target_id
	,calculate_cycle_type
	,execute_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT cmt.mode_id AS mode_id
	,'grade' AS type
	,ngt.grade_id AS target_id
	,'GW_OVERAGE_BIL_CAL' as calculate_cycle_type
	,'GW_OVERAGE_BIL' AS execute_cycle_type
	,ngt.maximum_charge
	,ngt.accumulation
	,ngt.circulation
FROM charge_mode_temp cmt
JOIN charge_mode_grade cmg ON (cmt.source_id = cmg.charge_id)
JOIN grade g ON (
		g.charge_id = cmg.charge_id
		)
JOIN new_grade_temp ngt ON (
		ngt.source_id = g.grade_id
		AND ngt.source = 'with_overage'
		)
WHERE cmt.source = 'grade' and cmg.has_grade = 'Y' and ngt.level = 0 and cmt.type = 'with_overage';

--insert charge_reference and new_grade mappings (without)
INSERT INTO charge_reference_temp (
	mode_id
	,type
	,target_id
	,calculate_cycle_type
	,execute_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT cmt.mode_id AS mode_id
	,'grade' AS type
	,ngt.grade_id AS target_id
	,CASE 
			WHEN cmt.name like '%年繳%'
				THEN 'GW_RENTAL_CAL'
			WHEN cmt.name like '%季繳%'
				THEN 'GW_RENTAL_CAL'
			ELSE 
				'GW_RENTAL_CAL'
			END AS calculate_cycle_type
	,CASE 
			WHEN cmt.name like '%年繳%'
				THEN 'ANY'
			WHEN cmt.name like '%季繳%'
				THEN 'SEASON'
			ELSE 
				'ANY'
			END AS execute_cycle_type
	,ngt.maximum_charge
	,ngt.accumulation
	,ngt.circulation
FROM charge_mode_temp cmt
JOIN charge_mode_grade cmg ON (cmt.source_id = cmg.charge_id)
JOIN new_grade_temp ngt ON (
		ngt.source = 'without_rental'
		AND ngt.source_id = cmt.source_id
		)
WHERE cmt.source = 'grade' and cmg.has_grade = 'N' and cmt.type = 'without_rental';

INSERT INTO charge_reference_temp (
	mode_id
	,type
	,target_id
	,calculate_cycle_type
	,execute_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT cmt.mode_id AS mode_id
	,'grade' AS type
	,ngt.grade_id AS target_id
	,'GW_OVERAGE_BIL_CAL' as calculate_cycle_type
	,'GW_OVERAGE_BIL' AS execute_cycle_type
	,ngt.maximum_charge
	,ngt.accumulation
	,ngt.circulation
FROM charge_mode_temp cmt
JOIN charge_mode_grade cmg ON (cmt.source_id = cmg.charge_id)
JOIN new_grade_temp ngt ON (
		ngt.source = 'without_overage'
		AND ngt.source_id = cmt.source_id
		)
WHERE cmt.source = 'grade' and cmg.has_grade = 'N' and cmt.type = 'without_overage';

--insert charge_reference and new_grade mappings (rental)
INSERT INTO charge_reference_temp (
	mode_id
	,type
	,target_id
	,calculate_cycle_type
	,execute_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT cmt.mode_id AS mode_id
	,'grade' AS type
	,ngt.grade_id AS target_id
	,'GW_RENTAL_CAL' as calculate_cycle_type
	,CASE 
			WHEN cmt.name like '%年繳%'
				THEN 'ANY'
			WHEN cmt.name like '%季繳%'
				THEN 'SEASON'
			ELSE 
				'ANY'
			END AS execute_cycle_type
	,ngt.maximum_charge
	,ngt.accumulation
	,ngt.circulation
FROM charge_mode_temp cmt
JOIN charge_mode_cycle cmc ON (cmt.source_id = cmc.charge_id)
JOIN new_grade_temp ngt ON (
		ngt.source = 'RENTAL'
		AND ngt.source_id = cmt.source_id
		)
WHERE cmt.source = 'cycle'
	AND type = 'RENTAL';

--insert charge_reference and new_grade mapping (overage)
INSERT INTO charge_reference_temp (
	mode_id
	,type
	,target_id
	,calculate_cycle_type
	,execute_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	)
SELECT cmt.mode_id AS mode_id
	,'grade' AS type
	,ngt.grade_id AS target_id
	,'GW_OVERAGE_BIL_CAL' as calculate_cycle_type
	,'GW_OVERAGE_BIL' as execute_cycle_type
	,ngt.maximum_charge
	,ngt.accumulation
	,ngt.circulation
FROM charge_mode_temp cmt
JOIN charge_mode_cycle cmc ON (cmt.source_id = cmc.charge_id)
JOIN new_grade_temp ngt ON (
		ngt.source = 'OVERAGE'
		AND ngt.source_id = cmt.source_id
		)
WHERE cmt.source = 'cycle'
	AND cmt.type = 'OVERAGE';

--insert into charge_mode
INSERT INTO charge_mode (
	charge_mode_id
	,name
	,enabled
	,paid_plan
	,charge_plan
	,product_category_id
	,grade_id
	,charge_base_type
	,calculate_cycle_type
	,charge_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	,creator_id
	,create_date
	,modifier_id
	,modify_date
	)
SELECT cmt.mode_id
	,name
	,true
	,paid_plan
	,charge_plan
	,product_category_id
	,crt.target_id AS grade_id
	,'INVOICE_AMOUNT_SUM' as charge_base_type
	,calculate_cycle_type
	,execute_cycle_type
	,maximum_charge
	,accumulation
	,circulation
	,69 AS creator_id
	,now()::TIMESTAMP
	,69 AS modifier_id
	,now()::TIMESTAMP
FROM charge_mode_temp cmt
JOIN charge_reference_temp crt on(crt.mode_id = cmt.mode_id);

--insert package_mode to package_temp
INSERT INTO package_temp (
	source_id
	,name
	,status
	,package_type
	,charge_id
)
SELECT
    pm.package_id
     ,CASE 
		WHEN (package_type = 1)
			THEN cmc.package_name
		WHEN (package_type = 2)
			THEN cmg.package_name
		END AS name
	,'E' AS STATUS
	,pm.package_type
	,pm.charge_id
FROM package_mode pm
JOIN charge_mode_cycle_add cmca ON (pm.addition_id = cmca.addition_id)
LEFT JOIN charge_mode_cycle cmc ON (cmc.charge_id = pm.charge_id)
LEFT JOIN charge_mode_grade cmg ON (cmg.charge_id = pm.charge_id);

INSERT INTO package_temp_map (
	name
	, status
	, package_type
	, charge_id
)
SELECT DISTINCT name
	,STATUS
	,package_type
	,charge_id
FROM package_temp;

INSERT INTO charge_package(
	package_id
	,name
	, enabled
	, creator_id
	, create_date
	, modifier_id
	, modify_date
)
SELECT package_id 
	, name
	, true
	,69 AS creator_id
	,now()::TIMESTAMP AS create_date
	,69 AS modifier_id
	,now()::TIMESTAMP AS modify_date
FROM package_temp_map pt;

--insert product_reference for rental mode
INSERT INTO product_temp (
	package_id
	,product_source_id
	,charge_plan
	,paid_plan
	,product_name
	,product_category_id
	,charge_cycle_type
	,calculate_cycle_type
	)
SELECT DISTINCT ptm.package_id
	,cmt.mode_id AS to_charge_mode_id
	,'PERIODIC' AS charge_plan
	,'PRE_PAID' AS paid_plan
	, ptm.name
	, 2 AS product_category_id 
	,crt.execute_cycle_type as charge_cycle_type
	,crt.calculate_cycle_type as calculate_cycle_type
FROM charge_mode_temp cmt
JOIN charge_reference_temp crt ON (crt.mode_id = cmt.mode_id)
JOIN package_temp_map ptm ON (
		ptm.charge_id = cmt.source_id
		AND ptm.package_type = 1
		)
JOIN package_temp pt ON (
		pt.package_type = ptm.package_type
		AND pt.charge_id = ptm.charge_id
		)
WHERE source = 'cycle'
	AND crt.type = 'grade'
	AND cmt.type = 'RENTAL';

--insert product_reference for overage mode
INSERT INTO product_temp (
	package_id
	,product_source_id
	,charge_plan
	,paid_plan
	,product_name
	,product_category_id
	,charge_cycle_type
	,calculate_cycle_type
	)
SELECT DISTINCT ptm.package_id
	,cmt.mode_id AS to_charge_mode_id
	,'PERIODIC' AS charge_plan
	,'POST_PAID' AS paid_plan
	, ptm.name
	, 3
	,crt.execute_cycle_type as charge_cycle_type
	,crt.calculate_cycle_type as calculate_cycle_type
FROM charge_mode_temp cmt
JOIN charge_reference_temp crt ON (crt.mode_id = cmt.mode_id)
JOIN package_temp_map ptm ON (
		ptm.charge_id = cmt.source_id
		AND ptm.package_type = 1
		)
JOIN package_temp pt ON (
		pt.package_type = ptm.package_type
		AND pt.charge_id = ptm.charge_id
		)
WHERE source = 'cycle'
	AND crt.type = 'grade'
	AND cmt.type = 'OVERAGE';
	
INSERT INTO product_temp (
	package_id
	,product_source_id
	,charge_plan
	,paid_plan
	,product_name
	,product_category_id
	,charge_cycle_type
	,calculate_cycle_type
	)
SELECT DISTINCT ptm.package_id
	,cmt.mode_id AS to_charge_mode_id
	,'PERIODIC' AS charge_plan
	,'PRE_PAID' AS paid_plan
	, ptm.name
	, 2
	,crt.execute_cycle_type as charge_cycle_type
	,crt.calculate_cycle_type as calculate_cycle_type
FROM charge_mode_temp cmt
JOIN charge_reference_temp crt ON (crt.mode_id = cmt.mode_id)
JOIN package_temp_map ptm ON (
		ptm.charge_id = cmt.source_id
		AND ptm.package_type = 2
		)
JOIN package_temp pt ON (
		pt.package_type = ptm.package_type
		AND pt.charge_id = ptm.charge_id
		)
WHERE source = 'grade'
	AND crt.type = 'grade'
	AND cmt.type = 'without_rental';
	
INSERT INTO product_temp (
	package_id
	,product_source_id
	,charge_plan
	,paid_plan
	,product_name
	,product_category_id
	,charge_cycle_type
	,calculate_cycle_type
	)
SELECT DISTINCT ptm.package_id
	,cmt.mode_id AS to_charge_mode_id
	,'PERIODIC' AS charge_plan
	,'POST_PAID' AS paid_plan
	, ptm.name
	, 3
	,crt.execute_cycle_type as charge_cycle_type
	,crt.calculate_cycle_type as calculate_cycle_type
FROM charge_mode_temp cmt
JOIN charge_reference_temp crt ON (crt.mode_id = cmt.mode_id)
JOIN package_temp_map ptm ON (
		ptm.charge_id = cmt.source_id
		AND ptm.package_type = 2
		)
JOIN package_temp pt ON (
		pt.package_type = ptm.package_type
		AND pt.charge_id = ptm.charge_id
		)
WHERE source = 'grade'
	AND crt.type = 'grade'
	AND cmt.type = 'without_overage';

--insert product_reference for grade mode
INSERT INTO product_temp (
	package_id
	,product_source_id
	,charge_plan
	,paid_plan
	,product_name
	,product_category_id
	,charge_cycle_type
	,calculate_cycle_type
	)
SELECT DISTINCT ptm.package_id
	,cmt.mode_id AS to_charge_mode_id
	,'PERIODIC' AS charge_plan
	,'PRE_PAID' AS paid_plan
	, ptm.name
	, 3 as product_category_id
	,crt.execute_cycle_type as charge_cycle_type
	, crt.calculate_cycle_type as calculate_cycle_type
FROM charge_mode_temp cmt
JOIN charge_reference_temp crt ON (crt.mode_id = cmt.mode_id)
JOIN package_temp_map ptm ON (
		ptm.charge_id = cmt.source_id
		AND ptm.package_type = 2
		)
JOIN package_temp pt ON (
		pt.package_type = ptm.package_type
		AND pt.charge_id = ptm.charge_id
		)
WHERE source = 'grade'
	AND crt.type = 'grade'
	AND cmt.type = 'with_rental';
	
INSERT INTO product_temp (
	package_id
	,product_source_id
	,charge_plan
	,paid_plan
	,product_name
	,product_category_id
	,charge_cycle_type
	,calculate_cycle_type
	)
SELECT DISTINCT ptm.package_id
	,cmt.mode_id AS to_charge_mode_id
	,'PERIODIC' AS charge_plan
	,'POST_PAID' AS paid_plan
	, ptm.name
	, 3 as product_category_id
	,crt.execute_cycle_type as charge_cycle_type
	,crt.calculate_cycle_type as calculate_cycle_type
FROM charge_mode_temp cmt
JOIN charge_reference_temp crt ON (crt.mode_id = cmt.mode_id)
JOIN package_temp_map ptm ON (
		ptm.charge_id = cmt.source_id
		AND ptm.package_type = 2
		)
JOIN package_temp pt ON (
		pt.package_type = ptm.package_type
		AND pt.charge_id = ptm.charge_id
		)
WHERE source = 'grade'
	AND crt.type = 'grade'
	AND cmt.type = 'with_overage';

INSERT INTO product(
	product_id
	, product_name
	, product_type
	, creator_id
	, create_date
	, modifier_id
	, modify_date
	, product_category_id
)select product_id
	, product_name
	, 'SERVICE' 
	,69 AS creator_id
	,now()::TIMESTAMP AS create_date
	,69 AS modifier_id
	,now()::TIMESTAMP AS modify_date
	, product_category_id
FROM product_temp;

INSERT INTO package_ref (
	from_package_id
	, to_charge_mode_id
)
SELECT package_id
	, product_source_id
FROM product_temp;

--insert package_mode to contract_temp
--user need to change start_date and end_date when package closed been canceled.
INSERT INTO contract_temp (
	source_id
	,package_id
	,name
	,company_id
	,STATUS
	,sales_price
	,period_month
	,effective_date
	,expiration_date
	,real_start_date
	,real_end_date
	,create_date
	)
SELECT pm.package_id
	,ptm.package_id
	,pt.name
	,com.company_id AS company_id
	,'Y' as status
	,CASE 
		WHEN (pm.package_type = 1)
			THEN cmc.sales_price
		WHEN (pm.package_type = 2)
			THEN cmg.sales_price
		END AS sales_price
	,CASE 
		WHEN (pm.package_type = 1)
			THEN cmc.contract_limit
		WHEN (pm.package_type = 2)
			THEN cmg.contract_limit 
		END AS period_month
	,cmca.real_start_date AS effective_date
	,cmca.real_end_date AS expiration_date
	,cmca.real_start_date AS real_start_date
	,cmca.real_end_date AS real_end_date
	,pm.create_date as create_date
FROM package_mode pm
JOIN charge_mode_cycle_add cmca ON (pm.addition_id = cmca.addition_id)
JOIN package_temp pt ON (pt.source_id = pm.package_id)
JOIN package_temp_map ptm ON (
		pt.package_type = ptm.package_type
		AND pt.charge_id = ptm.charge_id
		)
LEFT JOIN charge_mode_cycle cmc ON (cmc.charge_id = pm.charge_id)
LEFT JOIN charge_mode_grade cmg ON (cmg.charge_id = pm.charge_id)
JOIN company com ON (com.company_id = pm.company_id);

--insert package_mode to contract
INSERT INTO contract (
	contract_id
	,name
	,company_id
	,package_id
	,STATUS
	,auto_renew
	,period_month
	,effective_date
	,expiration_date
	,is_first_contract
	,first_invoice_date_as_effective_date
	,remark
	,create_date
	)
SELECT contract_id
	,name
	,company_id
	,package_id
	,'T'
	,true
	,period_month
	,real_start_date
	,date_trunc('day', real_end_date) + interval '1 day' - interval '1 second'
	,false
	,false
	,source_id
	,create_date
FROM contract_temp;

--reset sequence
SELECT setval('charge_mode_charge_mode_id_seq', (SELECT COALESCE(MAX(charge_mode_id),1) FROM charge_mode),true);
SELECT setval('package_ref_package_ref_id_seq', (SELECT COALESCE(MAX(package_ref_id),1) FROM package_ref),true);
SELECT setval('contract_contract_id_seq', (SELECT COALESCE(MAX(contract_id),1) FROM contract),true);
SELECT setval('new_grade_grade_id_seq', (SELECT COALESCE(MAX(grade_id),1) FROM new_grade),true);
SELECT setval('charge_package_package_id_seq', (SELECT COALESCE(MAX(package_id),1) FROM charge_package),true);
SELECT setval('product_product_id_seq', (SELECT COALESCE(MAX(product_id),1) FROM product),true);

--wrong data fix
update new_grade set fix_price = unit_price , unit_price = null where grade_id in (5,6,8);

--update cnt_end for specific grade
UPDATE new_grade ngm
SET cnt_end = new_cnt_end
FROM (
	SELECT ng.grade_id
		,unit_price
		,cnt_end
		,cr.maximum_charge
		,ROUND(maximum_charge / unit_price, 0) + cnt_start - 1 AS new_cnt_end
	FROM new_grade ng
	JOIN charge_mode cr ON (cr.grade_id = ng.grade_id)
	WHERE unit_price IS NOT NULL
		AND maximum_charge != 0
	) AS new_ng
WHERE new_ng.grade_id = ngm.grade_id;

--remove maximum_charge for specific grade
UPDATE charge_mode
SET maximum_charge = NULL,accumulation = true
WHERE charge_mode_id IN (
	SELECT charge_mode_id
	FROM charge_mode
	WHERE grade_id IN (
		select grade_id from (
			select * from new_grade where name like '%overage%'
			AND root_id = grade_id
		) as overage_tmp
		where name like '%馬上開%'
			or name like '%200型%'
			or name like '%穩穩開%'
			or name like '%400型%'
			or name like '%隨便開%'
			and name not like '%級距%'
			and name not like '%A%'
	)
);

INSERT INTO new_grade (
	name
	,root_id
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,enabled
	,creator_id
	,create_date
	,modifier_id
	,modify_date
	)
SELECT name
	,root_id
	,(select count(grade_id) from new_grade where root_id = ng.grade_id) AS level
	,ng.cnt_end +1 AS cnt_end
	,15000 AS cnt_end
	,0 AS fix_price
	,null AS unit_price
	,true as enabled
	,creator_id
	,create_date
	,modifier_id
	,modify_date
FROM new_grade ng
WHERE grade_id IN (
	SELECT grade_id
	FROM (
		SELECT *
		FROM new_grade
		WHERE name LIKE '%overage%'
		AND root_id = grade_id
		) AS overage_tmp
	WHERE name LIKE '%馬上開%'
		OR name LIKE '%200型%'
		OR name LIKE '%穩穩開%'
		OR name LIKE '%400型%'
		and name not like '%A%'
		AND name NOT LIKE '%級距%'
);

--insert additional overage grade
INSERT INTO new_grade (
	name
	,root_id
	,LEVEL
	,cnt_start
	,cnt_end
	,fix_price
	,unit_price
	,enabled
	,creator_id
	,create_date
	,modifier_id
	,modify_date
	)
SELECT name
	,root_id
	,(select count(grade_id) from new_grade where root_id = ng.grade_id) AS LEVEL
	,15001 AS cnt_start
	,30000 AS cnt_end
	,760 AS fix_price
	,null AS unit_price
	,true as enabled
	,creator_id
	,create_date
	,modifier_id
	,modify_date
FROM new_grade ng
WHERE grade_id IN (
	SELECT grade_id
	FROM (
		SELECT *
		FROM new_grade
		WHERE name LIKE '%overage%'
		AND root_id = grade_id
		) AS overage_tmp
	WHERE name LIKE '%馬上開%'
		OR name LIKE '%200型%'
		OR name LIKE '%穩穩開%'
		OR name LIKE '%400型%'
		and name not like '%A%'
		AND name NOT LIKE '%級距%'
);
		
--update overage grade
update new_grade
SET cnt_end=30000
	,unit_price=NULL
	,fix_price=760
WHERE grade_id IN (
	SELECT grade_id
	FROM (
		SELECT *
		FROM new_grade
		WHERE name LIKE '%overage%'
		AND root_id = grade_id
		) AS overage_tmp
	WHERE name LIKE '%隨便開%'
		and name not like '%A%'
		AND name NOT LIKE '%級距%'
);
		
--insert another overage grade
INSERT INTO new_grade (
	name
	,root_id
	,LEVEL
	,cnt_start
	,cnt_end
	,unit_price
	,fix_price
	,enabled
	,creator_id
	,create_date
	,modifier_id
	,modify_date
	)
SELECT name
	,root_id
	,(select count(grade_id) from new_grade where root_id = ng.grade_id) AS LEVEL
	,30001 AS cnt_start
	,45000 AS cnt_end
	,null AS unit_price
	,760 AS fix_price
	,true as enabled
	,creator_id
	,create_date
	,modifier_id
	,modify_date
FROM new_grade ng
WHERE grade_id in (
	SELECT charge_mode_id
	FROM charge_mode
	WHERE grade_id IN (
		select grade_id from (
			select * from new_grade where name like '%overage%'
			AND root_id = grade_id
		) as overage_tmp
		where name like '%馬上開%'
			or name like '%200型%'
			or name like '%穩穩開%'
			or name like '%400型%'
			or name like '%隨便開%'
			and name not like '%A%'
			and name not like '%級距%'
	)
);

UPDATE contract
SET allow_partial_billing = true
WHERE contract_id IN (
		SELECT contract_id
		FROM contract con
		JOIN charge_package cp ON (cp.package_id = con.package_id)
		JOIN package_ref pr ON (pr.from_package_id = con.package_id)
		WHERE to_charge_mode_id IN (
				SELECT charge_mode_id
				FROM charge_mode
				WHERE name LIKE '%季繳%'
				)
		);

UPDATE contract
SET allow_partial_billing = false
WHERE contract_id IN (
	SELECT contract_id
		FROM contract con
		JOIN charge_package cp ON (cp.package_id = con.package_id)
		JOIN package_ref pr ON (pr.from_package_id = con.package_id)
		WHERE to_charge_mode_id NOT IN (
				SELECT charge_mode_id
				FROM charge_mode
				WHERE name LIKE '%季繳%'
				)
);

CREATE SEQUENCE invoice_amount_summary_report_id_seq;
ALTER SEQUENCE invoice_amount_summary_report_id_seq OWNED BY invoice_amount_summary_report.id;
SELECT setval('public.invoice_amount_summary_report_id_seq', (SELECT COALESCE(MAX(id),1) FROM invoice_amount_summary_report),true);

--create index for invoice_amount_summary_report
CREATE INDEX IF NOT EXISTS idx_IASR_invoice_date ON invoice_amount_summary_report(invoice_date);
CREATE INDEX IF NOT EXISTS idx_IASR_seller ON invoice_amount_summary_report(seller);
CREATE UNIQUE INDEX IF NOT EXISTS iasr_pkey ON public.invoice_amount_summary_report USING btree (id);

--truncate table new_grade,charge_reference,charge_mode,contract,product_reference,billing_item,charge_package;
--DISCARD TEMP;
