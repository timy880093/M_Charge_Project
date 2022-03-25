CREATE TABLE "contract" (
  "contract_id" bigserial PRIMARY KEY,
  "name" varchar(50),
  "company_id" bigint,
  "package_id" bigint,
  "auto_renew" boolean,
  "effective_date" timestamp,
  "expiration_date" timestamp,
  "settle_date" timestamp,
  "period_month" int,
  "creator_id" bigint,
  "create_date" timestamp,
  "first_invoice_date_as_effective_date" boolean,
  "is_first_contract" boolean,
  "is_monopoly" boolean,
  "allow_partial_billing" boolean,
  "installation_date" timestamp,
  "renew_package_id" bigint,
  "status" varchar(50),
  "remark" varchar(20)
);

CREATE TABLE "contract_history" (
  "contract_history_id" bigserial PRIMARY KEY,
  "contract_id" bigserial,
  "name" varchar(50),
  "company_id" bigint,
  "auto_renew" boolean,
  "effective_date" timestamp,
  "expiration_date" timestamp,
  "settle_date" timestamp,
  "period_month" int,
  "creator_id" bigint,
  "create_date" timestamp,
  "first_invoice_date_as_effective_date" boolean,
  "is_first_contract" boolean,
  "is_monopoly" boolean,
  "allow_partial_billing" boolean,
  "installation_date" timestamp,
  "renew_package_id" bigint,
  "status" varchar(50),
  "remark" varchar(20)
);

CREATE TABLE "charge_package" (
  "package_id" bigserial PRIMARY KEY,
  "parent_id" bigint,
  "name" varchar(30),
  "enabled" boolean,
  "creator_id" bigint,
  "create_date" timestamp,
  "modifier_id" bigint,
  "modify_date" timestamp
);

CREATE TABLE "product" (
  "product_id" bigserial PRIMARY KEY,
  "product_name" varchar(40),
  "product_source_id" bigint,
  "product_category_id" bigint,
  "product_type" varchar(30),
  "product_status" varchar(30),
  "product_pricing_id" bigint,
  "product_discount_id" bigint,
  "creator_id" bigint,
  "create_date" timestamp,
  "modifier_id" bigint,
  "modify_date" timestamp
);

CREATE TABLE "product_source" (
  "product_source_id" bigserial PRIMARY KEY
);

CREATE TABLE "package_ref" (
  "package_ref_id" bigserial PRIMARY KEY,
  "to_product_id" bigint,
  "to_product_quantity" integer,
  "to_charge_mode_id" bigint,
  "from_package_id" bigint
);

CREATE TABLE "charge_mode" (
  "charge_mode_id" bigserial PRIMARY KEY,
  "name" varchar(30),
  "enabled" boolean,
  "paid_plan" varchar(30),
  "charge_plan" varchar(30),
  "product_category_id" bigint,
  "grade_id" bigint,
  "charge_base_type" varchar(30),
  "charge_cycle_type" varchar(30),
  "calculate_cycle_type" varchar(30),
  "maximum_charge" decimal(23,4),
  "circulation" boolean,
  "accumulation" boolean,
  "creator_id" bigint,
  "create_date" timestamp,
  "modifier_id" bigint,
  "modify_date" timestamp
);

CREATE TABLE "product_category" (
  "product_category_id" bigserial PRIMARY KEY,
  "category_name" varchar(30),
  "parent_category_id" bigint
);

CREATE TABLE "product_purchase" (
  "product_purchase_id" bigserial PRIMARY KEY,
  "company_id" bigint,
  "product_id" bigint,
  "sales_price" decimal(23,4),
  "purchase_date" timestamp,
  "purchase_quantity" integer
);

CREATE TABLE "product_pricing" (
  "product_pricing_id" bigserial PRIMARY KEY,
  "base_price" decimal(23,4),
  "create_date" timestamp,
  "in_active" boolean
);

CREATE TABLE "product_discount" (
  "product_discount_id" bigserial PRIMARY KEY,
  "discount_value" integer,
  "discount_unit" varchar(10),
  "create_date" timestamp,
  "valid_from" timestamp,
  "valid_until" timestamp,
  "maximum_discount_amount" decimal(23,4)
);

CREATE TABLE "billing_item" (
  "billing_item_id" bigserial PRIMARY KEY,
  "company_id" bigint,
  "deduct_id" bigint,
  "product_category_id" bigint,
  "product_purchase_id" bigint,
  "contract_id" bigint,
  "package_ref_id" bigint,
  "expected_out_date" timestamp,
  "calculate_from_date" timestamp,
  "calculate_to_date" timestamp,
  "count" integer,
  "billing_item_type" varchar(30),
  "paid_plan" varchar(30),
  "charge_plan" varchar(30),
  "bill_id" bigint,
  "tax_rate" decimal(8,4),
  "tax_amount" decimal(23,4),
  "tax_excluded_amount" decimal(23,4),
  "tax_included_amount" decimal(23,4),
  "is_memo" boolean DEFAULT false,
  "create_date" timestamp,
  "remark" varchar(40)
);

