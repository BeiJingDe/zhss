ALTER TABLE `zhss_electricity_item_result` ADD COLUMN `jh_val_a` DOUBLE ( 20, 2 ) DEFAULT NULL COMMENT '稽核指标值A' AFTER val;
ALTER TABLE `zhss_electricity_item_result` ADD COLUMN `jh_val_a_name` VARCHAR ( 64 ) NOT NULL COMMENT '稽核指标名称A' AFTER jh_val_a;
ALTER TABLE `zhss_electricity_item_result` ADD COLUMN `jh_val_b` DOUBLE ( 20, 2 ) DEFAULT NULL COMMENT '稽核指标值B' AFTER jh_val_a_name;
ALTER TABLE `zhss_electricity_item_result` ADD COLUMN `jh_val_b_name` VARCHAR ( 64 ) NOT NULL COMMENT '稽核指标名称B' AFTER jh_val_b;

ALTER TABLE `zhss_electricity_item_result` ADD COLUMN  `is_change` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未修改，1 修改' AFTER jh_val_b_name;
