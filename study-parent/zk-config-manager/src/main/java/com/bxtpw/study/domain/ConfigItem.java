package com.bxtpw.study.domain;

import java.io.Serializable;

/**
 * 配置项
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/12 14:16
 * @since 0.1
 */
public class ConfigItem implements Serializable {

    private static final long serialVersionUID = -1255958270840485636L;

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取int value
     *
     * @param defaultValue 默认值
     */
    public int intValue(int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取int value，默认是0
     */
    public int intValue() {
        return intValue(0);
    }

    /**
     * 获取long value
     *
     * @param defaultValue 默认值
     */
    public long longValue(long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取long value，默认是0
     */
    public long longValue() {
        return longValue(0L);
    }

    /**
     * 获取double value
     *
     * @param defaultValue 默认值
     */
    public double doubleValue(double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取double value，默认是0
     */
    public double doubleValue() {
        return doubleValue(0);
    }

    /**
     * 获取float value
     *
     * @param defaultValue 默认值
     */
    public float floatValue(float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取float value，默认是0
     */
    public float floatValue() {
        return floatValue(0);
    }

    /**
     * 获取String value
     *
     * @param defaultValue 默认值
     */
    public String stringValue(String defaultValue) {
        return null == value || "".equals(value) ? defaultValue : value;
    }

    /**
     * 获取string value，默认是null
     */
    public String stringValue() {
        return stringValue(null);
    }

    /**
     * 字符串为1，ok，yes，true的都返回true(不区分大小写)
     */
    private static final String BOOLEAN_TRUE_PATTERN = "(?i)1|ok|true|yes";

    /**
     * 获取布尔类型的值, 字符串为1，ok，yes，true的都返回true，否则返回false
     *
     * @param defaultValue 默认值
     */
    public boolean booleanVlaue(boolean defaultValue) {
        if (null == stringValue()) return defaultValue;
        return stringValue().matches(BOOLEAN_TRUE_PATTERN);
    }

    /**
     * 获取布尔类型的值, 字符串为1，ok，yes，true的都返回true(不区分大小写)，否则返回false
     */
    public boolean booleanValue() {
        return null != this.value && this.value.matches(BOOLEAN_TRUE_PATTERN);
    }
}
