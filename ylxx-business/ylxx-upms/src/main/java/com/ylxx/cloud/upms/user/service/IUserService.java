package com.ylxx.cloud.upms.user.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.user.model.UserDO;
import com.ylxx.cloud.upms.user.model.UserDTO;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户信息表 服务类
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
public interface IUserService {

	Page<UserVO> selectPageVo(UserDTO userDto);

	List<UserVO> selectVos(UserDTO userDto);

	UserVO selectById(String id);

	void insert(UserVO userVo);
	
	void insertBatch(List<UserVO> userVos);

	void update(UserVO userVo);

	void deleteBatchIds(List<String> ids);

    UserVO selectUser(String username, String mmpd);

    UserVO selectUser(String username);

    void updateLastLoginTime(String username);

    void updateLastLogoutTime(String username);

    void updateMmpd(UserVO userVo);

	void changeUserRole(String username, List<String> roleCodes);

    void changeUserOrg(String username, List<String> orgCodes);

    List<Map<String, Object>> selectUserAndRealname();

    int importData(MultipartFile file);

    List<UserVO> selectByIds(List<String> ids);

    Page<String> selectPageVoList(UserDTO userDto);
}
