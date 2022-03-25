CREATE TABLE "invoice_remaining" (
	"invoice_remaining_id" bigserial PRIMARY KEY
	,"company_id" BIGINT
	,"contract_id" BIGINT
	,"usage" INTEGER
	,"remaining" INTEGER
	,"create_date" TIMESTAMP
	);

ALTER TABLE charge_rule ADD COLUMN charge_by_remaining_count boolean NOT NULL DEFAULT FALSE;

UPDATE charge_rule
SET charge_by_remaining_count = false
WHERE charge_by_remaining_count IS NULL;
