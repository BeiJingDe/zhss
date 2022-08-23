package com.ylxx.cloud.business.base.util;

import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;

/**
 * 业务性缓存的配置参数
 */
public class BusinessConfigCacheValue {

    // netty服务器和rabbitmq接收到数据是否打印出来（0：否；1：是）
    public static String CTMS_KEY_ENABLE_MESSAGE_LOG() {
        return ConfigCacheUtil.getConfig("CTMS", "ENABLE_MESSAGE_LOG", ConfigConsts.VALUE_YES_1); }
    // 烤烟终端质保描述
    public static String CTMS_KEY_TERMINAL_WARRANTY_DESC() {
        return ConfigCacheUtil.getConfig("CTMS", "TERMINAL_WARRANTY_DESC"); }
    // 是否保存烤烟上报的报文解析数据（0：否；1：是）
    public static String CTMS_KEY_IS_SAVE_HISTORY_DATA() {
        return ConfigCacheUtil.getConfig("CTMS", "IS_SAVE_HISTORY_DATA", ConfigConsts.VALUE_YES_1); }
    // 	电烤烟单烤能耗成本（元）
    public static String CTMS_KEY_ELEC_TOAST_SINGLE_COST() {
        return ConfigCacheUtil.getConfig("CTMS", "ELEC_TOAST_SINGLE_COST"); }
    // 碳烤烟单烤能耗成本（元）
    public static String CTMS_KEY_CARBON_TOAST_SINGLE_COST() {
        return ConfigCacheUtil.getConfig("CTMS", "CARBON_TOAST_SINGLE_COST"); }
    // 生物质烤烟单烤能耗成本（元）
    public static String CTMS_KEY_BIOMASS_TOAST_SINGLE_COST() {
        return ConfigCacheUtil.getConfig("CTMS", "BIOMASS_TOAST_SINGLE_COST"); }
    // 	三明市烤烟季执行月份（电价0.4904元，全三明统一，执行月份4-7月）
    public static String CTMS_KEY_SAN_MING_SPECIAL_MONTH() {
        return ConfigCacheUtil.getConfig("CTMS", "SAN_MING_SPECIAL_MONTH"); }
    // 	三明市烤烟季每度电收费标准（电价0.4904元，全三明统一，执行月份4-7月）
    public static String CTMS_KEY_SAN_MING_ELEC_STANDARD_SPECIAL() {
        return ConfigCacheUtil.getConfig("CTMS", "SAN_MING_ELEC_STANDARD_SPECIAL"); }
    // 三明市农业电价每度电收费标准（电价0.5750元）
    public static String CTMS_KEY_SAN_MING_ELEC_STANDARD_FARMING() {
        return ConfigCacheUtil.getConfig("CTMS", "SAN_MING_ELEC_STANDARD_FARMING"); }
    // 1度电，相较于标准媒，减排二氧化碳0.897千克“二氧化碳”，0.027千克“二氧化硫”
    public static String CTMS_KEY_REDUCE_EMISSIONS_CO2() {
        return ConfigCacheUtil.getConfig("CTMS", "REDUCE_EMISSIONS_CO2"); }
    // 1度电，相较于标准媒，减排二氧化碳0.897千克“二氧化碳”，0.027千克“二氧化硫”
    public static String CTMS_KEY_REDUCE_EMISSIONS_SO2() {
        return ConfigCacheUtil.getConfig("CTMS", "REDUCE_EMISSIONS_SO2"); }
    // 是否开启烤烟批次电量数据同步任务（0：否；1：是）
    public static String CTMS_KEY_ENABLE_BATCH_ENERGY_ASYNC() {
        return ConfigCacheUtil.getConfig("CTMS", "ENABLE_BATCH_ENERGY_ASYNC"); }
    // 烤烟上报报文是否需要进行和校验（0：否；1：是）
    public static String CTMS_KEY_ENABLE_MESSAGE_SUM_CHECK() {
        return ConfigCacheUtil.getConfig("CTMS", "ENABLE_MESSAGE_SUM_CHECK", ConfigConsts.VALUE_YES_1); }
    // 烤烟机上报报文处理队列数量1~5个（根据终端编号hash到不同队列，然后多线程处理，加快处理速度）
    public static String CTMS_KEY_DATA_HANDLE_QUEUE_NUM() {
        return ConfigCacheUtil.getConfig("CTMS", "DATA_HANDLE_QUEUE_NUM", "1"); }

}
