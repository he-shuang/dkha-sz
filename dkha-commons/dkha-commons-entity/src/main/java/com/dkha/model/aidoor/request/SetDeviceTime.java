package com.dkha.model.aidoor.request;

public class SetDeviceTime {
    private String macAddress;
    private String deviceSN;
    private String systemDateTime;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceSN() {
        return deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }

    public String getSystemDateTime() {
        return systemDateTime;
    }

    public void setSystemDateTime(String systemDateTime) {
        this.systemDateTime = systemDateTime;
    }
}
