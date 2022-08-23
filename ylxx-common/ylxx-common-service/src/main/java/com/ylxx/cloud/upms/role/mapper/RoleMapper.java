package com.ylxx.cloud.upms.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.role.model.RoleDO;
import com.ylxx.cloud.upms.role.model.RoleDTO;
import com.ylxx.cloud.upms.role.model.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色信息表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
public interface RoleMapper extends BaseMapper<RoleDO> {
	
	Page<RoleVO> selectPageVo(Page<RoleDTO> page, @Param("param") RoleDTO roleDto);

	List<RoleVO> selectPageVo(@Param("param") RoleDTO roleDto);

	void insertBatch(List<RoleVO> roleVos);

}
