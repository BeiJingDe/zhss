package com.ylxx.cloud.oss.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ylxx.cloud.core.util.EnvironmentUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OssUtil {

    /**
     * 创建OSS client对象
     * @return
     */
    public static OSS buildClient() {
        try {
            log.info("########## 创建OSS client对象 start ##########");
            String endpoint = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_ENDPOINT);
            log.info("endpoint = {}", endpoint);
            String accessKeyId = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_ACCESS_KEY_ID);
            log.info("accessKeyId = {}", accessKeyId);
            String accessKeySecret = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_ACCESS_KEY_SECRET);
            log.info("accessKeySecret = {}", accessKeySecret);
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            log.info("########## 创建OSS client对象 end ##########");
            return ossClient;
        } catch (Exception e) {
            throw new ServiceException("创建OSS client对象失败");
        }
    }

    /**
     * 关闭OSS client对象
     * @param ossClient
     */
    public static void shutdown(OSS ossClient) {
        if(ossClient != null) {
            try {
                ossClient.shutdown();
            } catch (Exception e) {
                // 不处理
            }
        }
    }

}
