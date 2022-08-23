package com.ylxx.cloud.file.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.file.model.FileDTO;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.file.service.IFileService;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件管理表 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-18
 */
@RestController
@Api(tags = "附件管理：文件管理表控制层")
@RequestMapping(ApiVersionConsts.V1 + "/file")
public class FileController extends BaseController {

	@Resource
	private IFileService fileService;

	@GetMapping
	@LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询", httpMethod = "GET")
	public ApiResult selectPageVo(@Validated FileDTO fileDto) {
		Page<FileVO> result = fileService.selectPageVo(fileDto);
		return ApiResultBuilder.success(result);
	}
	
//	@GetMapping("/vos")
//	@LogAnnotation(value = "查询全部", operateType = OperateTypeEnum.QUERY)
//	@ApiOperation(value = "查询全部", httpMethod = "GET")
	public ApiResult selectVos(@Validated FileDTO fileDto) {
		if(StrUtil.isBlank(fileDto.getId())
				&& StrUtil.isBlank(fileDto.getBelongId())
				&& ObjectUtil.isEmpty(fileDto.getIds())) {
			throw new ActionException("参数id，belongId，ids不能同时为空");
		}
		List<FileVO> result = fileService.selectVos(fileDto);
		return ApiResultBuilder.success(result);
	}

//	@PostMapping("/insert")
//	@LogAnnotation(value = "新增记录", operateType = OperateTypeEnum.INSERT)
//	@ApiOperation(value = "新增记录", httpMethod = "POST")
	public ApiResult insert(@RequestBody @Validated FileVO fileVo) {
		fileService.insert(fileVo);
		return ApiResultBuilder.success(fileVo.getId());
	}

//	@PostMapping("/update")
//	@LogAnnotation(value = "修改记录", operateType = OperateTypeEnum.UPDATE)
//	@ApiOperation(value = "修改记录", httpMethod = "PUT")
	public ApiResult update(@RequestBody @Validated FileVO fileVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, fileVo);

		fileService.update(fileVo);
		return ApiResultBuilder.success(fileVo.getId());
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除", httpMethod = "POST")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		fileService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}

	@PostMapping("/upload")
	@LogAnnotation(value = "上传文件", operateType = OperateTypeEnum.UPLOAD)
	@ApiOperation(value = "上传文件")
	public ApiResult upload(FileVO fileVo, MultipartFile[] file) {
		// 应付演示，假上传
		String isRealUpload = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FILE, ConfigConsts.FILE_KEY_IS_REAL_UPLOAD);
		if (StrUtil.equals(isRealUpload, ConfigConsts.VALUE_NO_0)) {
			return ApiResultBuilder.success(IdUtil.fastSimpleUUID());
		}
//		FileVO fileVo = new FileVO();
//		fileVo.setBelongId(belongId);
		List<FileVO> fileVos = fileService.upload(fileVo, file);
		List<String> ids = CollUtil.newArrayList();
		if(ObjectUtil.isNotEmpty(fileVos)) {
			ids = fileVos.stream().map(m -> m.getId()).collect(Collectors.toList());
		}
		return ApiResultBuilder.success(CollUtil.join(ids, ","));
	}

	@GetMapping("/download")
	@LogAnnotation(value = "下载文件", operateType = OperateTypeEnum.DOWNLOAD)
	@ApiOperation(value = "下载文件")
	public void download(@Validated FileVO fileVo) {
		fileService.download(fileVo.getId());
	}
	
}

