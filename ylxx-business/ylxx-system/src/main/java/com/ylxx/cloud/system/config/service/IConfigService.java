package com.ylxx.cloud.system.config.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.config.model.ConfigDTO;
import com.ylxx.cloud.system.config.model.ConfigVO;

/**
 * 系统参数 服务类
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface IConfigService {

	Page<ConfigVO> selectPageVo(ConfigDTO configDto);

	List<ConfigVO> selectVos(ConfigDTO configDto);

	ConfigVO selectById(String id);

	void insert(ConfigVO configVo);
	
	void insertBatch(List<ConfigVO> configVos);

	void update(ConfigVO configVo);

	void deleteBatchIds(List<String> ids);

	void refreshCache();

}