CREATE TABLE "deduct" (
  "deduct_id" bigserial PRIMARY KEY,
  "company_id" bigint,
  "contract_id" bigint,
  "product_id" bigint,
  "package_ref_id" bigint,
  "charge_mode_id" bigint,
  "product_category_id" bigint,
  "target_product_category_id" bigint,
  "deduct_type" varchar(30),
  "deduct_status" varchar(30),
  "charge_base_type" varchar(30),
  "quota" decimal(23,4),
  "deduct_by_fee" boolean,
  "sales_price" decimal(23,4),
  "effective_date" timestamp,
  "expiration_date" timestamp,
  "creator_id" bigint,
  "create_date" timestamp DEFAULT (now()),
  "modifier_id" bigint,
  "modify_date" timestamp
);

CREATE TABLE "new_grade" (
  "grade_id" bigserial PRIMARY KEY,
  "name" varchar(40),
  "root_id" bigint,
  "level" integer,
  "cnt_start" integer,
  "cnt_end" integer,
  "enabled" boolean,
  "unit_price" decimal(12,4),
  "fix_price" decimal(23,4),
  "creator_id" bigint,
  "create_date" timestamp,
  "modifier_id" bigint,
  "modify_date" timestamp
);

CREATE TABLE "bill" (
  "bill_id" bigserial PRIMARY KEY,
  "company_id" bigint,
  "tax_rate" decimal(4,3),
  "tax_amount" decimal(13,4),
  "tw_tax" boolean,
  "tax_excluded_amount" decimal(23,4),
  "tax_included_amount" decimal(23,4),
  "bill_status" varchar(30),
  "bill_remark" varchar(200),
  "bill_ym" varchar(6),
  "payment_remark" varchar(200),
  "bank_code" varchar(20),
  "expected_entry_date" Timestamp,
  "out_to_bank_date" Timestamp,
  "paid_date" Timestamp,
  "actual_received" Integer,
  "payment_method" varchar(30),
  "is_memo" boolean,
  "creator_id" bigint,
  "create_date" Timestamp
);

CREATE TABLE "deduct_history" (
  "deduct_history_id" bigserial PRIMARY KEY,
  "deduct_id" bigint,
  "bill_id" bigint,
  "deduct_billing_item_id" bigint,
  "amount" decimal(23,4),
  "create_date" Timestamp
);

CREATE TABLE "report" (
  "report_id" char(36) PRIMARY KEY,
  "report_type" varchar(30),
  "create_date" Timestamp
);

CREATE TABLE "notice" (
  "notice_id" bigserial PRIMARY KEY,
  "notice_type" varchar(30),
  "company_id" bigint,
  "bill_id" bigint,
  "contract_id" bigint,
  "request_by_system" boolean,
  "notice_status" varchar(30),
  "recipient" varchar(50),
  "creator_id" bigint,
  "create_date" Timestamp,
  "modifier_id" bigint,
  "modify_date" Timestamp
);

CREATE TABLE "bill_report_relation" (
  "bill_report_relation_id" bigserial,
  "bill_id" bigint,
  "report_id" char(36)
);

CREATE TABLE "financial_transaction" (
  "transaction_id" bigserial PRIMARY KEY,
  "bill_id" bigint,
  "tax_included_amount" decimal(23,4),
  "transacction_remark" varchar(100),
  "transaction_date" Timestamp,
  "creator_id" bigint
);

ALTER TABLE "contract" ADD FOREIGN KEY ("company_id") REFERENCES "company" ("company_id");

ALTER TABLE "contract" ADD FOREIGN KEY ("package_id") REFERENCES "charge_package" ("package_id");

ALTER TABLE "contract_history" ADD FOREIGN KEY ("company_id") REFERENCES "company" ("company_id");

ALTER TABLE "product" ADD FOREIGN KEY ("product_source_id") REFERENCES "product_source" ("product_source_id");

ALTER TABLE "product" ADD FOREIGN KEY ("product_category_id") REFERENCES "product_category" ("product_category_id");

ALTER TABLE "product" ADD FOREIGN KEY ("product_pricing_id") REFERENCES "product_pricing" ("product_pricing_id");

ALTER TABLE "product" ADD FOREIGN KEY ("product_discount_id") REFERENCES "product_discount" ("product_discount_id");

ALTER TABLE "package_ref" ADD FOREIGN KEY ("to_product_id") REFERENCES "product" ("product_id");

ALTER TABLE "package_ref" ADD FOREIGN KEY ("to_charge_mode_id") REFERENCES "charge_mode" ("charge_mode_id");

ALTER TABLE "package_ref" ADD FOREIGN KEY ("from_package_id") REFERENCES "charge_package" ("package_id");

ALTER TABLE "charge_mode" ADD FOREIGN KEY ("product_category_id") REFERENCES "product_category" ("product_category_id");

ALTER TABLE "charge_mode" ADD FOREIGN KEY ("grade_id") REFERENCES "new_grade" ("grade_id");

ALTER TABLE "product_category" ADD FOREIGN KEY ("parent_category_id") REFERENCES "product_category" ("product_category_id");

