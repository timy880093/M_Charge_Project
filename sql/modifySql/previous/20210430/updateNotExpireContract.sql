UPDATE contract
SET STATUS = 'E';

--因為合約的續約在舊系統中是手動進行的，因此不能期待在轉移的時候不要有任何續約
--所以預期的範圍就是轉移日期+二個月後-1秒，就是預期已出帳的合約預繳
UPDATE contract 
SET STATUS = 'C'
WHERE contract_id in (
	SELECT contract_id FROM contract con
	WHERE con.effective_date > date '2021-01-01'+ interval '2 month' - interval '1 seconds'
);
		
UPDATE contract
SET STATUS = 'C'
WHERE contract_id IN (
		SELECT contract_id
		FROM contract con
		JOIN company com ON (con.company_id = com.company_id)
			AND con.expiration_date >= '2021-01-25'
			AND con.name like '%季繳%'
		);