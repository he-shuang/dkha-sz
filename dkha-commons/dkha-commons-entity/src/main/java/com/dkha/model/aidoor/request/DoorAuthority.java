package com.dkha.model.aidoor.request;

public class DoorAuthority  {

    /**
     * 人员编号
     */
    private String personSerial;

    /**
     * 人员权限名称
     */
    private String authorityName;

    /**
     * 权限早上生效时间
     */
    private String morningStartTime;

    /**
     * 权限早上失效时间
     */
    private String morningEndTime;

    /**
     * 权限中午生效时间
     */
    private String noonStartTime;

    /**
     * 权限中午失效时间
     */
    private String noonEndTime;

    /**
     * 权限晚上生效时间
     */
    private String nightStartTime;

    /**
     * 权限晚上失效时间
     */
    private String nightEndTime;

    public String getPersonSerial() {
        return personSerial;
    }

    public void setPersonSerial(String personSerial) {
        this.personSerial = personSerial;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getMorningStartTime() {
        return morningStartTime;
    }

    public void setMorningStartTime(String morningStartTime) {
        this.morningStartTime = morningStartTime;
    }

    public String getMorningEndTime() {
        return morningEndTime;
    }

    public void setMorningEndTime(String morningEndTime) {
        this.morningEndTime = morningEndTime;
    }

    public String getNoonStartTime() {
        return noonStartTime;
    }

    public void setNoonStartTime(String noonStartTime) {
        this.noonStartTime = noonStartTime;
    }

    public String getNoonEndTime() {
        return noonEndTime;
    }

    public void setNoonEndTime(String noonEndTime) {
        this.noonEndTime = noonEndTime;
    }

    public String getNightStartTime() {
        return nightStartTime;
    }

    public void setNightStartTime(String nightStartTime) {
        this.nightStartTime = nightStartTime;
    }

    public String getNightEndTime() {
        return nightEndTime;
    }

    public void setNightEndTime(String nightEndTime) {
        this.nightEndTime = nightEndTime;
    }
}
