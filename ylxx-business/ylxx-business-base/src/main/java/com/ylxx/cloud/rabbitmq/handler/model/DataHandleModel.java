package com.ylxx.cloud.rabbitmq.handler.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caixiaopeng
 * @ClassName DataHandleModel
 * Description: TODO
 * @date 2021-01-22 10:44:56
 */
@Data
public class DataHandleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String v10001; // IMEI    Int8*16
    private String v10002; // IMSI    Int8*16
    private String v10003; // ICCID    Int8*20
    private Object v10004; // 保留    Int32
    private Integer v10005; // RSRP    Int16
    private Object v10006; // 保留    Int16
    private Integer v10007; // RSSI    Int8
    private Object v10008; // 保留    Int8
    private Object v10009; // 保留    Int8
    private Object v10010; // 保留    Int8

    private Long v10011; // A 相电压    Int32    0.1V              （硬件设备上的电表已去掉，相应的电量数据也没了）
    private Long v10012; // B 相电压    Int32    0.1V
    private Long v10013; // C 相电压    Int32    0.1V
    private Long v10014; // A 相电流    Int32    0.001A
    private Long v10015; // B 相电流    Int32    0.001A
    private Long v10016; // C 相电流    Int32    0.001A
    private Long v10017; // 总有功功率    Int32    0.1W
    private Long v10018; // 总无功功率    Int32    0.1W
    private Long v10019; // 总功率因数    Int32    0~1.000
    private Long v10020; // A 相线电压    Int32    0.1V
    private Long v10021; // B 相线电压    Int32    0.1V
    private Long v10022; // C 相线电压    Int32    0.1V
    private Long v10023; // 当前总有功电能    Int32    0.01Kwh
    private Long v10024; // 当前正向总有功电能    Int32    0.01Kwh
    private Long v10025; // 当前反向总有功电能    Int32    0.01Kwh
    private Long v10026; // 当前正向总无功电能    Int32    0.01Kvarh
    private Long v10027; // 当前反向总无功电能    Int32    0.01Kvarh

    private Integer v10028; // 设备地址（从机地址）    Int16    默认为0x01，可修改，
    private Integer v10029; // 厂家/设备型号/版本号    Int16    可用于区别不同厂家，不同版本的控制器，没有则不填写
    private Integer v10030; // 开始烘烤段号    Int16    实际开始烘烤阶段，数值 1～20对应阶段1～20
    private Integer v10031; // 实际烘烤总段数    Int16    实际烘烤阶段之和，数值 1～20对应阶段1～20
    private Integer v10032; // 烤房设备状态
    private Integer v10033; // 烤房故障标识
    private Integer v10034; // 压机1故障标识
    private Integer v10035; // 压机2故障标识
    private Integer v10036; // 烤房工作状态    Int16    0-关机 1-开机
    private Integer v10037; // 上棚干球实时温度    Int16    单位：0.1℃，比如当前值653，表示65.3℃
    private Integer v10038; // 上棚湿球实时温度    Int16    单位：0.1℃，比如当前值653，表示65.3℃
    private Integer v10039; // 上棚湿度    Int16    单位：0.1%
    private Integer v10040; // 下棚干球实时温度    Int16    单位：0.1℃，比如当前值653，表示65.3℃
    private Integer v10041; // 下棚湿球实时温度    Int16    单位：0.1℃，比如当前值653，表示65.3℃
    private Integer v10042; // 下棚湿度    Int16    单位：0.1%
    private Integer v10043; // 当前干球设定值    Int16    单位：0.1℃，比如当前值653，表示65.3℃
    private Integer v10044; // 当前湿球设定值    Int16    单位：0.1℃，比如当前值653，表示65.3℃
    private Integer v10045; // 进风风门角度/档位    Int16    开度0~100%，新风风门角度
    private Integer v10046; // 排湿风门角度/档位    Int16    开度0~101%，新风风门角度
    private Integer v10047; // 干球温度设定1    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.5℃
    private Integer v10048; // 干球温度设定2    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.6℃
    private Integer v10049; // 干球温度设定3    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.7℃
    private Integer v10050; // 干球温度设定4    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.8℃
    private Integer v10051; // 干球温度设定5    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.9℃
    private Integer v10052; // 干球温度设定6    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.10℃
    private Integer v10053; // 干球温度设定7    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.11℃
    private Integer v10054; // 干球温度设定8    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.12℃
    private Integer v10055; // 干球温度设定9    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.13℃
    private Integer v10056; // 干球温度设定10    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.14℃
    private Integer v10057; // 湿球温度设定1    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.15℃
    private Integer v10058; // 湿球温度设定2    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.16℃
    private Integer v10059; // 湿球温度设定3    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.17℃
    private Integer v10060; // 湿球温度设定4    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.18℃
    private Integer v10061; // 湿球温度设定5    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.19℃
    private Integer v10062; // 湿球温度设定6    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.20℃
    private Integer v10063; // 湿球温度设定7    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.21℃
    private Integer v10064; // 湿球温度设定8    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.22℃
    private Integer v10065; // 湿球温度设定9    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.23℃
    private Integer v10066; // 湿球温度设定10    Int16    单位：0.1℃，比如，读数355，则实际控制温度35.24℃
    private Integer v20001; //	温升时间1	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20002; //	恒温时间1	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20003; //	温升时间2	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20004; //	恒温时间2	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20005; //	温升时间3	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20006; //	恒温时间3	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20007; //	温升时间4	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20008; //	恒温时间4	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20009; //	温升时间5	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20010; //	恒温时间5	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20011; //	温升时间6	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20012; //	恒温时间6	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20013; //	温升时间7	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20014; //	恒温时间7	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20015; //	温升时间8	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20016; //	恒温时间8	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20017; //	温升时间9	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20018; //	恒温时间9	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20019; //	温升时间10	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20020; //	恒温时间10	Int16   单位：0.1h，比如读数55，实际为5.5小时
    private Integer v20021; //	干球温度上回差设定	Int16   单位：0.1℃
    private Integer v20022; //	干球温度下回差设定	Int16   单位：0.1℃
    private Integer v20023; //	湿球温度上回差设定	Int16   单位：0.1℃
    private Integer v20024; //	湿球温度下回差设定	Int16   单位：0.1℃
    private Integer v10067; // 当前工作段号    Int16    当前正在烘烤阶段，数值 1～20对应阶段1～20
    private Integer v10068; // 当前烘烤阶段已运行时间    Int16    单位：分钟，当前烘烤阶段已运行时间，数值1表示1分钟，数值6表示0.1小时

    private Object v10069; // 保留    Int16
    private Object v10070; // 保留    Int16
    private Object v10071; // 保留    Int16
    private String v10072; // IP    Int32
    private Integer v10073; // PORT    Int16
    private Integer v10074; // 故障码    Int16
    private Object v10075; // 保留    Int16
    private Integer v10076; // 上传周期    Int16
    private Integer v10077; // 空调通信IP    Int8
    private Integer v10078; // 电表通信IP    Int8
    private Object v10079; // 保留    int16
    private Date v10080; // 时间戳    int32

}
