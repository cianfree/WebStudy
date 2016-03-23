package com.bxtpw.study.bit.query;

import com.bxtpw.study.bit.domain.Agent;

/**
 * 登录用户查询
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年11月29日 下午5:12:24
 * @since 0.1
 */
public class AgentQuery extends TagQuery<AgentQuery> {

    private static final long serialVersionUID = -7269555978671367854L;

    private Boolean isRegion;
    private Boolean isProvince;
    private Boolean isCity;
    private Boolean isDistrict;
    private Boolean isSurname;
    private Boolean isTown;
    private Boolean isBurg;

    public Boolean getIsRegion() {
        return isRegion;
    }

    public AgentQuery setIsRegion(Boolean isRegion) {
        this.isRegion = isRegion;
        addTagCondition(isRegion, Agent.IS_REGION);
        return this;
    }

    public Boolean getIsProvince() {
        return isProvince;
    }

    public AgentQuery setIsProvince(Boolean isProvince) {
        this.isProvince = isProvince;
        addTagCondition(isProvince, Agent.IS_PROVINCE);
        return this;
    }

    public Boolean getIsCity() {
        return isCity;
    }

    public AgentQuery setIsCity(Boolean isCity) {
        this.isCity = isCity;
        addTagCondition(isCity, Agent.IS_CITY);
        return this;
    }

    public Boolean getIsDistrict() {
        return isDistrict;
    }

    public AgentQuery setIsDistrict(Boolean isDistrict) {
        this.isDistrict = isDistrict;
        addTagCondition(isDistrict, Agent.IS_DISTRICT);
        return this;
    }

    public Boolean getIsSurname() {
        return isSurname;
    }

    public AgentQuery setIsSurname(Boolean isSurname) {
        this.isSurname = isSurname;
        addTagCondition(isSurname, Agent.IS_SURNAME);
        return this;
    }

    public Boolean getIsTown() {
        return isTown;
    }

    public AgentQuery setIsTown(Boolean isTown) {
        this.isTown = isTown;
        addTagCondition(isTown, Agent.IS_TOWN);
        return this;
    }

    public Boolean getIsBurg() {
        return isBurg;
    }

    public AgentQuery setIsBurg(Boolean isBurg) {
        this.isBurg = isBurg;
        addTagCondition(isBurg, Agent.IS_BURG);
        return this;
    }
}
