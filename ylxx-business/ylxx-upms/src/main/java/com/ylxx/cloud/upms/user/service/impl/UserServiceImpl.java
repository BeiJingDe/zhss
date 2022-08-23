package com.ylxx.cloud.upms.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.upms.org.mapper.OrgMapper;
import com.ylxx.cloud.upms.org.model.OrgDTO;
import com.ylxx.cloud.upms.org.model.OrgVO;
import com.ylxx.cloud.upms.rel.mapper.RelUserOrgMapper;
import com.ylxx.cloud.upms.rel.mapper.RelUserRoleMapper;
import com.ylxx.cloud.upms.rel.model.RelUserOrgDO;
import com.ylxx.cloud.upms.rel.model.RelUserOrgVO;
import com.ylxx.cloud.upms.rel.model.RelUserRoleDO;
import com.ylxx.cloud.upms.rel.model.RelUserRoleVO;
import com.ylxx.cloud.upms.role.mapper.RoleMapper;
import com.ylxx.cloud.upms.role.model.RoleDTO;
import com.ylxx.cloud.upms.role.model.RoleVO;
import com.ylxx.cloud.upms.role.service.IRoleService;
import com.ylxx.cloud.upms.user.consts.UserConsts;
import com.ylxx.cloud.upms.user.mapper.UserMapper;
import com.ylxx.cloud.upms.user.model.UserDO;
import com.ylxx.cloud.upms.user.model.UserDTO;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.ylxx.cloud.upms.user.service.IUserService;
import com.ylxx.cloud.util.RSAUtil;
import com.ylxx.cloud.util.SM4Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 用户信息表 服务实现类
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private RelUserRoleMapper relUserRoleMapper;
	@Resource
	private RelUserOrgMapper relUserOrgMapper;
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private OrgMapper orgMapper;
	@Resource
	private IRoleService roleService;

	@Override
	public Page<UserVO> selectPageVo(UserDTO userDto) {
		Page<UserDTO> page = new Page<UserDTO>(userDto.getCurrent(), userDto.getSize());
		if(StrUtil.isNotBlank(userDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(userDto.getOrders()));
		}
		return userMapper.selectPageVo(page, userDto);
	}

	@Override
	public Page<String> selectPageVoList(UserDTO userDto) {
		Page<UserDTO> page = new Page<UserDTO>(userDto.getCurrent(), userDto.getSize());
		return userMapper.selectPageVoList(page, userDto);
	}

	@Override
	public List<UserVO> selectVos(UserDTO userDto) {
		return userMapper.selectPageVo(userDto);
	}
	
	@Override
	public UserVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			UserDTO userDto = new UserDTO();
			userDto.setId(id);
			List<UserVO> vos = userMapper.selectPageVo(userDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(UserVO userVo) {
		userVo.setId(IdUtil.fastSimpleUUID());
		userVo.setStatus(UserConsts.STATUS_TYPE_1);
		userVo.setCreateTime(DateUtil.date());
		userVo.setUpdateTime(DateUtil.date());
		userVo.setCreateBy(HttpServletUtil.getUsername());
		userVo.setUpdateBy(HttpServletUtil.getUsername());
		// 密码rsa解密，sm4加密
		String sm4Key = ConfigCacheValue.SYSTEM_KEY_SM4_KEY();
		String rsaPrivateKey = ConfigCacheValue.SYSTEM_KEY_RSA_PRIVATE_KEY();
		try {
			// RSA解密，SM4加密
			String mmpd = SM4Util.encrypt(sm4Key, RSAUtil.decrypt(userVo.getMmpd(), rsaPrivateKey));
			userVo.setMmpd(mmpd);
		} catch (Exception e) {
			throw new ServiceException("用户密码加解密失败", e);
		}
		userMapper.insert(userVo);
	}
	
	@Override
	public void insertBatch(List<UserVO> userVos) {
		if(ObjectUtil.isNotEmpty(userVos)) {
			userMapper.insertBatch(userVos);
		}
	}

	@Override
	public void update(UserVO userVo) {
		if(StrUtil.isNotBlank(userVo.getId())) {
			// 部分属性不允许修改
			userVo.setUsername(null);
			userVo.setLastLoginTime(null);
			userVo.setLastLogoutTime(null);
			userVo.setCreateBy(null);
			userVo.setCreateTime(null);
			if(StrUtil.isBlank(userVo.getMmpd())) {
				userVo.setMmpd(null);
			}
			// 密码rsa解密，sm4加密
			if(StrUtil.isNotBlank(userVo.getMmpd())) {
				String sm4Key = ConfigCacheValue.SYSTEM_KEY_SM4_KEY();
				String rsaPrivateKey = ConfigCacheValue.SYSTEM_KEY_RSA_PRIVATE_KEY();
				try {
					// RSA解密，SM4加密
					String mmpd = SM4Util.encrypt(sm4Key, RSAUtil.decrypt(userVo.getMmpd(), rsaPrivateKey));
					userVo.setMmpd(mmpd);
				} catch (Exception e) {
					throw new ServiceException("用户密码加解密失败", e);
				}
			}
			// 更新用户和时间
			userVo.setUpdateTime(DateUtil.date());
			userVo.setUpdateBy(HttpServletUtil.getUsername());

			userMapper.updateById(userVo);
		}
	}
	
	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			String isUserRealDelete = ConfigCacheValue.SYSTEM_KEY_IS_USER_REAL_DELETE();
			if(StrUtil.equals(ConfigConsts.VALUE_YES_1, isUserRealDelete)) {
				// 直接删除
				userMapper.deleteBatchIds(ids);
			} else {
				// 更新状态为删除
				UserVO userVo = new UserVO();
				userVo.setStatus(UserConsts.STATUS_TYPE_3);

				LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate();
				updateWrapper.in(UserDO::getId, ids);
				userMapper.update(userVo, updateWrapper);
			}
		}
	}

	@Override
	public UserVO selectUser(String username, String mmpd) {
		if(StrUtil.isNotBlank(username) && StrUtil.isNotBlank(mmpd)) {
			UserDTO userDto = new UserDTO();
			userDto.setUsername(username);
			userDto.setMmpd(mmpd);
			List<UserVO> vos = userMapper.selectPageVo(userDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}

	@Override
	public UserVO selectUser(String username) {
		if(StrUtil.isNotBlank(username)) {
			UserDTO userDto = new UserDTO();
			userDto.setUsername(username);
			List<UserVO> vos = userMapper.selectPageVo(userDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}

	@Override
	public void updateLastLoginTime(String username) {
		if(StrUtil.isNotBlank(username)) {
			UserDO userDo = new UserDO();
			userDo.setLastLoginTime(DateUtil.date());
			LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate();
			wrapper.eq(UserDO::getUsername, username);
			userMapper.update(userDo, wrapper);
		}
	}

	@Override
	public void updateLastLogoutTime(String username) {
		if(StrUtil.isNotBlank(username)) {
			UserDO userDo = new UserDO();
			userDo.setLastLogoutTime(DateUtil.date());
			LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate();
			wrapper.eq(UserDO::getUsername, username);
			userMapper.update(userDo, wrapper);
		}
	}

	@Override
	public void updateMmpd(UserVO userVo) {
		if(StrUtil.isNotBlank(userVo.getId()) && StrUtil.isNotBlank(userVo.getMmpd())) {
			UserVO user = new UserVO();
			user.setId(userVo.getId());
			user.setMmpd(userVo.getMmpd());
			this.update(user);
		}
	}

	@Override
	public void changeUserRole(String username, List<String> roleCodes) {
		if(StrUtil.isNotBlank(username)) {
			UserVO userVo = this.selectUser(username);
			if(ObjectUtil.isNull(userVo)) {
				throw new ServiceException("修改的用户不存在");
			}
			LambdaUpdateWrapper<RelUserRoleDO> updateWrapper = Wrappers.lambdaUpdate();
			updateWrapper.eq(RelUserRoleDO::getUserId, userVo.getId());
			relUserRoleMapper.delete(updateWrapper);

			if(ObjectUtil.isNotEmpty(roleCodes)) {
				RoleDTO roleDto = new RoleDTO();
				roleDto.setRoleCodes(roleCodes);
				List<RoleVO> roleVos = roleMapper.selectPageVo(roleDto);
				if(ObjectUtil.isNotNull(roleVos)) {
					List<RelUserRoleVO> relUserRoleVos = CollUtil.newArrayList();
					roleVos.forEach(item -> {
						RelUserRoleVO relUserRoleVo = new RelUserRoleVO();
						relUserRoleVo.setUserId(userVo.getId());
						relUserRoleVo.setRoleId(item.getId());
						relUserRoleVos.add(relUserRoleVo);
					});
					relUserRoleMapper.insertBatch(relUserRoleVos);
				}
			}
		}
	}

	@Override
	public void changeUserOrg(String username, List<String> orgCodes) {
		if(StrUtil.isNotBlank(username)) {
			UserVO userVo = this.selectUser(username);
			if(ObjectUtil.isNull(userVo)) {
				throw new ServiceException("修改的用户不存在");
			}
			LambdaUpdateWrapper<RelUserOrgDO> updateWrapper = Wrappers.lambdaUpdate();
			updateWrapper.eq(RelUserOrgDO::getUserId, userVo.getId());
			relUserOrgMapper.delete(updateWrapper);

			if(ObjectUtil.isNotEmpty(orgCodes)) {
				OrgDTO orgDto = new OrgDTO();
				orgDto.setOrgCodes(orgCodes);
				List<OrgVO> orgVos = orgMapper.selectPageVo(orgDto);
				if(ObjectUtil.isNotNull(orgVos)) {
					List<RelUserOrgVO> relUserOrgVos = CollUtil.newArrayList();
					orgVos.forEach(item -> {
						RelUserOrgVO relUserOrgVo = new RelUserOrgVO();
						relUserOrgVo.setUserId(userVo.getId());
						relUserOrgVo.setOrgId(item.getId());
						relUserOrgVos.add(relUserOrgVo);
					});
					relUserOrgMapper.insertBatch(relUserOrgVos);
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> selectUserAndRealname() {
		return userMapper.selectUserAndRealname();
	}

	@Override
	public int importData(MultipartFile file) {
		if (ObjectUtil.isNull(file)) {
			throw new ServiceException("上传文件不能为空");
		}
		if (file.getOriginalFilename().lastIndexOf(".xls") == -1
				&& file.getOriginalFilename().lastIndexOf(".xlsx") == -1) {
			throw new ServiceException("请上传excel格式文件（.xls/.xlsx）");
		}
		List<UserVO> importList = null;
		try {
			importList = ExcelUtil.importExcel(UserVO.class, file.getInputStream());
		} catch (Exception e) {
			throw new ServiceException("文件数据解析失败", e);
		}
		if(ObjectUtil.isNotEmpty(importList)) {
			String username = HttpServletUtil.getUsername();
			Date now = DateUtil.date();
			final String defaultMmpd;
			try {
				// SM4加密
				defaultMmpd = SM4Util.encrypt(ConfigCacheValue.SYSTEM_KEY_SM4_KEY(), ConfigCacheValue.SYSTEM_KEY_DEFAULT_MMPD());
			} catch (Exception e) {
				throw new ServiceException("用户密码加解密失败", e);
			}
			List<UserVO> insertList = importList.stream()
					.filter(item -> !StrUtil.hasBlank(item.getUsername(), item.getRealname()))
					.map(item -> {
						item.setId(IdUtil.fastSimpleUUID());
						if(StrUtil.isBlank(item.getMmpd())) {
							item.setMmpd(defaultMmpd);
						} else {
							item.setMmpd(SM4Util.encrypt(ConfigCacheValue.SYSTEM_KEY_SM4_KEY(), item.getMmpd()));
						}
						item.setStatus(UserConsts.STATUS_TYPE_1);
						item.setCreateTime(now);
						item.setUpdateTime(now);
						item.setCreateBy(username);
						item.setUpdateBy(username);
						return item;
					})
					.collect(Collectors.toList());
			if(ObjectUtil.isNotEmpty(insertList)) {
				// 1. 批量新增用户
				this.insertBatch(insertList);
				// 2. 批量分配角色
				RoleVO roleVo = roleService.selectRole(ConfigCacheValue.SYSTEM_KEY_DEFAULT_ROLE());
				if(ObjectUtil.isNotNull(roleVo)) {
					List<RelUserRoleVO> relUserRoleVos = insertList.stream()
							.map(item -> {
								RelUserRoleVO relUserRoleVo = new RelUserRoleVO();
								relUserRoleVo.setUserId(item.getId());
								relUserRoleVo.setRoleId(roleVo.getId());
								return relUserRoleVo;
							}).collect(Collectors.toList());
					relUserRoleMapper.insertBatch(relUserRoleVos);
				}
			}
		}
		return ObjectUtil.isEmpty(importList) ? 0 : importList.size();
	}

	@Override
	public List<UserVO> selectByIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			UserDTO userDto = new UserDTO();
			userDto.setIds(ids);
			return userMapper.selectPageVo(userDto);
		}
		return null;
	}

}
