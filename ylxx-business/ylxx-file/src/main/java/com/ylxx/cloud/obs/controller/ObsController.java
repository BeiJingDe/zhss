package com.ylxx.cloud.obs.controller;

import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.obs.service.ObsService;
import com.ylxx.cloud.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * OBS文件上传
 * @Author Z-7
 * @Date 2022/8/22
 */
@Slf4j
@RestController
@Api(tags = "OBS文件上传")
@RequestMapping("/obs")
public class ObsController {

    @Resource
    private ObsService obsService;

    @PostMapping("upload")
    @ApiOperation(value = "OBS文件上传", httpMethod = "POST")
    public ApiResult uploadTest(MultipartFile file) {
        String s = obsService.uploadFile("test",file);
        if (StringUtils.isBlank(s)) {
            return ApiResultBuilder.failed("文件上传失败");
        }
        return ApiResultBuilder.success();
    }

    @PostMapping("delete")
    @ApiOperation(value = "OBS删除文件", httpMethod = "POST")
    public ApiResult deleteTest(String fileName) {
        obsService.deleteFile(fileName);
        return ApiResultBuilder.failed("没有返回值，有待测试，先人工查看");
    }

    /**
     * 当前下载地址默认写死在D盘
     * @param fileName 文件名称 key在 bucket下  示例值 key = 文件夹名 / 文件名
     * @return
     */
    @PostMapping("download")
    @ApiOperation(value = "OBS下载文件", httpMethod = "POST")
    public ApiResult downloadTest(String fileName) {
        String result = obsService.downloadFile(fileName);
        if (!"下载完成".equals(result)) {
            return ApiResultBuilder.failed(result);
       }
        return ApiResultBuilder.success();

    }


    @PostMapping("select")
    @ApiOperation(value = "OBS下载文件", httpMethod = "POST")
    public ApiResult selectTest() {
        List<String> list = obsService.selectFiles(null);
        if (CollectionUtils.isEmpty(list)) {
            return ApiResultBuilder.failed("操作失败");
        }
        return ApiResultBuilder.success(list);
    }
}
