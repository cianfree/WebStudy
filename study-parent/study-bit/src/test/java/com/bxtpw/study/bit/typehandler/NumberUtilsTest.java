package com.bxtpw.study.bit.typehandler;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/12 8:48
 * @since 0.1
 */
public class NumberUtilsTest {

    @Test
    public void testBytes2long() throws Exception {
        assertEquals(3, NumberUtils.bytes2long(new byte[]{1, 1}));
        assertEquals(0, NumberUtils.bytes2long(new byte[]{0, 0}));
        assertEquals(5, NumberUtils.bytes2long(new byte[]{1, 0, 1}));
        assertEquals(7, NumberUtils.bytes2long(new byte[]{1, 1, 1}));

    }
}