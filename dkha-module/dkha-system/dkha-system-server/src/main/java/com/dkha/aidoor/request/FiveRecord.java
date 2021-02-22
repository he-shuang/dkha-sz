package com.dkha.aidoor.request;

public class FiveRecord {
    private int deviceId;

    private int deviceNo;

    private int deviceType;

    private String serial;

    private String password;

    private String mac;

    private String btkey;

    private int btkeyType;

    private int algorithm;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(int deviceNo) {
        this.deviceNo = deviceNo;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getBtkey() {
        return btkey;
    }

    public void setBtkey(String btkey) {
        this.btkey = btkey;
    }

    public int getBtkeyType() {
        return btkeyType;
    }

    public void setBtkeyType(int btkeyType) {
        this.btkeyType = btkeyType;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }
}
