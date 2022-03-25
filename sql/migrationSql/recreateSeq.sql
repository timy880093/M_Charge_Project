CREATE SEQUENCE iasr_seq START 101;
ALTER TABLE invoice_amount_summary_report ALTER COLUMN id SET DEFAULT nextval('iasr_seq'::regclass) NOT NULL;