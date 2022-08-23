package com.ylxx.cloud.system.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.api.model.RoleApiDO;
import com.ylxx.cloud.system.api.model.RoleApiDTO;
import com.ylxx.cloud.system.api.model.RoleApiVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 角色API接口权限表 服务类
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
public interface IRoleApiService {

	Page<RoleApiVO> selectPageVo(RoleApiDTO roleApiDto);

	RoleApiVO selectById(String id);

    void refreshCache();
}
