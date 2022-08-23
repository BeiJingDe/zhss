package com.ylxx.cloud.localfs.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.localfs.service.ILocalFsService;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

//@RestController
//@Api(tags = "附件管理：本地文件上传控制层")
//@RequestMapping(ApiVersionConsts.V1 + "/localfs")
public class LocalFsController {

    @Resource
    private ILocalFsService localFsService;

    @PostMapping("/upload")
    @LogAnnotation(value = "本地上传文件", operateType = OperateTypeEnum.UPLOAD)
    @ApiOperation(value = "本地上传文件", httpMethod = "POST")
    public ApiResult upload(FileVO fileVo, MultipartFile[] file) {
        List<FileVO> fileVos = localFsService.upload(fileVo, file);
        List<String> ids = CollUtil.newArrayList();
        if(ObjectUtil.isNotEmpty(fileVos)) {
            ids = fileVos.stream().map(m -> m.getId()).collect(Collectors.toList());
        }
        return ApiResultBuilder.success(CollUtil.join(ids, ","));
    }

    @GetMapping("/download")
    @LogAnnotation(value = "下载文件", operateType = OperateTypeEnum.DOWNLOAD)
    @ApiOperation(value = "下载文件", httpMethod = "GET")
    public void download(@Validated FileVO fileVo) {
        localFsService.download(fileVo.getId());
    }

}
