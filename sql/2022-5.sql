CREATE TABLE `platform_notice` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
	`notice_title` VARCHAR ( 42 )  NOT NULL COMMENT '公告标题',
	`notice_brief` VARCHAR ( 50 ) NOT NULL COMMENT '公告简要',
	`notice_content` VARCHAR ( 2000 ) NULL COMMENT '公告内容',
  `is_enable` tinyint(1) DEFAULT '1' COMMENT '是否可用，1 可用，0 不可用',
	`create_by` VARCHAR ( 64 ) NULL COMMENT '创建者',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_by` VARCHAR ( 64 ) NULL COMMENT '更新者',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY ( `id` ) 
) ENGINE = INNODB COMMENT '通知公告表';


ALTER TABLE `platform_meeting_info` ADD `meeting_level` VARCHAR ( 64 ) DEFAULT NULL COMMENT '会议级别';

DROP TABLE IF EXISTS `platform_index_rank`;
CREATE TABLE `platform_index_rank`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指标主键',
  `index_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '指标名称',
  `index_sub_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '衍生指标',
  `stat_date` date NULL DEFAULT NULL COMMENT '统计时间',
  `org_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  `val` double(20, 2) NULL DEFAULT NULL COMMENT '指标值',
  `rank` int(0) NULL DEFAULT NULL COMMENT '排名',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型,地市排名,同业排名',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_index`(`index_id`, `index_sub_name`, `stat_date`, `org_name`, `type`) USING BTREE,
  INDEX `idx_index_ide`(`index_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `platform_index_rank_static`;
CREATE TABLE `platform_index_rank_static`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型,地市排名,同业排名',
  `index_sub_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '衍生指标',
  `stat_date` date NULL DEFAULT NULL COMMENT '统计时间',
  `score` decimal(10, 2) NULL DEFAULT NULL COMMENT '评分',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_index`(`org_name`, `type`, `index_sub_name`, `stat_date`) USING BTREE,
  INDEX `idx_stat_date`(`stat_date`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;