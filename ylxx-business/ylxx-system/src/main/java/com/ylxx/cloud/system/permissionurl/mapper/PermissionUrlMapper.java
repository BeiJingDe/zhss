package com.ylxx.cloud.system.permissionurl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlDO;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlDTO;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlVO;

/**
 * 允许的url名单 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface PermissionUrlMapper extends BaseMapper<PermissionUrlDO> {
	
	Page<PermissionUrlVO> selectPageVo(Page<PermissionUrlDTO> page, @Param("param") PermissionUrlDTO permissionUrlDto);

	List<PermissionUrlVO> selectPageVo(@Param("param") PermissionUrlDTO permissionUrlDto);

	void insertBatch(List<PermissionUrlVO> permissionUrlVos);

}
