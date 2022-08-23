package com.ylxx.cloud.upms.role.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.upms.menu.mapper.MenuMapper;
import com.ylxx.cloud.upms.menu.model.MenuDTO;
import com.ylxx.cloud.upms.menu.model.MenuVO;
import com.ylxx.cloud.upms.rel.mapper.RelRoleMenuMapper;
import com.ylxx.cloud.upms.rel.model.RelRoleMenuDO;
import com.ylxx.cloud.upms.rel.model.RelRoleMenuVO;
import com.ylxx.cloud.upms.rel.model.RelUserRoleDO;
import com.ylxx.cloud.upms.rel.model.RelUserRoleVO;
import com.ylxx.cloud.upms.user.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.role.model.RoleDO;
import com.ylxx.cloud.upms.role.model.RoleDTO;
import com.ylxx.cloud.upms.role.model.RoleVO;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.upms.role.mapper.RoleMapper;
import com.ylxx.cloud.upms.role.service.IRoleService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description 角色信息表 服务实现类
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {
	
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private MenuMapper menuMapper;
	@Resource
	private RelRoleMenuMapper relRoleMenuMapper;
	
	@Override
	public Page<RoleVO> selectPageVo(RoleDTO roleDto) {
		Page<RoleDTO> page = new Page<RoleDTO>(roleDto.getCurrent(), roleDto.getSize());
		if(StrUtil.isNotBlank(roleDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(roleDto.getOrders()));
		}
		return roleMapper.selectPageVo(page, roleDto);
	}

	@Override
	public List<RoleVO> selectVos(RoleDTO roleDto) {
		return roleMapper.selectPageVo(roleDto);
	}
	
	@Override
	public RoleVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			RoleDTO roleDto = new RoleDTO();
			roleDto.setId(id);
			List<RoleVO> vos = roleMapper.selectPageVo(roleDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(RoleVO roleVo) {
		roleVo.setId(IdUtil.fastSimpleUUID());
		roleVo.setCreateTime(DateUtil.date());
		roleVo.setUpdateTime(DateUtil.date());
		roleVo.setCreateBy(HttpServletUtil.getUsername());
		roleVo.setUpdateBy(HttpServletUtil.getUsername());

		roleMapper.insert(roleVo);
	}
	
	@Override
	public void insertBatch(List<RoleVO> roleVos) {
		if(ObjectUtil.isNotEmpty(roleVos)) {
			roleMapper.insertBatch(roleVos);
		}
	}

	@Override
	public void update(RoleVO roleVo) {
		roleVo.setUpdateTime(DateUtil.date());
		roleVo.setUpdateBy(HttpServletUtil.getUsername());

		roleMapper.updateById(roleVo);
	}
	
	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			roleMapper.deleteBatchIds(ids);
		}
	}

	@Override
	public List<RoleVO> selectRoleVos(String username) {
		if(StrUtil.isNotBlank(username)) {
			RoleDTO roleDto = new RoleDTO();
			roleDto.setCurrent(1);
			roleDto.setSize(SystemConsts.MAX_PAGE_SIZE);
			roleDto.setOrders("[{column:'T1.sort_no',asc:true}]");
			roleDto.setUsername(username);
			Page<RoleVO> result = this.selectPageVo(roleDto);
			return result.getRecords();
		}
		return null;
	}

	@Override
	public void changeRoleMenu(String roleCode, List<String> menuCodes) {
		if(StrUtil.isNotBlank(roleCode)) {
			RoleVO roleVo = this.selectRole(roleCode);
			if(ObjectUtil.isNull(roleVo)) {
				throw new ServiceException("修改的角色不存在");
			}
			LambdaUpdateWrapper<RelRoleMenuDO> updateWrapper = Wrappers.lambdaUpdate();
			updateWrapper.eq(RelRoleMenuDO::getRoleId, roleVo.getId());
			relRoleMenuMapper.delete(updateWrapper);

			if(ObjectUtil.isNotEmpty(menuCodes)) {
				MenuDTO menuDto = new MenuDTO();
				menuDto.setMenuCodes(menuCodes);
				List<MenuVO> menuVos = menuMapper.selectPageVo(menuDto);
				if(ObjectUtil.isNotNull(menuVos)) {
					List<RelRoleMenuVO> relRoleMenuVos = CollUtil.newArrayList();
					menuVos.forEach(item -> {
						RelRoleMenuVO relRoleMenuVo = new RelRoleMenuVO();
						relRoleMenuVo.setRoleId(roleVo.getId());
						relRoleMenuVo.setMenuId(item.getId());
						relRoleMenuVos.add(relRoleMenuVo);
					});
					relRoleMenuMapper.insertBatch(relRoleMenuVos);
				}
			}
		}
	}

	@Override
	public RoleVO selectRole(String roleCode) {
		if(StrUtil.isNotBlank(roleCode)) {
			RoleDTO roleDto = new RoleDTO();
			roleDto.setRoleCode(roleCode);
			List<RoleVO> vos = roleMapper.selectPageVo(roleDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}

	@Override
	public List<RoleDO> selectByIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			return roleMapper.selectBatchIds(ids);
		}
		return null;
	}

}
