package com.ylxx.cloud.obs.service.impl;

import com.ylxx.cloud.obs.service.ObsService;
import com.ylxx.cloud.obs.util.AWSUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author Z-7
 * @Date 2022/8/23
 */
@Service
public class IObsServiceImpl implements ObsService {
    @Resource
    private AWSUtil awsUtil;

    /**
     * 上传文件
     *
     * @param key  资源目录
     * @param file 文件
     * @return
     */
    @Override
    public String uploadFile(String key, MultipartFile file) {
        try {
            File toFile = awsUtil.multipartFileToFile(file);
            return awsUtil.uploadObject(key, toFile);
        } catch (IOException e) {
            throw new RuntimeException("multipartFileToFile异常");
        }
    }

    /**
     * 删除文件
     *
     * @param key 示例值 key = xxxx/xxx.txt
     */
    @Override
    public void deleteFile(String key) {
        // 没有返回值，有待测试
        awsUtil.deleteFile(key);
    }

    /**
     * 下载文件
     *
     * @param key 删除指定文件 示例值：key = 目录/文件名
     * @return 待定
     */
    @Override
    public String downloadFile(String key) {
        return awsUtil.downloadFile(key);
    }

    /**
     * 列出一个桶内的所有文件
     *
     * @param prefix 文件前缀 可为空
     */
    @Override
    public List<String> selectFiles(String prefix) {
        return awsUtil.selectFiles(prefix);
    }
}
