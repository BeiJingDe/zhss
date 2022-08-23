package com.ylxx.cloud.system.permissionurl.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlDTO;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlVO;

/**
 * 允许的url名单 服务类
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface IPermissionUrlService {

	Page<PermissionUrlVO> selectPageVo(PermissionUrlDTO permissionUrlDto);

	List<PermissionUrlVO> selectVos(PermissionUrlDTO permissionUrlDto);

	PermissionUrlVO selectById(String id);

	void insert(PermissionUrlVO permissionUrlVo);
	
	void insertBatch(List<PermissionUrlVO> permissionUrlVos);

	void update(PermissionUrlVO permissionUrlVo);

	void deleteBatchIds(List<String> ids);

	void refreshCache();

}
