package com.ylxx.cloud.upms.role.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.role.model.RoleDO;
import com.ylxx.cloud.upms.role.model.RoleDTO;
import com.ylxx.cloud.upms.role.model.RoleVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 角色信息表 服务类
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
public interface IRoleService {

	Page<RoleVO> selectPageVo(RoleDTO roleDto);

	List<RoleVO> selectVos(RoleDTO roleDto);

	RoleVO selectById(String id);

	void insert(RoleVO roleVo);
	
	void insertBatch(List<RoleVO> roleVos);

	void update(RoleVO roleVo);

	void deleteBatchIds(List<String> ids);

    List<RoleVO> selectRoleVos(String username);

    void changeRoleMenu(String roleCode, List<String> menuCodes);

	RoleVO selectRole(String roleCode);

	List<RoleDO> selectByIds(List<String> ids);
}
