package com.ylxx.cloud.rabbitmq.handler.model;

import lombok.Getter;

/**
 * @author caixiaopeng
 * @ClassName DataHandleModBus
 * Description: TODO
 * @date 2021-01-28 19:40:59
 */
@Getter
public class DataHandleModBus {

    private int v10001=0x00; //IMEI
    private int v10002=0x08; //IMSI
    private int v10003=0x10; //ICCID
    private int v10004=0x1A; //保留
    private int v10005=0x1C; //RSRP
    private int v10006=0x1D; //保留
    private int v10007=0x1E; //RSSI
    private int v10008=0x00; //保留
    private int v10009=0x1F; //保留
    private int v10010=0x00; //保留

    private int v10028=0x20; //设备地址（从机地址）
    private int v10029=0x21; //厂家/设备型号/版本号
    private int v10030=0x22; //开始烘烤段号
    private int v10031=0x23; //实际烘烤总段数
    private int v10032=0x24; //烤房设备状态
    private int v10033=0x25; //烤房故障标识
    private int v10034=0x26; //压机1故障标识
    private int v10035=0x27; //压机2故障标识
    private int v10036=0x28; //烤房工作状态
    private int v10037=0x29; //上棚干球实时温度
    private int v10038=0x2A; //上棚湿球实时温度
    private int v10039=0x2B; //上棚湿度
    private int v10040=0x2C; //下棚干球实时温度
    private int v10041=0x2D; //下棚湿球实时温度
    private int v10042=0x2E; //下棚湿度
    private int v10043=0x2F; //当前干球设定值
    private int v10044=0x30; //当前湿球设定值
    private int v10045=0x31; //进风风门角度/档位
    private int v10046=0x32; //排湿风门角度/档位
    private int v10047=0x33; //干球温度设定1
    private int v10048=0x34; //干球温度设定2
    private int v10049=0x35; //干球温度设定3
    private int v10050=0x36; //干球温度设定4
    private int v10051=0x37; //干球温度设定5
    private int v10052=0x38; //干球温度设定6
    private int v10053=0x39; //干球温度设定7
    private int v10054=0x3A; //干球温度设定8
    private int v10055=0x3B; //干球温度设定9
    private int v10056=0x3C; //干球温度设定10
    private int v10057=0x3D; //湿球温度设定1
    private int v10058=0x3E; //湿球温度设定2
    private int v10059=0x3F; //湿球温度设定3
    private int v10060=0x40; //湿球温度设定4
    private int v10061=0x41; //湿球温度设定5
    private int v10062=0x42; //湿球温度设定6
    private int v10063=0x43; //湿球温度设定7
    private int v10064=0x44; //湿球温度设定8
    private int v10065=0x45; //湿球温度设定9
    private int v10066=0x46; //湿球温度设定10
    private int v20001=0x47; //温升时间1
    private int v20002=0x48; //恒温时间1
    private int v20003=0x49; //温升时间2
    private int v20004=0x4A; //恒温时间2
    private int v20005=0x4B; //温升时间3
    private int v20006=0x4C; //恒温时间3
    private int v20007=0x4D; //温升时间4
    private int v20008=0x4E; //恒温时间4
    private int v20009=0x4F; //温升时间5
    private int v20010=0x50; //恒温时间5
    private int v20011=0x51; //温升时间6
    private int v20012=0x52; //恒温时间6
    private int v20013=0x53; //温升时间7
    private int v20014=0x54; //恒温时间7
    private int v20015=0x55; //温升时间8
    private int v20016=0x56; //恒温时间8
    private int v20017=0x57; //温升时间9
    private int v20018=0x58; //恒温时间9
    private int v20019=0x59; //温升时间10
    private int v20020=0x5A; //恒温时间10
    private int v20021=0x5B; //干球温度上回差设定
    private int v20022=0x5C; //干球温度下回差设定
    private int v20023=0x5D; //湿球温度上回差设定
    private int v20024=0x5E; //湿球温度下回差设定
    private int v10067=0x5F; //当前工作段号
    private int v10068=0x60; //当前烘烤阶段已运行时间

    private int v10069=0x61; //保留
    private int v10070=0x62; //保留
    private int v10071=0x63; //保留
    private int v10072=0x64; //IP
    private int v10073=0x65; //PORT
    private int v10074=0x66; //故障码
    private int v10075=0x67; //保留
    private int v10076=0x68; //上传周期
    private int v10077=0x69; //空调通信IP
    private int v10078=0x00; //电表通信IP
    private int v10079=0x6B; //保留
    private int v10080=0x00; //时间戳
    
}
