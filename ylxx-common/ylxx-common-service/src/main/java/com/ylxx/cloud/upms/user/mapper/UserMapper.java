package com.ylxx.cloud.upms.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.org.model.OrgVO;
import com.ylxx.cloud.upms.user.model.BaseOrg;
import com.ylxx.cloud.upms.user.model.UserDO;
import com.ylxx.cloud.upms.user.model.UserDTO;
import com.ylxx.cloud.upms.user.model.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
public interface UserMapper extends BaseMapper<UserDO> {
	
	Page<UserVO> selectPageVo(Page<UserDTO> page, @Param("param") UserDTO userDto);

	List<UserVO> selectPageVo(@Param("param") UserDTO userDto);

	void insertBatch(List<UserVO> userVos);

    List<Map<String, Object>> selectUserAndRealname();

	List<String> selectRoleCodeByUserName( @Param("username") String username);

	Page<String> selectPageVoList(Page<UserDTO> page, @Param("param") UserDTO userDto);

    BaseOrg selectBaseOrg(@Param("deptId") String deptId);

	List<BaseOrg> selectBaseOrgCorp();

	void deleteBaseOrgDept();

	int insertBaseOrg(BaseOrg newBaseOrg);

	int insertBaseUserOrg(@Param("userId")String userId,@Param("deptId") String deptId);

	OrgVO selectOrgVos(@Param("username") String username);

	OrgVO selectOrgVo(@Param("parentId") String parentId);
}
