package com.ylxx.cloud.system.appversion.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.system.appversion.model.AppVersionDO;
import com.ylxx.cloud.system.appversion.model.AppVersionDTO;
import com.ylxx.cloud.system.appversion.model.AppVersionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 手机APP版本管理 服务类
 *
 * @author caixiaopeng
 * @since 2021-01-14
 */
public interface IAppVersionService {

	Page<AppVersionVO> selectPageVo(AppVersionDTO appVersionDto);

	AppVersionVO selectById(String id);

	void insert(AppVersionVO appVersionVo);
	
	void insertBatch(List<AppVersionVO> appVersionVos);

	void update(AppVersionVO appVersionVo);

	void deleteBatchIds(List<String> ids);

	void importData(MultipartFile file);

	void publish(String id);

}
