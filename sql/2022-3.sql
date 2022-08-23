/**
*  2022/3/10
**/
CREATE TABLE `qlyy_dimension_basic_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `wd_column` varchar(255) NOT NULL COMMENT '维度列',
  `wd_type` varchar(255) NOT NULL COMMENT '维度类型名称',
  `describe` text COMMENT '定义',
  PRIMARY KEY (`id`),
  KEY `wd_column` (`wd_column`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `platform_report_collection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `report_id` varchar(42) NOT NULL COMMENT '报告id',
  `user_name` varchar(42) NOT NULL COMMENT '用户名',
  `is_enable` tinyint(1) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `platform_report_share` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
	`report_id` VARCHAR ( 42 ) NOT NULL COMMENT '报告id',
	`src_user` VARCHAR ( 42 ) NOT NULL COMMENT '分享者',
	`dst_user` VARCHAR ( 42 ) NOT NULL COMMENT '被分享者',
	`is_enable` TINYINT ( 1 ) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY ( `id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;