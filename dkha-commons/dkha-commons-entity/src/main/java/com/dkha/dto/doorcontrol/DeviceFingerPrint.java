package com.dkha.dto.doorcontrol;

import lombok.Data;

/**
 * 获取设备指纹信息实体
 */
@Data
public class DeviceFingerPrint {
    private String clientIp;    //设备端IP
    private String clientPort;  //设备端端口
    private String serialNumber; //设备SN号
    private String macAddress; //设备MAC地址
    private String signKey;
    private String deviceName; //设备标识
}
