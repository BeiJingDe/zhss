package com.ylxx.cloud.fdfs.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.fdfs.service.IFdfsService;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FastDFS文件上传 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-18
 */
//@RestController
//@Api(tags = "附件管理：FastDFS文件上传控制层")
//@RequestMapping(ApiVersionConsts.V1 + "/fdfs")
public class FdfsController extends BaseController {

    @Resource
    private IFdfsService fdfsService;

    @PostMapping("/upload")
    @LogAnnotation(value = "FastDFS上传文件", operateType = OperateTypeEnum.UPLOAD)
    @ApiOperation(value = "FastDFS上传文件", httpMethod = "POST")
    public ApiResult upload(FileVO fileVo, MultipartFile[] file) {
        List<FileVO> fileVos = fdfsService.upload(fileVo, file);
        List<String> ids = CollUtil.newArrayList();
        if(ObjectUtil.isNotEmpty(fileVos)) {
            ids = fileVos.stream().map(m -> m.getId()).collect(Collectors.toList());
        }
        return ApiResultBuilder.success(CollUtil.join(ids, ","));
    }

}
