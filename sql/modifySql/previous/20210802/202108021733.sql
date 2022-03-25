ALTER TABLE PUBLIC.billing_item ADD prev_invoice_remaining_id int8 NULL;

ALTER TABLE PUBLIC.billing_item ADD CONSTRAINT billing_item_prev_invoice_remaining_id_fkey FOREIGN KEY (prev_invoice_remaining_id) REFERENCES invoice_remaining (invoice_remaining_id);

ALTER TABLE PUBLIC."notice" ADD invoice_remaining_id BIGINT NULL;

ALTER TABLE notice ALTER COLUMN notice_type type VARCHAR(40);

ALTER TABLE charge_rule ALTER COLUMN charge_base_type type VARCHAR(40);
