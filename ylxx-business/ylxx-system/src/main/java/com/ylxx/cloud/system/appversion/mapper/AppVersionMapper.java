package com.ylxx.cloud.system.appversion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.appversion.model.AppVersionDO;
import com.ylxx.cloud.system.appversion.model.AppVersionDTO;
import com.ylxx.cloud.system.appversion.model.AppVersionVO;

/**
 * 手机APP版本管理 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2021-01-14
 */
public interface AppVersionMapper extends BaseMapper<AppVersionDO> {
	
	Page<AppVersionVO> selectPageVo(Page<AppVersionDTO> page, @Param("param") AppVersionDTO appVersionDto);

	List<AppVersionVO> selectPageVo(@Param("param") AppVersionDTO appVersionDto);

	void insertBatch(List<AppVersionVO> appVersionVos);

}
