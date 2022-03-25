SELECT *
FROM (
	SELECT str
		,count(str)
	FROM (
		SELECT CONCAT (
				expected_out_date
				,'_'
				,calculate_from_date
				,'_'
				,calculate_to_date
				,'_'
				,count
				,'_'
				,tax_excluded_amount
				,'_'
				,tax_included_amount
				) AS str
		FROM billing_item
		WHERE tax_excluded_amount IS NOT NULL
			AND tax_included_amount IS NOT NULL
			AND paid_plan = 'POST_PAID'
		) AS foo
	GROUP BY str
	) foo2
WHERE count > 1
