CREATE TABLE `method_lock` (
	`id` INT ( 20 ) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`method_name` VARCHAR ( 64 ) NOT NULL DEFAULT '' COMMENT '锁定的方法名',
	`method_desc` VARCHAR ( 1024 ) NOT NULL DEFAULT '备注信息',
	`update_time` datetime NOT NULL COMMENT '保存数据时间',
	PRIMARY KEY ( `id` ),
UNIQUE KEY `uidx_method_name` ( `method_name` ) USING BTREE 
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '锁定中的方法';