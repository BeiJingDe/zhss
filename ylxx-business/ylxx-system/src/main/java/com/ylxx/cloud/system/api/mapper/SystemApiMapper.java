package com.ylxx.cloud.system.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.api.model.SystemApiDO;
import com.ylxx.cloud.system.api.model.SystemApiDTO;
import com.ylxx.cloud.system.api.model.SystemApiVO;

/**
 * 系统API接口管理表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
public interface SystemApiMapper{
	
	Page<SystemApiVO> selectPageVo(Page<SystemApiDTO> page, @Param("param") SystemApiDTO systemApiDto);

	List<SystemApiVO> selectPageVo(@Param("param") SystemApiDTO systemApiDto);

	void createRoleApiRel();
}
