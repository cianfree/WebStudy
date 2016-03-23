package com.bxtpw.study.bit.typehandler;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/12 8:47
 * @since 0.1
 */
public class NumberUtils {

    private NumberUtils() {
    }

    /**
     * 数组转long
     *
     * @param bytes
     * @return
     */
    public static long bytes2long(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return 0;
        }
        int len = bytes.length;
        long result = 0;
        for (int i = len - 1; i >= 0; --i) {
            result |= bytes[i] << i;
        }
        return result;
    }

    /**
     * bytes数字转十进制int
     *
     * @param bytes
     * @return
     */
    public static int bytes2int(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return 0;
        }
        int len = bytes.length;
        int result = 0;
        for (int i = len - 1; i >= 0; --i) {
            result |= bytes[i] << i;
        }
        return result;
    }

}
