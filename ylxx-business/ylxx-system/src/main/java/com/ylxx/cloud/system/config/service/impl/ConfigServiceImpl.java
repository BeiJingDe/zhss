package com.ylxx.cloud.system.config.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.config.mapper.ConfigMapper;
import com.ylxx.cloud.system.config.model.ConfigDTO;
import com.ylxx.cloud.system.config.model.ConfigVO;
import com.ylxx.cloud.system.config.service.IConfigService;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description 系统参数 服务实现类
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Service
@Transactional
@Slf4j
public class ConfigServiceImpl implements IConfigService {
	
	@Resource
	private ConfigMapper configMapper;
	
	@Override
	public Page<ConfigVO> selectPageVo(ConfigDTO configDto) {
		Page<ConfigDTO> page = new Page<ConfigDTO>(configDto.getCurrent(), configDto.getSize());
		if(StrUtil.isNotBlank(configDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(configDto.getOrders()));
		}
		return configMapper.selectPageVo(page, configDto);
	}

	@Override
	public List<ConfigVO> selectVos(ConfigDTO configDto) {
		return configMapper.selectPageVo(configDto);
	}
	
	@Override
	public ConfigVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			ConfigDTO configDto = new ConfigDTO();
			configDto.setId(id);
			List<ConfigVO> vos = configMapper.selectPageVo(configDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(ConfigVO configVo) {
		configVo.setId(IdUtil.fastSimpleUUID());
		configVo.setIsEncrypt("0");
		configVo.setSortNo(9999);
		configVo.setIsShow("1");
		configVo.setCreateTime(DateUtil.date());
		configVo.setUpdateTime(DateUtil.date());
		configVo.setCreateBy(HttpServletUtil.getUsername());
		configVo.setUpdateBy(HttpServletUtil.getUsername());
		configMapper.insert(configVo);

		// 刷新缓存
		ConfigCacheUtil.setConfig(configVo.getConfigGroup(), configVo.getKey(), configVo.getValue());
	}
	
	@Override
	public void insertBatch(List<ConfigVO> configVos) {
		if(ObjectUtil.isNotEmpty(configVos)) {
			configMapper.insertBatch(configVos);
		}
	}

	@Override
	public void update(ConfigVO configVo) {
		if(StrUtil.isNotBlank(configVo.getId())
				&& StrUtil.isNotBlank(configVo.getValue())) {
			ConfigVO old = this.selectById(configVo.getId());
			if(ObjectUtil.isNull(old)) {
				throw new ServiceException("更新的配置数据不存在");
			}
			configVo.setConfigGroup(null);
			configVo.setKey(null);
			configVo.setCreateBy(null);
			configVo.setCreateTime(null);
			if(StrUtil.isBlank(configVo.getDescription())) {
				configVo.setDescription(null);
			}
			configVo.setUpdateBy(HttpServletUtil.getUsername());
			configVo.setUpdateTime(DateUtil.date());
			configMapper.updateById(configVo);

			// 刷新缓存
			ConfigCacheUtil.setConfig(old.getConfigGroup(), old.getKey(), configVo.getValue());
		}
	}
	
	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			configMapper.deleteBatchIds(ids);
		}
	}
	
	@Override
	public void refreshCache() {
		List<ConfigVO> vos = configMapper.selectPageVo(new ConfigDTO());
		log.info("将系统参数从redis中删除");
		ConfigCacheUtil.deleteCache();
		log.info("将系统参数缓存到redis中#start");
		vos.forEach(item -> {
			ConfigCacheUtil.setConfig(item.getConfigGroup(), item.getKey(), item.getValue());
		});
		log.info("将系统参数缓存到redis中#end");
	}
	
}
