package com.ylxx.cloud.system.log.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.log.model.SystemLogDTO;
import com.ylxx.cloud.system.log.model.SystemLogVO;

/**
 * 系统日志 服务类
 *
 * @author caixiaopeng
 * @since 2020-07-03
 */
public interface ISystemLogService {

	Page<SystemLogVO> selectPageVo(SystemLogDTO systemLogDto);

	List<SystemLogVO> selectVos(SystemLogDTO systemLogDto);

	SystemLogVO selectById(String id);

	void insert(SystemLogVO systemLogVo);
	
	//void insertBatch(List<SystemLogVO> systemLogVos);

	void update(SystemLogVO systemLogVo);

	void deleteBatchIds(List<String> ids);

    void backup();

	void recovery();
}
