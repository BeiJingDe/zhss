package com.ylxx.cloud.system.dict.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.dict.model.DictDO;
import com.ylxx.cloud.system.dict.model.DictDTO;
import com.ylxx.cloud.system.dict.model.DictVO;

/**
 * 数据字典 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface DictMapper extends BaseMapper<DictDO> {
	
	Page<DictVO> selectPageVo(Page<DictDTO> page, @Param("param") DictDTO dictDto);

	List<DictVO> selectPageVo(@Param("param") DictDTO dictDto);

	void insertBatch(List<DictVO> dictVos);

}
