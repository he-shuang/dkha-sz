package com.dkha.entity;

import lombok.Data;

/**
 * Uwb报警Id对应的开灯控制的modbus实体
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/9/4 0004 10:32
 */
@Data
public class UwbAlarmGatewayEntity {
    //网关设备ID
    private String gwId;
    //网关设备IP地址
    private String gwIpGateway;
    //通行设备ID
    private String mbdId;
    //通行设备类型
    private String gbdDeviceType;
    //通讯地址编码 modbus编号
    private int gbdAddr;
    //分组号
    private int gbdGroup;
    //uwb围栏ID
    private String gbdFenceID;
    //modbus 线号
    private String gbdLinenum;

}
