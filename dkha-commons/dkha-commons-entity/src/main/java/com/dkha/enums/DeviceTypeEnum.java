package com.dkha.enums;

/**
 * 报警消息类型
 */
public enum DeviceTypeEnum {
    /**
     * PIR设备
     */
    DEVICE_TYPE_PIR("0"),
    /**
     * 智能控灯设备
     */
    DEVICE_TYPE_ZN("1"),
    /**
     * PM2.5
     */
    DEVICE_TYPE_PM("2"),
    /**
     * 电流互感器
     */
    DEVICE_TYPE_DL("3"),
    /**
     * 网关设备
     */
    DEVICE_TYPE_WG("4"),
    /**
     * 重要设备
     */
    DEVICE_TYPE_ZY("5");
    private String value;

    DeviceTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }


}
