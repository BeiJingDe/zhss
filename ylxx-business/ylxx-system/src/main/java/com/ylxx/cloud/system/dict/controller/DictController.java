package com.ylxx.cloud.system.dict.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.system.dict.consts.DictConsts;
import com.ylxx.cloud.system.dict.model.DictDTO;
import com.ylxx.cloud.system.dict.model.DictVO;
import com.ylxx.cloud.system.dict.service.IDictService;
import com.ylxx.cloud.system.dict.util.DictCacheUtil;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据字典 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@RestController
@Api(tags = "系统管理：数据字典控制层")
@RequestMapping(ApiVersionConsts.V1 + "/system/dict")
@LogAnnotation(module = "数据字典")
public class DictController extends BaseController {

	@Resource
	private IDictService dictService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询字典编码种类", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询字典编码种类")
	public ApiResult selectPageVo(@Validated DictDTO dictDto) {
		if(StrUtil.isBlank(dictDto.getOrders())) {
			dictDto.setOrders("[{column:'T1.update_time',asc:false}]");
		}
		Page<DictVO> result = dictService.selectPageVo(dictDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/insert")
	@LogAnnotation(value = "新增字典编码", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "新增字典编码")
	public ApiResult insert(@RequestBody @Validated DictVO dictVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, dictVo);

		dictService.insert(dictVo);
		return ApiResultBuilder.success(dictVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "修改字典信息", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改字典信息")
	public ApiResult update(@RequestBody @Validated DictVO dictVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, dictVo);

		dictService.update(dictVo);
		return ApiResultBuilder.success(dictVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除字典", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除字典")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		dictService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}

	@GetMapping("/refresh-cache")
	@LogAnnotation(value = "刷新数据字典缓存", operateType = OperateTypeEnum.REFRESH)
	@ApiOperation(value = "刷新数据字典缓存")
	public ApiResult refreshCache() {
		dictService.refreshCache();
		return ApiResultBuilder.success().setMessage("缓存刷新成功");
	}
	
	@GetMapping("/cache-value")
	@LogAnnotation(value = "获取缓存中的数据字典", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "获取缓存中的数据字典")
	public ApiResult getCacheValue() {
		JSONObject dictMaps = new JSONObject();

		Set<String> keys = DictCacheUtil.getAllKeys();
		for(String key : keys) {
			String dictKey = key.split(SystemConsts.CONNECTOR)[1];
			String dictMap = DictCacheUtil.getDictMap(dictKey);
			dictMaps.put(dictKey, JSON.parse(dictMap));
		}

		return ApiResultBuilder.success(dictMaps);
	}

	@GetMapping("/export")
	@LogAnnotation(value = "数据导出", operateType = OperateTypeEnum.EXPORT)
	@ApiOperation(value = "数据导出")
	public void export(@Validated DictDTO dictDto) {
		dictDto.setCurrent(1);
		dictDto.setSize(SystemConsts.MAX_PAGE_SIZE);
		ApiResult<Page<DictVO>> result = this.selectPageVo(dictDto);
		List<DictVO> list = CollUtil.newArrayList();
		if(ObjectUtil.isNotNull(result.getData()) && ObjectUtil.isNotEmpty(result.getData().getRecords())) {
			list = result.getData().getRecords();
		}
		if(StrUtil.equals(dictDto.getDictType(), DictConsts.DICT_TYPE_01)) {
			ExcelUtil<DictVO> excelUtil = new ExcelUtil<>(DictVO.class, 1);
			excelUtil.exportExcel(list, "数据导出", HttpServletUtil.getResponse());
		} else if(StrUtil.equals(dictDto.getDictType(), DictConsts.DICT_TYPE_02)) {
			ExcelUtil<DictVO> excelUtil = new ExcelUtil<>(DictVO.class, 2);
			excelUtil.exportExcel(list, "数据导出", HttpServletUtil.getResponse());
		} else {
			ExcelUtil.export(DictVO.class, list, "数据导出", HttpServletUtil.getResponse());
		}
	}

}

