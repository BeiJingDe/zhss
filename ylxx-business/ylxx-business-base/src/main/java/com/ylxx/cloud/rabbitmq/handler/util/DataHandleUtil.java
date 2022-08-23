package com.ylxx.cloud.rabbitmq.handler.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ylxx.cloud.business.base.util.BusinessConfigCacheValue;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.rabbitmq.handler.consts.DataHandleConsts;
import com.ylxx.cloud.rabbitmq.handler.model.DataHandleModel;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.util.SumCheckUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author caixiaopeng
 * @ClassName DataHandleUtil
 * Description: TODO
 * @date 2021-01-05 21:22:41
 */
@Slf4j
public class DataHandleUtil {

    /**
     * 将烤烟机上报的二进制报文数据解析为model
     * @param body
     * @return
     */
    public static DataHandleModel parseModel(byte[] body) {
        ByteBuf byteBuf = null;
        try {
            byteBuf = Unpooled.wrappedBuffer(body);
            // ********** 内容解析，参照文档《通信协议.xls》 start ********** //
            // 1. 数据协议校验
            if(!isDataCorrect(byteBuf, DataHandleConsts.FUNC_CODE_TYPE1)) {
                throw new ServiceException("数据协议校验失败");
            }
            // 1.1 起始符，4个字节
            byteBuf.readInt();
            // 2.1 IMEI	Int8*16 (终端编号device_code)
            String IMEI = DataHandleUtil.parseIMEI(byteBuf);
            // 2.2 功能码，1个字节
            byteBuf.readByte();
            // 2.3 寄存器起始地址，2个字节
            byteBuf.readShort();
            // 2.4 字节数，2个字节
            byteBuf.readShort();
            // 2.5 IMSI	Int8*16
            String IMSI = DataHandleUtil.parseIMSI(byteBuf);
            // 2.6 ICCID Int8*20
            String ICCID = DataHandleUtil.parseICCID(byteBuf);
            // 2.7 保留	Int32
            byteBuf.readInt();
            // 2.8 RSRP	Int16
            int RSRP = DataHandleUtil.parseUInt16(byteBuf);
            // 2.9 保留	Int16
            byteBuf.readShort();
            // 2.10 RSSI    Int8
            int RSSI = DataHandleUtil.parseUInt8(byteBuf);
            // 2.11 保留	Int8
            byteBuf.readByte();
            // 2.12 保留	Int8
            byteBuf.readByte();
            // 2.13 保留	Int8
            byteBuf.readByte();
            // 2.14 空调的数据
            Map<Integer, Integer> airData = DataHandleUtil.parseAirData(byteBuf);
            // 2.15 保留	Int16
            byteBuf.readShort();
            // 2.16 保留	Int16
            byteBuf.readShort();
            // 2.17 保留	Int16
            byteBuf.readShort();
            // 2.18 IP	Int32
            String IP = DataHandleUtil.parseIP(byteBuf);
            // 2.19 PORT	Int16
            int PORT = DataHandleUtil.parseUInt16(byteBuf);
            // 2.20 故障码	Int16
            int faultCode = DataHandleUtil.parseUInt16(byteBuf);
            // 2.21 保留	Int16
            byteBuf.readShort();
            // 2.22 上传周期	Int16
            int uploadInterval = DataHandleUtil.parseUInt16(byteBuf);
            // 2.23 空调通信IP	Int8
            int airIP = DataHandleUtil.parseUInt8(byteBuf);
            // 2.24 电表通信IP	Int8
            int elecIP = DataHandleUtil.parseUInt8(byteBuf);
            // 2.25 保留	int16
            byteBuf.readShort();
            // 2.26 时间戳	int32
            Date dataTime = DataHandleUtil.parseDataTime(byteBuf);

            // 3.1 和校验值
            byteBuf.readByte();
            // 3.2 结束符
            byteBuf.readByte();
            // ********** 内容解析，参照文档《通信协议.xls》 end ********** //

            // ********** 数据封装 start ********** //
            DataHandleModel dataHandleModel = new DataHandleModel();
            dataHandleModel.setV10001(IMEI);
            dataHandleModel.setV10002(IMSI);
            dataHandleModel.setV10003(ICCID);
            dataHandleModel.setV10005(RSRP);
            dataHandleModel.setV10007(RSSI);
            airData.forEach((key, val) -> { ReflectUtil.setFieldValue(dataHandleModel, "v"+key, val); });
            dataHandleModel.setV10072(IP);
            dataHandleModel.setV10073(PORT);
            dataHandleModel.setV10074(faultCode);
            dataHandleModel.setV10076(uploadInterval);
            dataHandleModel.setV10077(airIP);
            dataHandleModel.setV10078(elecIP);
            dataHandleModel.setV10080(dataTime);
            // ********** 数据封装 end ********** //
            return dataHandleModel;
        } catch (Exception e) {
            throw new ServiceException(StrUtil.format("数据解析异常：{}", e.getMessage()));
        } finally {
            if(ObjectUtil.isNotNull(byteBuf)) {
                ReferenceCountUtil.release(byteBuf);
            }
        }
    }

