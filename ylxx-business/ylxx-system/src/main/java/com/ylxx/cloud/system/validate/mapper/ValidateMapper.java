package com.ylxx.cloud.system.validate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.validate.model.ValidateDO;
import com.ylxx.cloud.system.validate.model.ValidateDTO;
import com.ylxx.cloud.system.validate.model.ValidateVO;

/**
 * 前后端一致校验规则表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
public interface ValidateMapper extends BaseMapper<ValidateDO> {
	
	Page<ValidateVO> selectPageVo(Page<ValidateDTO> page, @Param("param") ValidateDTO validateDto);

	List<ValidateVO> selectPageVo(@Param("param") ValidateDTO validateDto);

	void insertBatch(List<ValidateVO> validateVos);

}
