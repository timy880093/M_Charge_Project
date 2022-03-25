ALTER TABLE public.package_ref DROP CONSTRAINT package_ref_to_charge_mode_id_fkey;
ALTER TABLE public.deduct DROP CONSTRAINT deduct_charge_mode_id_fkey;
ALTER TABLE public.charge_mode DROP CONSTRAINT charge_mode_pkey;
ALTER TABLE public.charge_mode DROP CONSTRAINT charge_mode_grade_id_fkey;
ALTER TABLE public.charge_mode DROP CONSTRAINT charge_mode_product_category_id_fkey;

ALTER TABLE IF EXISTS charge_mode
RENAME charge_mode_id TO charge_rule_id;

ALTER TABLE IF EXISTS charge_mode
RENAME TO charge_rule;

ALTER TABLE IF EXISTS deduct
    RENAME charge_mode_id TO charge_rule_id;

ALTER TABLE IF EXISTS package_ref
    RENAME to_charge_mode_id TO to_charge_rule_id;

ALTER TABLE public.charge_rule ADD PRIMARY KEY (charge_rule_id);
ALTER TABLE public.charge_rule ADD CONSTRAINT charge_rule_grade_id_fkey FOREIGN KEY (grade_id) REFERENCES new_grade(grade_id);
ALTER TABLE public.charge_rule ADD CONSTRAINT charge_rule_product_category_id_fkey FOREIGN KEY (product_category_id) REFERENCES product_category(product_category_id);

ALTER TABLE public.deduct ADD CONSTRAINT deduct_charge_rule_id_fkey FOREIGN KEY (charge_rule_id) REFERENCES charge_rule(charge_rule_id);
ALTER TABLE public.package_ref ADD CONSTRAINT package_ref_to_charge_rule_id_fkey FOREIGN KEY (to_charge_rule_id) REFERENCES charge_rule(charge_rule_id);

ALTER SEQUENCE charge_mode_charge_mode_id_seq RENAME TO charge_rule_charge_rule_id_seq;


