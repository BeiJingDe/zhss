/**
* 10:44 2022/2/11
**/
ALTER TABLE `platform_page_element` ADD `extra_key_word` VARCHAR ( 128 ) DEFAULT NULL COMMENT '语音关键词' AFTER `extra_front`;

/**
* 14:38 2022/2/19
**/
ALTER TABLE `platform_report` ADD `is_template` TINYINT ( 1 ) DEFAULT '0' COMMENT '是否为模板，1 发布，0 未发布' after is_enable;

CREATE TABLE `platform_report_auto_cfg` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`report_id` BIGINT NOT NULL COMMENT '报告id',
	`auto_cfg` VARCHAR ( 64 ) COMMENT '配置',
	`is_enable` TINYINT ( 1 ) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY ( `id` ) USING BTREE,
	KEY `idx_report_id` ( `report_id` ) USING BTREE,
KEY `idx_is_enable` ( `is_enable` ) USING BTREE 
) ENGINE = INNODB AUTO_INCREMENT = 0 DEFAULT CHARSET = utf8 ROW_FORMAT = DYNAMIC;

/**
* 11:00 2022/2/26
**/
ALTER TABLE `platform_report` ADD `is_auto` TINYINT ( 1 ) DEFAULT NULL COMMENT '报告自动生成开关' AFTER `is_template`;


CREATE TABLE `platform_point_praise` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `report_id` bigint DEFAULT NULL,
  `user_name` varchar(42)  DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT NULL,
  `point_time` datetime DEFAULT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ;

CREATE TABLE `platform_report_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `report_id` bigint DEFAULT NULL,
  `user_name` varchar(42)  DEFAULT NULL,
  `content` varchar(128) CHARACTER SET utf8mb4  DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

/**
*  2022/2/28
**/
CREATE TABLE `platform_report_pageview` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `report_id` bigint NOT NULL COMMENT '报告id',
  `views` bigint NOT NULL DEFAULT '0' COMMENT '点击量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_report_id` (`report_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `platform_home_hot_report` (
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报告名称',
  `report_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `views` int NOT NULL COMMENT '点击量',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/**
*  2022/3/2
**/
CREATE TABLE `platform_msg_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `msg_title` varchar(128) NOT NULL COMMENT '消息标题',
  `msg_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型',
  `msg_content` varchar(128) DEFAULT NULL COMMENT '消息内容',
  `msg_send_date` datetime DEFAULT NULL COMMENT '消息发送时间',
  `msg_receive_date` datetime DEFAULT NULL COMMENT '消息接收时间',
  `sender` varchar(64) DEFAULT 'auto' COMMENT '发送着',
  `receiver` varchar(64) NOT NULL COMMENT '接收者',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读，1 已读，0 未读',
  `is_enable` tinyint(1) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_userid` (`receiver`) USING BTREE,
  KEY `idx_is_enable` (`is_enable`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/**
*  2022/3/4
**/
CREATE TABLE `t_base_rel_role_home_page_del` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `board_name` varchar(128) NOT NULL COMMENT '看板名称',
  `code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `user_name` varchar(64) NOT NULL COMMENT '用户名',
  `is_enable` tinyint(1) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_is_enable` (`is_enable`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/**
*  2022/3/4
**/
ALTER TABLE `platform_index_system` ADD COLUMN `describe` text COMMENT '定义' AFTER index_type;

CREATE TABLE `platform_user_follow_info` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`follow_type` VARCHAR ( 64 ) NOT NULL COMMENT '关注类型：指标 index，维度 dim, 维度值 dim_value',
	`content` VARCHAR ( 64 ) DEFAULT NULL COMMENT '关注内容',
	`user_name` VARCHAR ( 64 ) DEFAULT NULL COMMENT '用户名',
	`is_enable` TINYINT ( 1 ) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY ( `id` ) USING BTREE,
	KEY `idx_follow_type` ( `follow_type` ),
	KEY `idx_user_name` ( `user_name` ),
KEY `is_enable` ( `is_enable` ) 
) ENGINE = INNODB AUTO_INCREMENT = 0 DEFAULT CHARSET = utf8 COMMENT = '用户关注信息';

CREATE TABLE `platform_index_dim_rel` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`index_id` VARCHAR ( 64 ) NOT NULL COMMENT '指标ID',
	`type` VARCHAR ( 64 ) NOT NULL COMMENT '类型：维度 dim, 维度值 dim_value',
	`content_id` VARCHAR ( 64 ) DEFAULT NULL COMMENT '内容id',
	`content_name` VARCHAR ( 64 ) DEFAULT NULL COMMENT '内容名称',
	`is_enable` TINYINT ( 1 ) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY ( `id` ) USING BTREE,
	KEY `idx_type` ( `type` ),
	KEY `idx_index_id` ( `index_id` ),
KEY `is_enable` ( `is_enable` ) 
) ENGINE = INNODB AUTO_INCREMENT = 0 DEFAULT CHARSET = utf8 COMMENT = '指标维度关系信息';
