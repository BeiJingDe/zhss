package com.ylxx.cloud.system.appversion.service.impl;

import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.appversion.model.AppVersionDO;
import com.ylxx.cloud.system.appversion.model.AppVersionDTO;
import com.ylxx.cloud.system.appversion.model.AppVersionVO;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.appversion.mapper.AppVersionMapper;
import com.ylxx.cloud.system.appversion.service.IAppVersionService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description 手机APP版本管理 服务实现类
 * @author caixiaopeng
 * @since 2021-01-14
 */
@Service
@Transactional
public class AppVersionServiceImpl implements IAppVersionService {
	
	@Resource
	private AppVersionMapper appVersionMapper;
	
	@Override
	public Page<AppVersionVO> selectPageVo(AppVersionDTO appVersionDto) {
		Page<AppVersionDTO> page = new Page<AppVersionDTO>(appVersionDto.getCurrent(), appVersionDto.getSize());
		if(StrUtil.isNotBlank(appVersionDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(appVersionDto.getOrders()));
		}
		return appVersionMapper.selectPageVo(page, appVersionDto);
	}

	@Override
	public AppVersionVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			AppVersionDTO appVersionDto = new AppVersionDTO();
			appVersionDto.setId(id);
			List<AppVersionVO> vos = appVersionMapper.selectPageVo(appVersionDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(AppVersionVO appVersionVo) {
		appVersionVo.setId(IdUtil.fastSimpleUUID());
		appVersionVo.setIsPublish(ConfigConsts.VALUE_NO_0);
		appVersionVo.setPublishTime(null);
		appVersionVo.setCreateTime(DateUtil.date());
		appVersionVo.setUpdateTime(DateUtil.date());
		appVersionVo.setCreateBy(HttpServletUtil.getUsername());
		appVersionVo.setUpdateBy(HttpServletUtil.getUsername());

		appVersionMapper.insert(appVersionVo);
	}
	
	@Override
	public void insertBatch(List<AppVersionVO> appVersionVos) {
		if(ObjectUtil.isNotEmpty(appVersionVos)) {
			appVersionMapper.insertBatch(appVersionVos);
		}
	}

	@Override
	public void update(AppVersionVO appVersionVo) {
		AppVersionVO old = this.selectById(appVersionVo.getId());
		if(ObjectUtil.isNull(old)) {
			throw new ServiceException("修改的APP版本不存在");
		}
		if(StrUtil.equals(appVersionVo.getIsPublish(), ConfigConsts.VALUE_YES_1)) {
			throw new ServiceException("APP版本已发布，不能修改");
		}
		appVersionVo.setIsPublish(ConfigConsts.VALUE_NO_0);
		appVersionVo.setPublishTime(null);
		appVersionVo.setCreateTime(null);
		appVersionVo.setCreateBy(null);
		appVersionVo.setUpdateTime(DateUtil.date());
		appVersionVo.setUpdateBy(HttpServletUtil.getUsername());

		appVersionMapper.updateById(appVersionVo);
	}
	
	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			AppVersionDTO appVersionDto = new AppVersionDTO();
			appVersionDto.setIds(ids);
			List<AppVersionVO> vos = appVersionMapper.selectPageVo(appVersionDto);
			if(ObjectUtil.isNotEmpty(vos)) {
				vos.forEach(item -> {
					if(StrUtil.equals(item.getIsPublish(), ConfigConsts.VALUE_YES_1)) {
						throw new ServiceException(StrUtil.format("版本号为[{}]的app已发布，不能删除", item.getAppVersion()));
					}
				});
				appVersionMapper.deleteBatchIds(ids);
			}
		}
	}

	@Override
	public void importData(MultipartFile file) {
		try {
			if (ObjectUtil.isNull(file)) {
				throw new ServiceException("上传文件不能为空");
			}
			if (file.getOriginalFilename().lastIndexOf(".xls") == -1
					&& file.getOriginalFilename().lastIndexOf(".xlsx") == -1) {
				throw new ServiceException("请上传excel格式文件（.xls/.xlsx）");
			}
			List<AppVersionVO> importList = ExcelUtil.importExcel(AppVersionVO.class, file.getInputStream());
			this.insertBatch(importList);
		} catch (Exception e) {
			throw new ServiceException("数据导入失败", e);
		}
	}

	@Override
	public void publish(String id) {
		if(StrUtil.isNotBlank(id)) {
			AppVersionVO appVersionVo = this.selectById(id);
			if(ObjectUtil.isNull(appVersionVo)) {
				throw new ServiceException("发布的APP版本不存在");
			}
			if(StrUtil.equals(appVersionVo.getIsPublish(), ConfigConsts.VALUE_YES_1)) {
				throw new ServiceException("APP版本已发布，不能重复发布");
			}
			AppVersionVO vo = new AppVersionVO();
			vo.setId(id);
			vo.setIsPublish(ConfigConsts.VALUE_YES_1);
			vo.setPublishTime(DateUtil.date());
			vo.setUpdateTime(DateUtil.date());
			vo.setUpdateBy(HttpServletUtil.getUsername());
			appVersionMapper.updateById(vo);
		}
	}

}
