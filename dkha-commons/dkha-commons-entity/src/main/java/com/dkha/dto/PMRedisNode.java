package com.dkha.dto;

import lombok.Data;

@Data
public class PMRedisNode {
    private  String  mbdid;            // 通讯设备ID
    private  String  pm ;               //读取PM2.5浓度值
    private  String  temperature ;       //读取温度值
    private  String  humidity ;         //读取湿度值
    private  String  isSendAlarm ;         //是否需要报警
    private  String  timestamp;            //处理时间
    private  String  isOffdevice;          //是否掉线
    private  String  ispmAlarm;            //PM报警
    private  String   istemperatureAlarm;  //温度报警
    private  String   ishumidityAlarm;     //湿度报警
}
