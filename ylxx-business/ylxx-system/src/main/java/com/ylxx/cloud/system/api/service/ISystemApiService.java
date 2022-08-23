package com.ylxx.cloud.system.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.api.model.SystemApiDO;
import com.ylxx.cloud.system.api.model.SystemApiDTO;
import com.ylxx.cloud.system.api.model.SystemApiVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统API接口管理表 服务类
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
public interface ISystemApiService {

	Page<SystemApiVO> selectPageVo(SystemApiDTO systemApiDto);

	SystemApiVO selectById(String id);

    void syncApi();
}
