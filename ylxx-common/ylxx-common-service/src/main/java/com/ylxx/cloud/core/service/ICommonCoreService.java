package com.ylxx.cloud.core.service;


import java.util.Date;
import java.util.Map;

public interface ICommonCoreService {

    /**
     * 返回数据库和服务器的当前时间
     * @return
     */
    Map<String, Object> now();

}
