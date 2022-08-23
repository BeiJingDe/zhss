package com.ylxx.cloud.upms.org.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.org.model.OrgDO;
import com.ylxx.cloud.upms.org.model.OrgDTO;
import com.ylxx.cloud.upms.org.model.OrgVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 组织信息表 服务类
 *
 * @author caixiaopeng
 * @since 2021-03-24
 */
public interface IOrgService {

	Page<OrgVO> selectPageVo(OrgDTO orgDto);

	OrgVO selectById(String id);

	void insert(OrgVO orgVo);
	
	void insertBatch(List<OrgVO> orgVos);

	void update(OrgVO orgVo);

	void deleteBatchIds(List<String> ids);

	void importData(MultipartFile file);

    List<OrgVO> selectOrgVos(String username);
}
