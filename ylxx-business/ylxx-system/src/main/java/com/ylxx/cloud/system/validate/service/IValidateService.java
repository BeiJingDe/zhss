package com.ylxx.cloud.system.validate.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.validate.model.ValidateDTO;
import com.ylxx.cloud.system.validate.model.ValidateVO;

/**
 * 前后端一致校验规则表 服务类
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface IValidateService {

	Page<ValidateVO> selectPageVo(ValidateDTO validateDto);

	List<ValidateVO> selectVos(ValidateDTO validateDto);

	ValidateVO selectById(String id);

	void insert(ValidateVO validateVo);
	
	void insertBatch(List<ValidateVO> validateVos);

	void update(ValidateVO validateVo);

	void deleteBatchIds(List<String> ids);

	void refreshCache();

	void refreshCacheModel();

}
