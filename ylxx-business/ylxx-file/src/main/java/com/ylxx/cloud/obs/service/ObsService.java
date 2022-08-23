package com.ylxx.cloud.obs.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Z-7
 * @Date 2022/8/23
 */
public interface ObsService {
    /**
     * 上传文件
     * @param key 资源目录
     * @param file 文件
     * @return
     */
    String uploadFile(String key,MultipartFile file);

    /**
     * 删除文件
     * @param key 示例值 key = xxxx/xxx.txt
     */
    void deleteFile(String key);

    /**
     * 下载文件
     * @param key 删除指定文件 示例值：key = 目录/文件名
     * @return 待定
     */
    String downloadFile(String key);

    /**
     * 列出一个桶内的所有文件
     * @param prefix 文件前缀 可为空
     */
    List<String> selectFiles(String prefix);
}
