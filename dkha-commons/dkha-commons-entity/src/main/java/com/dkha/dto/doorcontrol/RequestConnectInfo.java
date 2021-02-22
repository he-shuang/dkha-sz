package com.dkha.dto.doorcontrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 与设备建立连接的请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestConnectInfo {
    private  int signType; //1：签到； 2：签退； 3：签到和签退；
    private String serialNumber; // 设备端设备SN号
    private String macAddress;// 设备端设备mac地址；与接口“2.1”中返回的参数“macAddress”一致
    private String signKey; // 用于签名sign
    private String deviceId;// 管理端分配给设备端的唯一标识，设备端在回调管理端接口时会返回此信息，可作为判断设备端的依据（管理端自行管理此值）
    private String ip; // 管理端ip，设备端回调时需要此ip地址
    private String port;// 管理端端口号，设备端回调时需要此端口号


}
