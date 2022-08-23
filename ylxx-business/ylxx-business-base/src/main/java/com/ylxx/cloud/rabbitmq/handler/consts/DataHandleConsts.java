package com.ylxx.cloud.rabbitmq.handler.consts;

import com.ylxx.cloud.core.system.SystemConsts;

/**
 * @author caixiaopeng
 * @ClassName DataHandleConsts
 * Description: TODO
 * @date 2021-01-27 19:51:00
 */
public class DataHandleConsts {

    /**
     * redis缓存key前缀
     */
    public static final String PREFIX_IMEI = SystemConsts.PREFIX + "DATA_HANDLE_IMEI";
    public static final String PREFIX_CHID = SystemConsts.PREFIX + "DATA_HANDLE_CHID";

    // 如果设备端超过这个时间没有上报数据，说明已经掉线。（设备端5分钟上报一次，但是判断超时要大于5分钟）
    public static final int idleTime = 10;

    // 协议开始（4个字节）
    public static final int start = 0xFC5AA5FC;
    // 功能码（1个字节）
    public static final int FUNC_CODE_TYPE1 = 0x20; // 主动上报数据格式
    public static final int FUNC_CODE_TYPE2 = 0x03; // 平台主动读数据格式（返回主动上报数据格式）
    public static final int FUNC_CODE_TYPE3 = 0x10; // 平台主动写数据格式（版本1：一条命令写一个寄存器）
    public static final int FUNC_CODE_TYPE4 = 0x30; // 平台主动写数据格式（版本2：一条命令写多个寄存器）
    // 协议结束（1个字节）
    public static final int end = 0x16;
    // 报文末尾分隔符
    public static final long endSplit = 0xA5A5A5A55A5A5A5Al;

    // 报文数据字节最小长度（起始4+从机地址（终端编码）16+功能码1+寄存器地址2+字节数2+数据n+和检验1+结束符1）
    public static final int messageMinLength = 4+16+1+2+2+1+1;
    // 报文数据字节最大长度
    public static final int messageMaxLength = 512;


    // 电表数据的键值范围（电表的数据去掉了）
    public static final int[] elecDataRange = new int[] {10011, 10027};
    // 空调数据的键值范围1
    public static final int[] airDataRange1 = new int[] {10028, 10066};
    // 空调数据的键值范围2
    public static final int[] airDataRange2 = new int[] {20001, 20024};
    // 空调数据的键值范围3
    public static final int[] airDataRange3 = new int[] {10067, 10068};

}
