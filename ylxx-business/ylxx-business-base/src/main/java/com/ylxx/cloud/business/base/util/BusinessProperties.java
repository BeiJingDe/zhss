package com.ylxx.cloud.business.base.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author caixiaopeng
 * @ClassName BusinessProperties
 * Description: TODO
 * @date 2021-03-03 10:34:25
 */
@Configuration
@ConfigurationProperties("my")
@Data
public class BusinessProperties {

    private Netty netty = new Netty();
    private Rabbitmq rabbitmq = new Rabbitmq();
    private Cron cron = new Cron();

    @Data
    public class Netty {
        private Integer serverPort;
    }
    @Data
    public class Rabbitmq {
        // 设备上报数据
        private String queueMonitorData;
        // 设备响应数据
        private String queueResponseData;
        // 设备上线下线数据
        private String queueConnectData;
    }
    @Data
    public class Cron {
        // 定时任务：采集气象数据
        private String asyncBatchKwhReading;
        // 定时任务：同步外网数据到内网数据库
        private String asyncBatchDataTime;
        // 定时任务：获取内网数据到外网数据库
        private String refreshWeatherInfo;
    }

}
