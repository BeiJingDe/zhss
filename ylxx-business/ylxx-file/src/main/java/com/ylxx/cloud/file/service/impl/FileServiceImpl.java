package com.ylxx.cloud.file.service.impl;

import java.io.File;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.fdfs.service.IFdfsService;
import com.ylxx.cloud.localfs.service.ILocalFsService;
import com.ylxx.cloud.oss.service.IOssService;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.file.consts.FileConsts;
import com.ylxx.cloud.file.mapper.FileMapper;
import com.ylxx.cloud.file.model.FileDO;
import com.ylxx.cloud.file.model.FileDTO;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.file.service.IFileService;
import com.ylxx.cloud.fdfs.util.FastDFSUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;

/**
 * @Description 文件管理表 服务实现类
 * @author caixiaopeng
 * @since 2020-07-18
 */
@Service
@Transactional
public class FileServiceImpl implements IFileService {
	
	@Resource
	private FileMapper fileMapper;

	@Resource
	private IFdfsService fdfsService;
	@Resource
	private IOssService ossService;
	@Resource
	private ILocalFsService localFsService;

	@Override
	public Page<FileVO> selectPageVo(FileDTO fileDto) {
		// 不查询已删除的文件
		fileDto.setStatus(FileConsts.STATUS_TYPE_1);
		Page<FileDTO> page = new Page<FileDTO>(fileDto.getCurrent(), fileDto.getSize());
		if(StrUtil.isNotBlank(fileDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(fileDto.getOrders()));
		}
		return fileMapper.selectPageVo(page, fileDto);
	}

	@Override
	public List<FileVO> selectVos(FileDTO fileDto) {
		// 不查询已删除的文件
		fileDto.setStatus(FileConsts.STATUS_TYPE_1);
		return fileMapper.selectPageVo(fileDto);
	}
	
	@Override
	public FileVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			FileDTO fileDto = new FileDTO();
			fileDto.setId(id);
			// 不查询已删除的文件
			fileDto.setStatus(FileConsts.STATUS_TYPE_1);
			List<FileVO> vos = fileMapper.selectPageVo(fileDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(FileVO fileVo) {
		fileMapper.insert(fileVo);
	}
	
	@Override
	public void insertBatch(List<FileVO> fileVos) {
		if(ObjectUtil.isNotEmpty(fileVos)) {
			fileMapper.insertBatch(fileVos);
		}
	}

	@Override
	public void update(FileVO fileVo) {
		fileMapper.updateById(fileVo);
	}
	
	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			String storageType = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FILE, ConfigConsts.FILE_KEY_STORAGE_TYPE);
			String isRealDelete = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FILE, ConfigConsts.FILE_KEY_IS_REAL_DELETE);
			if (StrUtil.equals(isRealDelete, ConfigConsts.VALUE_YES_1)) {
				// 1. 真删除
				if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_FDFS)) {
					fdfsService.deleteBatchIds(ids);
				} else if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_OSS)) {
					ossService.deleteBatchIds(ids);
				} else if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_LOCALFS)) {
					localFsService.deleteBatchIds(ids);
				}
				fileMapper.deleteBatchIds(ids);
			} else {
				// 2. 假删除：只修改记录状态
				FileDO entity = new FileDO();
				entity.setStatus(FileConsts.STATUS_TYPE_0);
				entity.setUpdateTime(DateUtil.date());
				entity.setUpdateBy(HttpServletUtil.getUsername());
				LambdaUpdateWrapper<FileDO> updateWrapper = Wrappers.lambdaUpdate();
				updateWrapper.in(FileDO::getId, ids);
				fileMapper.update(entity, updateWrapper);
			}
		}
	}

	@Override
	public List<FileVO> upload(FileVO fileVo, MultipartFile[] file) {
		String storageType = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FILE, ConfigConsts.FILE_KEY_STORAGE_TYPE);
		if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_FDFS)) {
			return fdfsService.upload(fileVo, file);
		} else if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_OSS)) {
			return ossService.upload(fileVo, file);
		} else if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_LOCALFS)) {
			return localFsService.upload(fileVo, file);
		} else {
			throw new ServiceException(StrUtil.format("请设置storageType文件系统类型：{}，{}或{}", ConfigConsts.VALUE_FILE_STORAGE_TYPE_FDFS, ConfigConsts.VALUE_FILE_STORAGE_TYPE_OSS, ConfigConsts.VALUE_FILE_STORAGE_TYPE_LOCALFS));
		}
	}

	@Override
	public void download(String id) {
		String storageType = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FILE, ConfigConsts.FILE_KEY_STORAGE_TYPE);
		if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_FDFS)) {
			fdfsService.download(id);
		} else if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_OSS)) {
			ossService.download(id);
		} else if(StrUtil.equals(storageType, ConfigConsts.VALUE_FILE_STORAGE_TYPE_LOCALFS)) {
			localFsService.download(id);
		} else {
			throw new ServiceException(StrUtil.format("请设置storageType文件系统类型：{}，{}或{}", ConfigConsts.VALUE_FILE_STORAGE_TYPE_FDFS, ConfigConsts.VALUE_FILE_STORAGE_TYPE_OSS, ConfigConsts.VALUE_FILE_STORAGE_TYPE_LOCALFS));
		}
		if(StrUtil.isNotBlank(id)) {
			fileMapper.updateDownloadCount(id);
		}
	}

}
