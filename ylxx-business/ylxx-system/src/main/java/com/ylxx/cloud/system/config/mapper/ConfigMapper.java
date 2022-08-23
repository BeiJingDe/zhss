package com.ylxx.cloud.system.config.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.config.model.ConfigDO;
import com.ylxx.cloud.system.config.model.ConfigDTO;
import com.ylxx.cloud.system.config.model.ConfigVO;

/**
 * 系统参数 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface ConfigMapper extends BaseMapper<ConfigDO> {
	
	Page<ConfigVO> selectPageVo(Page<ConfigDTO> page, @Param("param") ConfigDTO configDto);

	List<ConfigVO> selectPageVo(@Param("param") ConfigDTO configDto);

	void insertBatch(List<ConfigVO> configVos);

}
