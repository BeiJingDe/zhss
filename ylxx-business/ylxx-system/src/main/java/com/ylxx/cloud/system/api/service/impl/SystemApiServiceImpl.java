package com.ylxx.cloud.system.api.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.api.model.SystemApiDO;
import com.ylxx.cloud.system.api.model.SystemApiDTO;
import com.ylxx.cloud.system.api.model.SystemApiVO;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.api.mapper.SystemApiMapper;
import com.ylxx.cloud.system.api.service.ISystemApiService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description 系统API接口管理表 服务实现类
 * @author caixiaopeng
 * @since 2021-11-15
 */
@Service
@Transactional
public class SystemApiServiceImpl implements ISystemApiService {
	
	@Resource
	private SystemApiMapper systemApiMapper;
	@Resource
	private DiscoveryClient discoveryClient;
	@Resource
	private RestTemplate restTemplate;
	
	@Override
	public Page<SystemApiVO> selectPageVo(SystemApiDTO systemApiDto) {
		Page<SystemApiDTO> page = new Page<SystemApiDTO>(systemApiDto.getCurrent(), systemApiDto.getSize());
		if(StrUtil.isNotBlank(systemApiDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(systemApiDto.getOrders()));
		}
		return systemApiMapper.selectPageVo(page, systemApiDto);
	}

	@Override
	public SystemApiVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			SystemApiDTO systemApiDto = new SystemApiDTO();
			systemApiDto.setId(id);
			List<SystemApiVO> vos = systemApiMapper.selectPageVo(systemApiDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void syncApi() {
		List<String> appNames = discoveryClient.getServices();
		if(ObjectUtil.isNotEmpty(appNames)) {
			String username = HttpServletUtil.getUsername();
			Date now  = DateUtil.date();
			List<SystemApiVO> systemApiVos = CollUtil.newArrayList();
			appNames.forEach(appName -> {
				String url = StrUtil.format("http://{}/v2/api-docs?group={}", appName, SystemConsts.SWAGGER_GROUP_NAME);
				String result = restTemplate.getForObject(url, String.class);
				if(JSON.isValid(result)) {
					JSONObject apiObj = JSON.parseObject(result);
					JSONObject paths = apiObj.getJSONObject("paths");
					if(ObjectUtil.isNotEmpty(paths)) {
						paths.keySet().forEach(reqUrl -> {
							JSONObject reqMethodObj = paths.getJSONObject(reqUrl);
							reqMethodObj.keySet().forEach(reqMethod -> {
								JSONObject methodInfo = reqMethodObj.getJSONObject(reqMethod);
								JSONArray moduleArr = methodInfo.getJSONArray("tags");

								String module = null;
								if(ObjectUtil.isNotEmpty(moduleArr)) {
									module = moduleArr.getString(0);
								}
								String summary = methodInfo.getString("summary");

								SystemApiVO systemApiVo = new SystemApiVO();
								systemApiVo.setId(IdUtil.fastSimpleUUID());
								systemApiVo.setAppName(appName);
								systemApiVo.setReqUrl(reqUrl);
								systemApiVo.setReqMethod(reqMethod.toUpperCase());
								systemApiVo.setModule(module);
								systemApiVo.setSummary(summary);
								systemApiVo.setCreateBy(username);
								systemApiVo.setUpdateBy(username);
								systemApiVo.setCreateTime(now);
								systemApiVo.setUpdateTime(now);
								systemApiVos.add(systemApiVo);
							});
						});
					}
				}
			});
			if(ObjectUtil.isNotEmpty(systemApiVos)) {
				// 把所有的接口存入表中，存在则忽略
				//systemApiMapper.insertBatchIgnore(systemApiVos);
				// 把所有的接口和所有的角色关联起来，默认不生效，存在则忽略
				systemApiMapper.createRoleApiRel();
			}
		}
	}


}