    /**
     * 判断报文完整性（粗略判断）
     * @param in
     * @return
     */
    public static boolean isDataIntact(ByteBuf in) {
        if(in.readableBytes() >= DataHandleConsts.messageMinLength) {
            int start = in.getInt(0);
            int funcCode = in.getByte(20) & 0xff;
            int byteCountRead = in.getUnsignedShort(23);
            byte sumRead = in.getByte(in.readableBytes()-2);
            int end = in.getByte(in.readableBytes()-1) & 0xff;
            // 起始符合，功能码，字节数，和校验码，结束符（5项校验）
            if(start == DataHandleConsts.start
                    && end == DataHandleConsts.end
                    && byteCountRead == in.readableBytes()-DataHandleConsts.messageMinLength) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断报文正确性
     * @param in
     * @return
     */
    public static boolean isDataCorrect(ByteBuf in, int FUNC_CODE_TYPE) {
        if(in.readableBytes() >= DataHandleConsts.messageMinLength) {
            int start = in.getInt(0);
            int funcCode = in.getByte(20) & 0xff;
            int byteCountRead = in.getUnsignedShort(23);
            byte sumRead = in.getByte(in.readableBytes()-2);
            int end = in.getByte(in.readableBytes()-1) & 0xff;
            // 起始符合，功能码，字节数，和校验码，结束符（5项校验）
            if(start != DataHandleConsts.start) {
                log.error(StrUtil.format("数据异常：通信协议起始符[{}]不一致：{}", HexUtil.toHex(DataHandleConsts.start), HexUtil.toHex(start)));
                return false;
            }
            if(end != DataHandleConsts.end) {
                log.error(StrUtil.format("数据异常：通信协议结束符[{}]不一致：{}", HexUtil.toHex(DataHandleConsts.end), HexUtil.toHex(end)));
                return false;
            }
            if(funcCode != FUNC_CODE_TYPE) {
                log.error(StrUtil.format("数据异常：通信协议功能码[{}]不一致：{}", HexUtil.toHex(FUNC_CODE_TYPE), HexUtil.toHex(funcCode)));
                return false;
            }
            int byteCountCalc = in.readableBytes()-DataHandleConsts.messageMinLength;
            if(byteCountRead != byteCountCalc) {
                log.error(StrUtil.format("数据异常：通信协议字节数[{}]不一致：{}", byteCountCalc, byteCountRead));
                return false;
            }
            byte[] body = ByteBufUtil.getBytes(in);
            byte sumCalc = SumCheckUtil.sum(body, 4, body.length-4-2);
            if(sumRead != sumCalc && !StrUtil.equals(ConfigConsts.VALUE_NO_0, BusinessConfigCacheValue.CTMS_KEY_ENABLE_MESSAGE_SUM_CHECK())) {
                log.error(StrUtil.format("数据异常：通信协议和校验[{}]不一致：{}", HexUtil.toHex(sumCalc), HexUtil.toHex(sumRead)));
                return false;
            }
            // 代码走到这，全部校验都通过
            return true;
        }
        log.error(StrUtil.format("数据异常：数据长度小于报文最小长度{}个字节", DataHandleConsts.messageMinLength));
        return false;
    }

    // IMEI，16个字节，最后一个字节保留不管
    public static String parseIMEI(ByteBuf byteBuf) {
        byte[] bs = new byte[15];
        byteBuf.readBytes(bs).readByte();
        return new String(bs);
    }

    // IMSI，16个字节，最后一个字节保留不管
    public static String parseIMSI(ByteBuf byteBuf) {
        byte[] bs = new byte[15];
        byteBuf.readBytes(bs).readByte();
        return new String(bs);
    }

    // ICCID，20个字节
    public static String parseICCID(ByteBuf byteBuf) {
        byte[] bs = new byte[20];
        byteBuf.readBytes(bs);
        return new String(bs);
    }

    // IP，4个字节
    public static String parseIP(ByteBuf byteBuf) {
        List<String> list = CollUtil.newArrayList();
        list.add(Short.toString(byteBuf.readUnsignedByte()));
        list.add(Short.toString(byteBuf.readUnsignedByte()));
        list.add(Short.toString(byteBuf.readUnsignedByte()));
        list.add(Short.toString(byteBuf.readUnsignedByte()));
        Collections.reverse(list);
        return CollUtil.join(list, ".");
    }

    // 读取1个字节，高低字节反转，转成int
    public static int parseUInt8(ByteBuf byteBuf) {
        return byteBuf.readByte() & 0xff;
    }

    // 读取2个字节，高低字节反转，转成int
    public static int parseUInt16(ByteBuf byteBuf) {
        return parseUInt8(byteBuf) | (parseUInt8(byteBuf) << 8);
    }

    // 读取4个字节，高低字节反转，转成int
    public static int parseUInt32(ByteBuf byteBuf) {
        return (parseUInt16(byteBuf) << 16) | parseUInt16(byteBuf);
    }

    // 时间戳，4个字节
    public static Date parseDataTime(ByteBuf byteBuf) {
        long l = (byteBuf.readByte() & 0xffl)
                | ((byteBuf.readByte() & 0xffl) << 8)
                | ((byteBuf.readByte() & 0xffl) << 16)
                | ((byteBuf.readByte() & 0xffl) << 24);
        return DateUtil.date(l*1000l);
    }

    // 电表的数据（去掉了）
    public static Map<Integer, Long> parseElecData(ByteBuf byteBuf) {
        Map<Integer, Long> data = CollUtil.newHashMap();
        Integer indexValue = DataHandleConsts.elecDataRange[0];
        while(indexValue <= DataHandleConsts.elecDataRange[1]) {
            // 电表，4个字节，10011到10027
            Long monitorValue = Integer.toUnsignedLong(parseUInt32(byteBuf));
            data.put(indexValue++, monitorValue);
        }
        return data;
    }
    // 空调的数据
    public static Map<Integer, Integer> parseAirData(ByteBuf byteBuf) {
        Map<Integer, Integer> data = CollUtil.newHashMap();
        // 空调，2个字节，10028到10066，20001到20024，10067到10068
        for (int i=DataHandleConsts.airDataRange1[0]; i<=DataHandleConsts.airDataRange1[1]; i++) {
            int monitorValue = byteBuf.readShort();
            data.put(i, monitorValue);
        }
        for (int i=DataHandleConsts.airDataRange2[0]; i<=DataHandleConsts.airDataRange2[1]; i++) {
            int monitorValue = byteBuf.readShort();
            data.put(i, monitorValue);
        }
        for (int i=DataHandleConsts.airDataRange3[0]; i<=DataHandleConsts.airDataRange3[1]; i++) {
            int monitorValue = byteBuf.readShort();
            data.put(i, monitorValue);
        }
        return data;
    }

    /**
     * 读取到的数据要特殊处理：参照文档《通信协议.xls》
     */
    public static BigDecimal dealData(Integer monitorIndex, Object monitorValue) {
        if(ObjectUtil.isNull(monitorValue)) {
            return null;
        }
        BigDecimal val;
        if(monitorValue instanceof Integer) {
            val = BigDecimal.valueOf((Integer) monitorValue);
        } else if(monitorValue instanceof Long) {
            val = BigDecimal.valueOf((Long) monitorValue);
        } else {
            return null;
        }
        switch (monitorIndex) {
            // 电表
            case 10011: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10012: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10013: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10014: val = val.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP); break;
            case 10015: val = val.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP); break;
            case 10016: val = val.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP); break;
            case 10017: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10018: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10019: val = val.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP); break;
            case 10020: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10021: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10022: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10023: val = val.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); break;
            case 10024: val = val.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); break;
            case 10025: val = val.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); break;
            case 10026: val = val.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); break;
            case 10027: val = val.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); break;
            // 空调
