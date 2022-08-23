package com.ylxx.cloud.system.log.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.log.mapper.SystemLogMapper;
import com.ylxx.cloud.system.log.model.SystemLogDTO;
import com.ylxx.cloud.system.log.model.SystemLogVO;
import com.ylxx.cloud.system.log.service.ISystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 系统日志 服务实现类
 * @author caixiaopeng
 * @since 2020-07-03
 */
@Service
@Transactional
public class SystemLogServiceImpl implements ISystemLogService {
	
	@Resource
	private SystemLogMapper systemLogMapper;
	
	@Override
	public Page<SystemLogVO> selectPageVo(SystemLogDTO systemLogDto) {
		Page<SystemLogDTO> page = new Page<SystemLogDTO>(systemLogDto.getCurrent(), systemLogDto.getSize());
		if(StrUtil.isNotBlank(systemLogDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(systemLogDto.getOrders()));
		}
		return systemLogMapper.selectPageVo(page, systemLogDto);
	}

	@Override
	public List<SystemLogVO> selectVos(SystemLogDTO systemLogDto) {
		return systemLogMapper.selectPageVo(systemLogDto);
	}
	
	@Override
	public SystemLogVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			SystemLogDTO systemLogDto = new SystemLogDTO();
			systemLogDto.setId(id);
			List<SystemLogVO> vos = systemLogMapper.selectPageVo(systemLogDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(SystemLogVO systemLogVo) {
		systemLogVo.setId(IdUtil.fastSimpleUUID());
		systemLogMapper.insert(systemLogVo);
	}
	
	/*@Override
	public void insertBatch(List<SystemLogVO> systemLogVos) {
		if(ObjectUtil.isNotEmpty(systemLogVos)) {
			systemLogVos.forEach(item -> {
				item.setId(IdUtil.fastSimpleUUID());
			});
			systemLogMapper.insertBatch(systemLogVos);
		}
	}*/

	@Override
	public void update(SystemLogVO systemLogVo) {
		systemLogMapper.updateById(systemLogVo);
	}
	
	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			systemLogMapper.deleteBatchIds(ids);
		}
	}

	@Override
	public void backup() {
		systemLogMapper.backup();
	}

	@Override
	public void recovery() {
		systemLogMapper.recovery();
	}
	
}
