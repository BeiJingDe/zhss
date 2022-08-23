package com.ylxx.cloud.upms.org.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.upms.org.mapper.OrgMapper;
import com.ylxx.cloud.upms.org.model.OrgDTO;
import com.ylxx.cloud.upms.org.model.OrgVO;
import com.ylxx.cloud.upms.org.service.IOrgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 组织信息表 服务实现类
 * @author caixiaopeng
 * @since 2021-03-24
 */
@Service
@Transactional
public class OrgServiceImpl implements IOrgService {
	
	@Resource
	private OrgMapper orgMapper;
	
	@Override
	public Page<OrgVO> selectPageVo(OrgDTO orgDto) {
		Page<OrgDTO> page = new Page<OrgDTO>(orgDto.getCurrent(), orgDto.getSize());
		if(StrUtil.isNotBlank(orgDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(orgDto.getOrders()));
		}
		return orgMapper.selectPageVo(page, orgDto);
	}

	@Override
	public OrgVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			OrgDTO orgDto = new OrgDTO();
			orgDto.setId(id);
			List<OrgVO> vos = orgMapper.selectPageVo(orgDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(OrgVO orgVo) {
		orgVo.setId(IdUtil.fastSimpleUUID());
		orgVo.setCreateTime(DateUtil.date());
		orgVo.setUpdateTime(DateUtil.date());
		orgVo.setCreateBy(HttpServletUtil.getUsername());
		orgVo.setUpdateBy(HttpServletUtil.getUsername());

		orgMapper.insert(orgVo);
	}
	
	@Override
	public void insertBatch(List<OrgVO> orgVos) {
		if(ObjectUtil.isNotEmpty(orgVos)) {
			orgMapper.insertBatch(orgVos);
		}
	}

	@Override
	public void update(OrgVO orgVo) {
		orgVo.setUpdateTime(DateUtil.date());
		orgVo.setUpdateBy(HttpServletUtil.getUsername());

		orgMapper.updateById(orgVo);
	}
	
	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			orgMapper.deleteBatchIds(ids);
		}
	}

	@Override
	public void importData(MultipartFile file) {
		try {
			if (ObjectUtil.isNull(file)) {
				throw new ServiceException("上传文件不能为空");
			}
			if (file.getOriginalFilename().lastIndexOf(".xls") == -1
					&& file.getOriginalFilename().lastIndexOf(".xlsx") == -1) {
				throw new ServiceException("请上传excel格式文件（.xls/.xlsx）");
			}
			List<OrgVO> importList = ExcelUtil.importExcel(OrgVO.class, file.getInputStream());
			this.insertBatch(importList);
		} catch (Exception e) {
			throw new ServiceException("数据导入失败", e);
		}
	}

	@Override
	public List<OrgVO> selectOrgVos(String username) {
		if(StrUtil.isNotBlank(username)) {
			OrgDTO orgDto = new OrgDTO();
			orgDto.setCurrent(1);
			orgDto.setSize(SystemConsts.MAX_PAGE_SIZE);
			orgDto.setOrders("[{column:'T1.sort_no',asc:true}]");
			orgDto.setUsername(username);
			Page<OrgVO> result = this.selectPageVo(orgDto);
			return result.getRecords();
		}
		return null;
	}
	
}
