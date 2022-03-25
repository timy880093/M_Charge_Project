update billing_item set bill_id = null where bill_id in (
	select bill_id from bill where bill_status = 'C'
);

delete from bill where bill_status = 'C';