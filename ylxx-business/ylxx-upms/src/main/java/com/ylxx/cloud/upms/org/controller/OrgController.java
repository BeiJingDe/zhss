package com.ylxx.cloud.upms.org.controller;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.upms.org.model.OrgDO;
import com.ylxx.cloud.upms.org.model.OrgDTO;
import com.ylxx.cloud.upms.org.model.OrgVO;
import com.ylxx.cloud.upms.org.service.IOrgService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 组织信息表 前端控制器
 *
 * @author caixiaopeng
 * @since 2021-03-24
 */
@RestController
@Api(tags = "系统管理：组织信息表控制层")
@RequestMapping(ApiVersionConsts.V1 + "/upms/org")
public class OrgController extends BaseController {

	@Resource
	private IOrgService orgService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询")
	public ApiResult<Page<OrgVO>> selectPageVo(@Validated OrgDTO orgDto) {
		Page<OrgVO> result = orgService.selectPageVo(orgDto);
		return ApiResultBuilder.success(result);
	}
	
	@PostMapping("/insert")
	@LogAnnotation(value = "新增记录", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "新增记录")
	public ApiResult insert(@RequestBody @Validated OrgVO orgVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, orgVo);

		orgService.insert(orgVo);
		return ApiResultBuilder.success(orgVo.getId()).setMessage("操作成功");
	}
	
	@PostMapping("/update")
	@LogAnnotation(value = "修改记录", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改记录")
	public ApiResult update(@RequestBody @Validated OrgVO orgVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, orgVo);

		orgService.update(orgVo);
		return ApiResultBuilder.success(orgVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		orgService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}

	@GetMapping("/export")
	@LogAnnotation(value = "数据导出", operateType = OperateTypeEnum.EXPORT)
	@ApiOperation(value = "数据导出")
	public void export(@Validated OrgDTO orgDto) {
		orgDto.setCurrent(1);
    	orgDto.setSize(SystemConsts.MAX_PAGE_SIZE);
		ApiResult<Page<OrgVO>> result = this.selectPageVo(orgDto);
		List<OrgVO> list = CollUtil.newArrayList();
		if(ObjectUtil.isNotNull(result.getData()) && ObjectUtil.isNotEmpty(result.getData().getRecords())) {
			list = result.getData().getRecords();
		}
		ExcelUtil.export(OrgVO.class, list, "数据导出", HttpServletUtil.getResponse());
	}

	@GetMapping("/template")
	@LogAnnotation(value = "模板导出", operateType = OperateTypeEnum.EXPORT)
	@ApiOperation(value = "模板导出")
	public void template() {
		List<OrgVO> list = CollUtil.newArrayList();
		ExcelUtil.export(OrgVO.class, list, "模板导出", HttpServletUtil.getResponse());
	}

	@PostMapping("/import")
	@LogAnnotation(value = "数据导入", operateType = OperateTypeEnum.IMPORT)
	@ApiOperation(value = "数据导入")
	public ApiResult importData(MultipartFile file) {
		orgService.importData(file);
		return ApiResultBuilder.success().setMessage("操作成功");
	}
	
}

