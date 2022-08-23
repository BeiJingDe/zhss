package com.ylxx.cloud.system.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.api.model.RoleApiDTO;
import com.ylxx.cloud.system.api.model.RoleApiVO;

/**
 * 角色API接口权限表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
public interface RoleApiMapper {
	
	Page<RoleApiVO> selectPageVo(Page<RoleApiDTO> page, @Param("param") RoleApiDTO roleApiDto);

	List<RoleApiVO> selectPageVo(@Param("param") RoleApiDTO roleApiDto);

}
