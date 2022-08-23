package com.ylxx.cloud.util;

/**
 * @author caixiaopeng
 * @ClassName SumCheckUtil
 * Description: 和检验工具
 * @date 2021-01-27 20:43:10
 */
public class SumCheckUtil {

    /**
     * 获取和校验值
     * @return
     */
    public static byte sum(byte[] data, int start, int length) {
        int sumCalc = 0x00;
        for(int i=start; i<start+length; i++) {
            sumCalc = sumCalc + (data[i] & 0xff);
        }
        return (byte)(sumCalc & 0xff);
    }

}
