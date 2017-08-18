-- Table: charge_mode_grade

-- DROP TABLE charge_mode_grade;

CREATE TABLE charge_mode_grade
(
  charge_id integer NOT NULL DEFAULT nextval('charge_mode_grade_charge_mode_grade_id_seq'::regclass), -- 流水號
  package_name character varying(50), -- 專案名稱
  charge_cycle integer, -- 計費周期。分為足月跟破月
  status integer, -- 1.生效中 2.未生效 3.結束4.暫停。
  free_month integer, -- 預付免費月份
  contract_limit integer, -- 限制綁約月份
  fee_period integer, -- 月租收費區間1.年繳2.季繳3.月繳
  base_quantity integer, -- 基本使用量
  sales_price integer, -- 銷售價格
  has_grade character varying, -- 是否有級距表 Y/N
  grade_price integer DEFAULT 0, -- 級距區間費用(是否有級距表=Y時)
  grade_cnt integer DEFAULT 0, -- 級距區間張數(是否有級距表=Y時)
  creator_id integer,
  create_date timestamp(6) without time zone,
  modifier_id integer,
  modify_date timestamp(6) without time zone,
  pre_payment integer, -- 預付金額
  CONSTRAINT charge_mode_grade_pkey PRIMARY KEY (charge_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE charge_mode_grade
  OWNER TO postgres;
COMMENT ON COLUMN charge_mode_grade.charge_id IS '流水號';
COMMENT ON COLUMN charge_mode_grade.package_name IS '專案名稱';
COMMENT ON COLUMN charge_mode_grade.charge_cycle IS '計費周期。分為足月跟破月';
COMMENT ON COLUMN charge_mode_grade.status IS '1.生效中 2.未生效 3.結束4.暫停。';
COMMENT ON COLUMN charge_mode_grade.free_month IS '預付免費月份';
COMMENT ON COLUMN charge_mode_grade.contract_limit IS '限制綁約月份';
COMMENT ON COLUMN charge_mode_grade.fee_period IS '月租收費區間1.年繳2.季繳3.月繳';
COMMENT ON COLUMN charge_mode_grade.base_quantity IS '基本使用量';
COMMENT ON COLUMN charge_mode_grade.sales_price IS '銷售價格';
COMMENT ON COLUMN charge_mode_grade.has_grade IS '是否有級距表 Y/N';
COMMENT ON COLUMN charge_mode_grade.grade_price IS '級距區間費用(是否有級距表=Y時)';
COMMENT ON COLUMN charge_mode_grade.grade_cnt IS '級距區間張數(是否有級距表=Y時)';
COMMENT ON COLUMN charge_mode_grade.pre_payment IS '預付金額';


-- Table: grade

-- DROP TABLE grade;

CREATE TABLE grade
(
  grade_id serial NOT NULL, -- 流水號
  charge_id integer, -- 級距型方案id
  cnt_start integer, -- 張數起
  cnt_end integer, -- 張數迄
  price integer, -- 價格
  creator_id integer,
  create_date timestamp(6) without time zone,
  modifier_id integer,
  modify_date timestamp(6) without time zone,
  CONSTRAINT grade_pkey PRIMARY KEY (grade_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE grade
  OWNER TO postgres;
COMMENT ON COLUMN grade.grade_id IS '流水號';
COMMENT ON COLUMN grade.charge_id IS '級距型方案id';
COMMENT ON COLUMN grade.cnt_start IS '張數起';
COMMENT ON COLUMN grade.cnt_end IS '張數迄';
COMMENT ON COLUMN grade.price IS '價格';


-- Table: deduct_detail

-- DROP TABLE deduct_detail;

CREATE TABLE deduct_detail
(
  deduct_detail_id serial NOT NULL, -- 扣抵副表ID
  prepay_deduct_master_id integer,
  cash_detail_id integer,
  company_id integer,
  cal_ym character varying(6), -- 計算年月
  deduct_type integer, -- 扣抵類型 1.月租 2.月租超額 6.扣抵(和prepay_deduct_master的is_enable_over....等欄位相對應)
  money integer, -- 扣款金額
  creator_id integer,
  create_date timestamp(6) without time zone,
  modifier_id integer,
  modify_date timestamp(6) without time zone,
  CONSTRAINT deduct_detail_pkey PRIMARY KEY (deduct_detail_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE deduct_detail
  OWNER TO gateweb;
COMMENT ON TABLE deduct_detail
  IS '扣抵副表';
COMMENT ON COLUMN deduct_detail.deduct_detail_id IS '扣抵副表ID';
COMMENT ON COLUMN deduct_detail.cal_ym IS '計算年月';
COMMENT ON COLUMN deduct_detail.deduct_type IS '扣抵類型 1.月租 2.月租超額 6.扣抵(和prepay_deduct_master的is_enable_over....等欄位相對應)';
COMMENT ON COLUMN deduct_detail.money IS '扣款金額';


-- Table: prepay_deduct_master

-- DROP TABLE prepay_deduct_master;

CREATE TABLE prepay_deduct_master
(
  prepay_deduct_master_id serial NOT NULL, -- 預繳扣抵主表ID
  company_id integer, -- 公司ID
  amount integer, -- 總預用金
  is_enable_over character(1), -- 是否啟用超額預繳扣抵
  creator_id integer,
  create_date timestamp(6) without time zone,
  modifier_id integer,
  modify_date timestamp(6) without time zone,
  CONSTRAINT prepay_deduct_master_pkey PRIMARY KEY (prepay_deduct_master_id),
  CONSTRAINT prepay_deduct_master_company_id_key UNIQUE (company_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE prepay_deduct_master
  OWNER TO gateweb;
COMMENT ON TABLE prepay_deduct_master
  IS '預繳扣抵主表';
COMMENT ON COLUMN prepay_deduct_master.prepay_deduct_master_id IS '預繳扣抵主表ID';
COMMENT ON COLUMN prepay_deduct_master.company_id IS '公司ID';
COMMENT ON COLUMN prepay_deduct_master.amount IS '總預用金';
COMMENT ON COLUMN prepay_deduct_master.is_enable_over IS '是否啟用超額預繳扣抵';


-- Table: prepay_detail

-- DROP TABLE prepay_detail;

CREATE TABLE prepay_detail
(
  prepay_detail_id serial NOT NULL, -- 預繳副表ID
  prepay_deduct_master_id integer,
  cash_detail_id integer, -- 找到cash_detail，看這筆是否已繳款(註:入帳時，才會把錢加到prepay_deduct_master)
  company_id integer,
  cal_ym character varying(6),
  money integer, -- 預繳金額
  creator_id integer,
  create_date timestamp(6) without time zone,
  modifier_id integer,
  modify_date timestamp(6) without time zone,
  CONSTRAINT prepay_detail_pkey PRIMARY KEY (prepay_detail_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE prepay_detail
  OWNER TO gateweb;
COMMENT ON TABLE prepay_detail
  IS '預繳副表';
COMMENT ON COLUMN prepay_detail.prepay_detail_id IS '預繳副表ID';
COMMENT ON COLUMN prepay_detail.cash_detail_id IS '找到cash_detail，看這筆是否已繳款(註:入帳時，才會把錢加到prepay_deduct_master)';
COMMENT ON COLUMN prepay_detail.money IS '預繳金額';

