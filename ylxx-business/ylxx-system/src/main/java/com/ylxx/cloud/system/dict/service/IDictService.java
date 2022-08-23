package com.ylxx.cloud.system.dict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.dict.model.DictDTO;
import com.ylxx.cloud.system.dict.model.DictVO;

import java.util.List;

/**
 * 数据字典 服务类
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface IDictService {

	Page<DictVO> selectPageVo(DictDTO dictDto);

	List<DictVO> selectVos(DictDTO dictDto);

	DictVO selectById(String id);

	void insert(DictVO dictVo);
	
	void insertBatch(List<DictVO> dictVos);

	void update(DictVO dictVo);

	void deleteBatchIds(List<String> ids);

	void deleteByParentId(String parentId);

	void refreshCache();

	boolean checkUnique(String code, String dictType, String id, String parentId);

}