//            case 10028: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10029: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10030: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10031: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10032: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10033: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10034: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10035: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10036: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10037: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10038: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10039: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10040: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10041: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10042: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10043: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10044: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10045: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10046: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10047: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10048: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10049: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10050: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10051: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10052: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10053: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10054: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10055: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10056: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10057: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10058: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10059: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10060: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10061: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10062: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10063: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10064: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10065: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 10066: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;

            case 20001: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20002: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20003: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20004: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20005: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20006: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20007: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20008: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20009: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20010: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20011: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20012: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20013: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20014: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20015: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20016: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20017: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20018: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20019: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20020: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20021: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20022: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20023: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
            case 20024: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;

//            case 10067: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
//            case 10068: val = val.divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP); break;
        }
        return val;
    }

//    /**
//     * 将数据解析为设备上线下线数据
//     * @param body
//     * @return
//     */
//    public static CuredTobaccoDeviceVO parseCuredTobaccoDeviceVO(byte[] body) {
//        try {
//            CuredTobaccoDeviceVO curedTobaccoDeviceVo = JSON.parseObject(new String(body), CuredTobaccoDeviceVO.class);
//            return curedTobaccoDeviceVo;
//        } catch (Exception e) {
//            throw new ServiceException(StrUtil.format("数据解析异常：{}", e.getMessage()));
//        }
//    }

}
