package com.ylxx.cloud.system.dict.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.dict.consts.DictConsts;
import com.ylxx.cloud.system.dict.mapper.DictMapper;
import com.ylxx.cloud.system.dict.model.DictDO;
import com.ylxx.cloud.system.dict.model.DictDTO;
import com.ylxx.cloud.system.dict.model.DictVO;
import com.ylxx.cloud.system.dict.service.IDictService;
import com.ylxx.cloud.system.dict.util.DictCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 数据字典 服务实现类
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Service
@Transactional
@Slf4j
public class DictServiceImpl implements IDictService {
	
	@Resource
	private DictMapper dictMapper;
	
	@Override
	public Page<DictVO> selectPageVo(DictDTO dictDto) {
		Page<DictDTO> page = new Page<DictDTO>(dictDto.getCurrent(), dictDto.getSize());
		if(StrUtil.isNotBlank(dictDto.getOrders())) {
			page.addOrder(CommonCoreServiceImpl.parseOrders(dictDto.getOrders()));
		}
		return dictMapper.selectPageVo(page, dictDto);
	}

	@Override
	public List<DictVO> selectVos(DictDTO dictDto) {
		return dictMapper.selectPageVo(dictDto);
	}
	
	@Override
	public DictVO selectById(String id) {
		if(StrUtil.isNotBlank(id)) {
			DictDTO dictDto = new DictDTO();
			dictDto.setId(id);
			List<DictVO> vos = dictMapper.selectPageVo(dictDto);
			if(ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
				return vos.get(0);
			}
		}
		return null;
	}
	
	@Override
	public void insert(DictVO dictVo) {
		if(StrUtil.isNotBlank(dictVo.getCode())
				&& StrUtil.isNotBlank(dictVo.getName())) {
			String parentId = StrUtil.blankToDefault(dictVo.getParentId(), null);
			String dictType = StrUtil.isBlank(parentId) ? DictConsts.DICT_TYPE_01 : DictConsts.DICT_TYPE_02;

			dictVo.setId(IdUtil.fastSimpleUUID());
			dictVo.setParentId(parentId);
			dictVo.setDictType(dictType);
			dictVo.setCreateTime(DateUtil.date());
			dictVo.setUpdateTime(DateUtil.date());
			dictVo.setCreateBy(HttpServletUtil.getUsername());
			dictVo.setUpdateBy(HttpServletUtil.getUsername());

			boolean isUnique = this.checkUnique(dictVo.getCode(), dictVo.getDictType(), dictVo.getId(), dictVo.getParentId());
			if(!isUnique) {
				throw new ActionException("编码类型重复");
			}

			dictMapper.insert(dictVo);
		}
	}
	
	@Override
	public void insertBatch(List<DictVO> dictVos) {
		if(ObjectUtil.isNotEmpty(dictVos)) {
			dictMapper.insertBatch(dictVos);
		}
	}

	@Override
	public void update(DictVO dictVo) {
		if(StrUtil.isNotBlank(dictVo.getId())
				&& StrUtil.isNotBlank(dictVo.getCode())
				&& StrUtil.isNotBlank(dictVo.getName())) {
			DictVO old = this.selectById(dictVo.getId());
			if(ObjectUtil.isNull(old)) {
				throw new ServiceException("修改的编码类型不存在");
			}
			if(StrUtil.equals(old.getDictType(), DictConsts.DICT_TYPE_02)
					&& StrUtil.isBlank(dictVo.getParentId())) {
				throw new ServiceException("父级主键不能为空");
			}

			dictVo.setParentId(old.getParentId());
			dictVo.setDictType(old.getDictType());
			dictVo.setCreateTime(old.getCreateTime());
			dictVo.setCreateBy(old.getCreateBy());
			dictVo.setUpdateTime(DateUtil.date());
			dictVo.setUpdateBy(HttpServletUtil.getUsername());

			boolean isUnique = this.checkUnique(dictVo.getCode(), dictVo.getDictType(), dictVo.getId(), dictVo.getParentId());
			if(!isUnique) {
				throw new ActionException("编码类型重复");
			}

			dictMapper.updateById(dictVo);
		}
	}

	@Override
	public void deleteBatchIds(List<String> ids) {
		if(ObjectUtil.isNotEmpty(ids)) {
			DictDTO dictDto = new DictDTO();
			dictDto.setIds(ids);
			List<DictVO> dictVos = dictMapper.selectPageVo(dictDto);
			if(ObjectUtil.isNotEmpty(dictVos)) {
				dictVos.forEach(item -> {
					if(StrUtil.equals(item.getDictType(), DictConsts.DICT_TYPE_01) && item.getChildNum() > 0) {
						throw new ServiceException(StrUtil.format("编码[{}]的子类型数量为{}，请先删除子类型", item.getCode(), item.getChildNum()));
					}
				});
				dictMapper.deleteBatchIds(ids);
			}
		}
	}

	@Override
	public void deleteByParentId(String parentId) {
		if(StrUtil.isNotBlank(parentId)) {
			LambdaUpdateWrapper<DictDO> wrapper = Wrappers.lambdaUpdate();
			wrapper.eq(DictDO::getParentId, parentId);
			dictMapper.delete(wrapper);
		}
	}

	@Override
	public void refreshCache() {
		List<DictVO> vos = dictMapper.selectPageVo(new DictDTO());
		log.info("将字典编码从redis中删除");
		DictCacheUtil.deleteCache();
		log.info("将字典编码缓存到redis中#start");
		JSONObject dictKeys = new JSONObject();
		JSONObject dictMaps = new JSONObject();
		vos.forEach(item -> {
			if(StrUtil.equals(DictConsts.DICT_TYPE_01, item.getDictType())) {
				dictKeys.put(item.getId(), item.getCode());
				dictMaps.put(item.getCode(), new JSONObject());
			}
		});
		vos.forEach(item -> {
			if(StrUtil.equals(DictConsts.DICT_TYPE_02, item.getDictType())) {
				JSONObject dictMap = dictMaps.getJSONObject(dictKeys.getString(item.getParentId()));
				if(ObjectUtil.isNotNull(dictMap)) {
					dictMap.put(item.getCode(), item.getName());
				}
			}
		});
		dictMaps.forEach((dictKey, dictMap) -> {
			DictCacheUtil.setDictMap(dictKey, dictMap.toString());
		});
		log.info("将字典编码缓存到redis中#end");
	}

	@Override
	public boolean checkUnique(String code, String dictType, String id, String parentId) {
		LambdaQueryWrapper<DictDO> query = Wrappers.lambdaQuery();
		query.eq(DictDO::getCode, code);
		query.eq(DictDO::getDictType, dictType);
		if(StrUtil.isNotBlank(id)) {
			query.ne(DictDO::getId, id);
		}
		if(StrUtil.isNotBlank(parentId)) {
			query.eq(DictDO::getParentId, parentId);
		}
		int count = dictMapper.selectCount(query);
		return count == 0;
	}

}
