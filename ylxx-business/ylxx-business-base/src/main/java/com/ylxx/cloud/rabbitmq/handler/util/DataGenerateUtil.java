package com.ylxx.cloud.rabbitmq.handler.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.rabbitmq.handler.consts.DataHandleConsts;
import com.ylxx.cloud.util.SumCheckUtil;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author caixiaopeng
 * @ClassName DataGenerateUtil
 * Description: TODO
 * @date 2021-01-05 21:22:41
 */
public class DataGenerateUtil {

    // IMEI+"0"，16个字节
    public static byte[] genIMEI(String IMEI) {
        byte[] bs = IMEI.getBytes();
        byte[] data = new byte[16];
        for (int i=0; i<15; i++) {
            data[i] = bs[i];
        }
        data[15] = 0;
        return data;
    }

    public static byte[] genUInt8(int val) {
        byte[] bs = new byte[1];
        bs[0] = (byte) val;
        return bs;
    }
    public static byte[] genUInt16(int val) {
        byte[] bs = new byte[2];
        bs[0] = (byte) (val>>8);
        bs[1] = (byte) val;
        return bs;
    }
    public static byte[] genUInt16LH(int val) {
        byte[] bs = new byte[2];
        bs[1] = (byte) (val>>8);
        bs[0] = (byte) val;
        return bs;
    }
    public static byte[] genUInt32(int val) {
        byte[] bs = new byte[4];
        bs[0] = (byte) (val>>24);
        bs[1] = (byte) (val>>16);
        bs[2] = (byte) (val>>8);
        bs[3] = (byte) val;
        return bs;
    }
    public static byte[] genUInt64(long val) {
        byte[] bs = new byte[8];
        bs[0] = (byte) (val>>56);
        bs[1] = (byte) (val>>48);
        bs[2] = (byte) (val>>40);
        bs[3] = (byte) (val>>32);
        bs[4] = (byte) (val>>24);
        bs[5] = (byte) (val>>16);
        bs[6] = (byte) (val>>8);
        bs[7] = (byte) val;
        return bs;
    }

    /**
     * 生成烤烟机下发命令（版本1）
     * @param IMEI：终端编号
     * @param registerAddr：寄存器地址
     * @param val：寄存器设置值
     * @return
     */
    public static byte[] genCommandBody(String IMEI, int registerAddr, int val) throws IOException {
        byte[] data = DataGenerateUtil.genUInt16LH(val);
        return genCommandBody(IMEI, DataHandleConsts.FUNC_CODE_TYPE3, registerAddr, data);
    }

    /**
     * 生成烤烟机下发命令（版本2）
     * @param IMEI
     * @param registerAddrs
     * @param vals
     * @return
     * @throws IOException
     */
    public static byte[] genCommandBody(String IMEI, int[] registerAddrs, int[] vals) throws IOException {
        byte[] data = new byte[registerAddrs.length*4];
        for (int i=0; i<registerAddrs.length; i++) {
            byte[] addr = DataGenerateUtil.genUInt16LH(registerAddrs[i]);
            byte[] val = DataGenerateUtil.genUInt16LH(vals[i]);
            int index = i*4;
            data[index] = addr[0];
            data[index+1] = addr[1];
            data[index+2] = val[0];
            data[index+3] = val[1];
        }
        return genCommandBody(IMEI, DataHandleConsts.FUNC_CODE_TYPE4, 0, data);
    }

    /**
     * 按照协议生成数据
     * @param IMEI
     * @param funCode
     * @param registerAddr
     * @param data
     * @return
     * @throws IOException
     */
    private static byte[] genCommandBody(String IMEI, int funCode, int registerAddr, byte[] data) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 写起始符
        os.write(DataGenerateUtil.genUInt32(DataHandleConsts.start));
        // 写从机地址
        os.write(DataGenerateUtil.genIMEI(IMEI));
        // 写功能码
        os.write(DataGenerateUtil.genUInt8(funCode));
        // 写寄存器地址
        os.write(DataGenerateUtil.genUInt16(registerAddr));
        // 写字节数
        os.write(DataGenerateUtil.genUInt16(0));
        // 写数据
        os.write(data);
        // 写和校验
        os.write(DataGenerateUtil.genUInt8(0));
        // 写结束符
        os.write(DataGenerateUtil.genUInt8(DataHandleConsts.end));

        byte[] body = os.toByteArray();
        // 计算字节数
        byte[] byteCount = DataGenerateUtil.genUInt16(body.length-DataHandleConsts.messageMinLength);
        body[23] = byteCount[0];
        body[24] = byteCount[1];
        // 计算和校验值
        byte sumCalc = SumCheckUtil.sum(body, 4, body.length-4-2);
        body[body.length-2] = sumCalc;
        return body;
    }

    // 自己发给自己的数据（例如设备下线）
    public static byte[] genCommandBody(String IMEI, int funCode, int registerAddr, byte[] data, byte[] endSplit) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(genCommandBody(IMEI,funCode,registerAddr,data));
        os.write(endSplit);
        return os.toByteArray();
    }


}
