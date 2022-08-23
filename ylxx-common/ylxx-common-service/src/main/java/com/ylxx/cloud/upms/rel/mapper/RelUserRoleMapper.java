package com.ylxx.cloud.upms.rel.mapper;

import java.util.List;

import com.ylxx.cloud.upms.rel.model.RelUserRoleDO;
import com.ylxx.cloud.upms.rel.model.RelUserRoleDTO;
import com.ylxx.cloud.upms.rel.model.RelUserRoleVO;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 用户和角色关联表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-09-11
 */
public interface RelUserRoleMapper extends BaseMapper<RelUserRoleDO> {

    Page<RelUserRoleVO> selectPageVo(Page<RelUserRoleDTO> page, @Param("param") RelUserRoleDTO relUserRoleDto);

    List<RelUserRoleVO> selectPageVo(@Param("param") RelUserRoleDTO relUserRoleDto);

    void insertBatch(List<RelUserRoleVO> relUserRoleVos);

}
