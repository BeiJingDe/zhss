package com.ylxx.cloud.system.api.service.impl;

import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.redis.util.RoleApiCacheUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.api.model.RoleApiDO;
import com.ylxx.cloud.system.api.model.RoleApiDTO;
import com.ylxx.cloud.system.api.model.RoleApiVO;
import com.ylxx.cloud.system.api.mapper.RoleApiMapper;
import com.ylxx.cloud.system.api.service.IRoleApiService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;

/**
 * @Description 角色API接口权限表 服务实现类
 * @author caixiaopeng
 * @since 2021-11-15
 */
@Service
@Transactional
public class RoleApiServiceImpl implements IRoleApiService {
	
	@Resource
	private RoleApiMapper roleApiMapper;
	
	@Override
	public Page<RoleApiVO> selectPageVo(RoleApiDTO roleApiDto) {
		Page<RoleApiDTO> page = new Page<RoleApiDTO>(roleApiDto.getCurrent(), roleApiDto.getSize());
		if(StrUtil.isNotBlank(roleApiDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(roleApiDto.getOrders()));
		}
		return roleApiMapper.selectPageVo(page, roleApiDto);
	}

	@Override
	public RoleApiVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			RoleApiDTO roleApiDto = new RoleApiDTO();
			roleApiDto.setId(id);
			List<RoleApiVO> vos = roleApiMapper.selectPageVo(roleApiDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void refreshCache() {
		List<RoleApiVO> roleApiVos = roleApiMapper.selectPageVo(new RoleApiDTO());
		RoleApiCacheUtil.deleteCache();
		roleApiVos.forEach(item -> {
			if(StrUtil.equals(ConfigConsts.VALUE_YES_1, item.getIsActive())) {
				RoleApiCacheUtil.addUrlSet(item.getRoleCode(), item.getAppName(), item.getReqMethod(), item.getReqUrl());
			}
		});
	}

}
