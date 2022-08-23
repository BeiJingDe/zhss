package com.ylxx.cloud.upms.org.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.org.model.OrgDO;
import com.ylxx.cloud.upms.org.model.OrgDTO;
import com.ylxx.cloud.upms.org.model.OrgVO;

/**
 * 组织信息表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2021-03-24
 */
public interface OrgMapper extends BaseMapper<OrgDO> {
	
	Page<OrgVO> selectPageVo(Page<OrgDTO> page, @Param("param") OrgDTO orgDto);

	List<OrgVO> selectPageVo(@Param("param") OrgDTO orgDto);

	void insertBatch(List<? extends OrgDO> orgDos);

}
