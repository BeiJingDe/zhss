package com.ylxx.cloud.system.appversion.controller;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
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
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.system.appversion.model.AppVersionDO;
import com.ylxx.cloud.system.appversion.model.AppVersionDTO;
import com.ylxx.cloud.system.appversion.model.AppVersionVO;
import com.ylxx.cloud.system.appversion.service.IAppVersionService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * ??????APP???????????? ???????????????
 *
 * @author caixiaopeng
 * @since 2021-01-14
 */
@RestController
@Api(tags = "?????????????????????APP?????????????????????")
@RequestMapping(ApiVersionConsts.V1 + "/system/appversion")
public class AppVersionController extends BaseController {

	@Resource
	private IAppVersionService appVersionService;
	
	@GetMapping
	@LogAnnotation(value = "????????????APP????????????", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "????????????APP????????????")
	public ApiResult<Page<AppVersionVO>> selectPageVo(@Validated AppVersionDTO appVersionDto) {
		Page<AppVersionVO> result = appVersionService.selectPageVo(appVersionDto);

		if(ObjectUtil.isNotEmpty(result.getRecords())) {
			// ???????????????2????????????
			// 1. ??????java?????????????????????
			// 		http://192.168.0.20:9000/api/v1/file/download?id=2350f6d067df44abaf49edae699548ba
			// 2. ????????????nginx????????????????????????
			// 		http://192.168.0.20:18080/storage/202101/2350f6d067df44abaf49edae699548ba.apk?attname=__UNI__863C404_20210106184807.apk
			String uploadPath = ConfigCacheValue.FILE_KEY_UPLOAD_PATH();
			result.getRecords().forEach(item -> {
				if(!StrUtil.hasBlank(uploadPath, item.getFilePath())) {
					String filePath = item.getFilePath().replace(uploadPath, "/storage");
					item.setFilePath(filePath);
				}
			});
		}

		return ApiResultBuilder.success(result);
	}
	
	@PostMapping("/insert")
	@LogAnnotation(value = "??????APP??????", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "??????APP??????")
	public ApiResult insert(@RequestBody @Validated AppVersionVO appVersionVo) {
		// ??????????????????
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, appVersionVo);

		appVersionService.insert(appVersionVo);
		return ApiResultBuilder.success(appVersionVo.getId()).setMessage("????????????");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "??????APP??????", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "??????APP??????")
	public ApiResult update(@RequestBody @Validated AppVersionVO appVersionVo) {
		// ??????????????????
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, appVersionVo);

		appVersionService.update(appVersionVo);
		return ApiResultBuilder.success(appVersionVo.getId()).setMessage("????????????");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "????????????APP??????", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "????????????APP??????")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		appVersionService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("????????????");
	}

	@PostMapping("/publish")
	@LogAnnotation(value = "APP????????????", operateType = OperateTypeEnum.PUBLISH)
	@ApiOperation(value = "APP????????????")
	public ApiResult publish(@RequestBody @Validated AppVersionVO appVersionVo) {
		if(StrUtil.isBlank(appVersionVo.getId())) {
			throw new ActionException("??????ID????????????");
		}
		appVersionService.publish(appVersionVo.getId());
		return ApiResultBuilder.success(appVersionVo.getId()).setMessage("????????????");
	}

//	@GetMapping("/export")
//	@LogAnnotation(value = "????????????", operateType = OperateTypeEnum.EXPORT)
//	@ApiOperation(value = "????????????")
	public void export(@Validated AppVersionDTO appVersionDto) {
		appVersionDto.setCurrent(1);
    	appVersionDto.setSize(SystemConsts.MAX_PAGE_SIZE);
		ApiResult<Page<AppVersionVO>> result = this.selectPageVo(appVersionDto);
		List<AppVersionVO> list = CollUtil.newArrayList();
		if(ObjectUtil.isNotNull(result.getData()) && ObjectUtil.isNotEmpty(result.getData().getRecords())) {
			list = result.getData().getRecords();
		}
		ExcelUtil.export(AppVersionVO.class, list, "????????????", HttpServletUtil.getResponse());
	}

//	@GetMapping("/template")
//	@LogAnnotation(value = "????????????", operateType = OperateTypeEnum.EXPORT)
//	@ApiOperation(value = "????????????")
	public void template() {
		List<AppVersionVO> list = CollUtil.newArrayList();
		ExcelUtil.export(AppVersionVO.class, list, "????????????", HttpServletUtil.getResponse());
	}

//	@PostMapping("/import")
//	@LogAnnotation(value = "????????????", operateType = OperateTypeEnum.IMPORT)
//	@ApiOperation(value = "????????????")
	public ApiResult importData(MultipartFile file) {
		appVersionService.importData(file);
		return ApiResultBuilder.success().setMessage("??????????????????");
	}

	@GetMapping("/latest")
	@LogAnnotation(value = "???????????????app??????", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "???????????????app??????")
	public ApiResult latest() {
		AppVersionDTO appVersionDto = new AppVersionDTO();
		appVersionDto.setCurrent(1);
		appVersionDto.setSize(1);
		appVersionDto.setOrders("[{column:'T1.sort_no',asc:false}]");
		appVersionDto.setIsPublish(ConfigConsts.VALUE_YES_1);
		ApiResult<Page<AppVersionVO>> result = this.selectPageVo(appVersionDto);

		if(ObjectUtil.isNotNull(result.getData()) && ObjectUtil.isNotEmpty(result.getData().getRecords())) {
			return ApiResultBuilder.success(result.getData().getRecords().get(0));
		}
		return ApiResultBuilder.success(null);
	}
	
}