ALTER TABLE "product_purchase" ADD FOREIGN KEY ("company_id") REFERENCES "company" ("company_id");

ALTER TABLE "product_purchase" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE "billing_item" ADD FOREIGN KEY ("contract_id") REFERENCES "contract" ("contract_id");

ALTER TABLE "billing_item" ADD FOREIGN KEY ("package_ref_id") REFERENCES "package_ref" ("package_ref_id");

ALTER TABLE "billing_item" ADD FOREIGN KEY ("bill_id") REFERENCES "bill" ("bill_id");

ALTER TABLE "deduct" ADD FOREIGN KEY ("company_id") REFERENCES "company" ("company_id");

ALTER TABLE "deduct" ADD FOREIGN KEY ("contract_id") REFERENCES "contract" ("contract_id");

ALTER TABLE "deduct" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE "deduct" ADD FOREIGN KEY ("package_ref_id") REFERENCES "package_ref" ("package_ref_id");

ALTER TABLE "deduct" ADD FOREIGN KEY ("charge_mode_id") REFERENCES "charge_mode" ("charge_mode_id");

ALTER TABLE "deduct" ADD FOREIGN KEY ("product_category_id") REFERENCES "product_category" ("product_category_id");

ALTER TABLE "new_grade" ADD FOREIGN KEY ("root_id") REFERENCES "new_grade" ("grade_id");

ALTER TABLE "bill" ADD FOREIGN KEY ("company_id") REFERENCES "company" ("company_id");

ALTER TABLE "deduct_history" ADD FOREIGN KEY ("deduct_id") REFERENCES "deduct" ("deduct_id");

ALTER TABLE "deduct_history" ADD FOREIGN KEY ("bill_id") REFERENCES "bill" ("bill_id");

ALTER TABLE "notice" ADD FOREIGN KEY ("company_id") REFERENCES "company" ("company_id");

ALTER TABLE "notice" ADD FOREIGN KEY ("contract_id") REFERENCES "contract" ("contract_id");

ALTER TABLE "bill_report_relation" ADD FOREIGN KEY ("report_id") REFERENCES "report" ("report_id");

ALTER TABLE "financial_transaction" ADD FOREIGN KEY ("bill_id") REFERENCES "bill" ("bill_id");

COMMENT ON COLUMN "contract"."status" IS '
    C: Created
    , E: Enabled
    , B: PrepayBilling
    , T: Terminated
    , S: Suspended
    , D: Disabled
  ';

COMMENT ON COLUMN "contract_history"."status" IS '
    C: Created
    , E: Enabled
    , B: PrepayBilling
    , T: Terminated
    , S: Suspended
    , D: Disabled
  ';

COMMENT ON COLUMN "product"."product_type" IS '
    SERVICE(服務)
    , PRODUCT(商品)
  ';

COMMENT ON COLUMN "product"."product_status" IS '
    E:Enabled
    , D:Disabled
  ';

COMMENT ON TABLE "product_source" IS '保留日後與實體商品接的空間';

COMMENT ON COLUMN "charge_mode"."paid_plan" IS '
    POST_PAID(後繳)
    , PRE_PAID(預繳)
  ';

COMMENT ON COLUMN "charge_mode"."charge_plan" IS '
    INITIATION(初始化)
    , PERIODIC(週期)
    , TERMINATION(終止)
    , SUSPENSION(暫停)
    , SUSPENSION_PERIODIC(暫停的區間)
    , RE_ACTIVATION(重新啟用)
    , USAGE(單次使用)
    , DEDUCT(扣抵)
  ';

COMMENT ON COLUMN "billing_item"."billing_item_type" IS '
    RENTAL(月租)
    , OVERAGE(超額)
    , MIGRATION(舊系統移轉)
    , DEDUCT(扣抵)
  ';

COMMENT ON COLUMN "billing_item"."paid_plan" IS '
    POST_PAID(後繳)
    , PRE_PAID(預繳)
  ';

COMMENT ON COLUMN "billing_item"."charge_plan" IS '
    INITIATION(初始化)
    , PERIODIC(週期)
    , TERMINATION(終止)
    , SUSPENSION(暫停)
    , SUSPENSION_PERIODIC(暫停的區間)
    , RE_ACTIVATION(重新啟用)
    , USAGE(單次使用)
    , DEDUCT(扣抵)
  ';

COMMENT ON COLUMN "deduct"."deduct_type" IS '
    DEDUCT(扣抵)
    , PREPAYMENT(預繳)
  ';

COMMENT ON COLUMN "deduct"."deduct_status" IS '
    C: Created
    ,B: Billing
    ,E: Enabled
    ,D: Disabled
  ';

COMMENT ON COLUMN "bill"."bill_status" IS '
    C:Created
    , P:Paid
  ';

COMMENT ON COLUMN "report"."report_type" IS '
    SCSB_CS_REPORT(上銀便利商店報表)
    , INVOICE_IMPORT_REPORT(發票資料匯入)
  ';

COMMENT ON COLUMN "notice"."notice_status" IS '
    WAIT_FOR_FIRST_SEND
    , FINISH
  ';
