package com.ylxx.cloud.upms.rel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.rel.model.RelRoleMenuDO;
import com.ylxx.cloud.upms.rel.model.RelRoleMenuDTO;
import com.ylxx.cloud.upms.rel.model.RelRoleMenuVO;

/**
 * 角色和菜单（按钮）关联表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-09-21
 */
public interface RelRoleMenuMapper extends BaseMapper<RelRoleMenuDO> {
	
	Page<RelRoleMenuVO> selectPageVo(Page<RelRoleMenuDTO> page, @Param("param") RelRoleMenuDTO relRoleMenuDto);

	List<RelRoleMenuVO> selectPageVo(@Param("param") RelRoleMenuDTO relRoleMenuDto);

	void insertBatch(List<RelRoleMenuVO> relRoleMenuVos);

}
