/**
*  2022/4/10
**/
CREATE TABLE `t_search_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sentence` varchar(255)  NOT NULL COMMENT '原始语句',
  `index` varchar(255)  NOT NULL COMMENT '指标信息',
  `dimension` varchar(255)  NOT NULL COMMENT '维度信息',
  `user_name` varchar(64)  DEFAULT NULL COMMENT '用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_search_static` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(255)  NOT NULL COMMENT '类型：index，dim',
  `content` varchar(255)  NOT NULL COMMENT '维度、指标内容',
  `count` varchar(255)  NOT NULL COMMENT '统计数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

alter table  `platform_index_system` add COLUMN   `is_key` tinyint(1) DEFAULT '0' COMMENT '是否为关键业绩指标，1 是，0 不是' after index_type;
alter table  `platform_index_system` add COLUMN   `is_prov` tinyint(1) DEFAULT '0' COMMENT '是否为同业对标指标，1 是，0 不是' after is_key;
alter table  `platform_index_system` add COLUMN   `is_greater` tinyint(1) DEFAULT '1' COMMENT '是否为正向排名，1 是，0 不是' after is_prov;