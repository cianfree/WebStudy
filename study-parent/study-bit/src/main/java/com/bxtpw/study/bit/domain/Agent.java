package com.bxtpw.study.bit.domain;

/**
 * 用户认证对象
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年11月22日 下午3:36:42
 * @since 0.1
 */
public class Agent extends BaseDomain {

    private static final long serialVersionUID = -7658211581142258389L;

    /**
     * 初始状态， 使用八位存储
     */
    public static final long TAG_INIT = 0b00000000;

    /**
     * 第一位：是否大区服务商；
     */
    public static final long IS_REGION = 0b00000001;
    /**
     * 第二位：是否省服务商；
     */
    public static final long IS_PROVINCE = 0b00000010;
    /**
     * 第三位：是否市服务商；
     */
    public static final long IS_CITY = 0b00000100;
    /**
     * 第四位：是否县服务商；
     */
    public static final long IS_DISTRICT = 0b00001000;
    /**
     * 第五位：是否姓氏服务商；
     */
    public static final long IS_SURNAME = 0b00010000;
    /**
     * 第六位：是否镇级服务商；
     */
    public static final long IS_TOWN = 0b00100000;
    /**
     * 第七位：是否村级服务商；
     */
    public static final long IS_BURG = 0b01000000;


    /**
     * 唯一主键
     */
    private Integer id;
    /**
     * <pre>
     * 标记，从右到左，
     * 第一位：是否大区服务商；
     * 第二位：是否省服务商；
     * 第三位：是否市服务商；
     * 第四位：是否县服务商；
     * 第五位：是否姓氏服务商；
     * 第六位：是否镇级服务商；
     * 第七位：是否村级服务商；
     * </pre>
     */
    private long tag = TAG_INIT;

    public Integer getId() {
        return id;
    }

    public Agent setId(Integer id) {
        this.id = id;
        return this;
    }

    public long getTag() {
        return tag;
    }

    public Agent setTag(long tag) {
        this.tag = tag;
        return this;
    }

    public Agent setIsRegion(boolean isRegion) {
        this.tag = calculateNewTagForBoolean(this.tag, IS_REGION, isRegion);
        return this;
    }

    public boolean getIsRegion() {
        return isBitTrue(this.tag, IS_REGION);
    }

    public Agent setIsProvince(boolean isProvince) {
        this.tag = calculateNewTagForBoolean(this.tag, IS_PROVINCE, isProvince);
        return this;
    }

    public boolean getIsProvince() {
        return isBitTrue(this.tag, IS_PROVINCE);
    }

    public Agent setIsCity(boolean isCity) {
        this.tag = calculateNewTagForBoolean(this.tag, IS_CITY, isCity);
        return this;
    }

    public boolean getIsCity() {
        return isBitTrue(this.tag, IS_CITY);
    }

    public Agent setIsDistrict(boolean isDistrict) {
        this.tag = calculateNewTagForBoolean(this.tag, IS_DISTRICT, isDistrict);
        return this;
    }

    public boolean getIsDistrict() {
        return isBitTrue(this.tag, IS_DISTRICT);
    }


    public Agent setIsSurname(boolean isSurname) {
        this.tag = calculateNewTagForBoolean(this.tag, IS_SURNAME, isSurname);
        return this;
    }

    public boolean getIsSurname() {
        return isBitTrue(this.tag, IS_SURNAME);
    }

    public Agent setIsTown(boolean isTown) {
        this.tag = calculateNewTagForBoolean(this.tag, IS_TOWN, isTown);
        return this;
    }

    public boolean getIsTown() {
        return isBitTrue(this.tag, IS_TOWN);
    }

    public Agent setIsBurg(boolean isBurg) {
        this.tag = calculateNewTagForBoolean(this.tag, IS_BURG, isBurg);
        return this;
    }

    public boolean getIsBurg() {
        return isBitTrue(this.tag, IS_BURG);
    }
}
