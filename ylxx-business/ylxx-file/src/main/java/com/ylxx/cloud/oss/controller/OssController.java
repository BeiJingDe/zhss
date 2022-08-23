package com.ylxx.cloud.oss.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.oss.service.IOssService;
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
 * OSS文件上传 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-18
 */
//@RestController
//@Api(tags = "附件管理：OSS文件上传控制层")
//@RequestMapping(ApiVersionConsts.V1 + "/oss")
public class OssController extends BaseController {

    @Resource
    private IOssService ossService;

    @PostMapping("/upload")
    @LogAnnotation(value = "OSS上传文件", operateType = OperateTypeEnum.UPLOAD)
    @ApiOperation(value = "OSS上传文件", httpMethod = "POST")
    public ApiResult upload(String belongId, MultipartFile[] file) {
        FileVO fileVo = new FileVO();
        fileVo.setBelongId(belongId);
        List<FileVO> fileVos = ossService.upload(fileVo, file);
        List<String> ids = CollUtil.newArrayList();
        if(ObjectUtil.isNotEmpty(fileVos)) {
            ids = fileVos.stream().map(m -> m.getId()).collect(Collectors.toList());
        }
        return ApiResultBuilder.success(CollUtil.join(ids, ","));
    }

    @PostMapping("/upload64")
    @LogAnnotation(value = "OSS上传文件（base64）", operateType = OperateTypeEnum.UPLOAD)
    @ApiOperation(value = "OSS上传文件（base64）", httpMethod = "POST")
    public ApiResult upload64(@RequestBody @Validated FileVO fileVo) {
        ossService.upload64(fileVo);
        return ApiResultBuilder.success(fileVo.getId());
    }

    @GetMapping("/download")
    @LogAnnotation(value = "OSS下载文件", operateType = OperateTypeEnum.DOWNLOAD)
    @ApiOperation(value = "OSS下载文件", httpMethod = "GET")
    public ApiResult download(String id) {
        ossService.download(id);
        return ApiResultBuilder.success().setMessage("下载成功");
    }

    @PostMapping("/download64")
    @LogAnnotation(value = "OSS下载文件", operateType = OperateTypeEnum.DOWNLOAD)
    @ApiOperation(value = "OSS下载文件", httpMethod = "POST")
    public ApiResult download64(@RequestBody @Validated FileVO fileVo) {
        fileVo = ossService.download64(fileVo.getId());
        return ApiResultBuilder.success(fileVo);
    }

    @GetMapping("/view-url")
    @LogAnnotation(value = "OSS文件预览", operateType = OperateTypeEnum.QUERY)
    @ApiOperation(value = "OSS文件预览", httpMethod = "GET")
    public ApiResult getViewUrl(String id) {
        String viewUrl = ossService.getViewUrl(id);
        return ApiResultBuilder.success(viewUrl);
    }

}
