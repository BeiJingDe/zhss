package com.ylxx.cloud.upms.rel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.rel.model.RelUserOrgDO;
import com.ylxx.cloud.upms.rel.model.RelUserOrgDTO;
import com.ylxx.cloud.upms.rel.model.RelUserOrgVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户和组织关联表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-09-21
 */
public interface RelUserOrgMapper extends BaseMapper<RelUserOrgDO> {
	
	Page<RelUserOrgVO> selectPageVo(Page<RelUserOrgDTO> page, @Param("param") RelUserOrgDTO relUserOrgDto);

	List<RelUserOrgVO> selectPageVo(@Param("param") RelUserOrgDTO relUserOrgDto);

	void insertBatch(List<RelUserOrgVO> relUserOrgVos);

}
