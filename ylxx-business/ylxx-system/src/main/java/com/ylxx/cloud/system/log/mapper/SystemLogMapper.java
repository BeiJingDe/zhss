package com.ylxx.cloud.system.log.mapper;

import java.util.List;

//import com.baomidou.mybatisplus.core.mapper.ICustomizedBaseMapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.log.model.SystemLogDO;
import com.ylxx.cloud.system.log.model.SystemLogDTO;
import com.ylxx.cloud.system.log.model.SystemLogVO;

/**
 * 系统日志 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-07-03
 */
public interface SystemLogMapper extends BaseMapper<SystemLogDO> {
	
	Page<SystemLogVO> selectPageVo(Page<SystemLogDTO> page, @Param("param") SystemLogDTO systemLogDto);

	List<SystemLogVO> selectPageVo(@Param("param") SystemLogDTO systemLogDto);

	double countSystemLogCapacity();

	void backup();

	void recovery();
}
