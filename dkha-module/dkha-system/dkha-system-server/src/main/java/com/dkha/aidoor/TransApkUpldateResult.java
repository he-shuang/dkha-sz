package com.dkha.aidoor;

import lombok.Data;

/**
 * 设备APK升级结果
 */
@Data
public class TransApkUpldateResult {
    int transTotal;    //总的升级数量
    int successTotal;  //成功升级数量
    int failTotal;     //失败升级数量
    String deviceid;      //设备ID
    String deviceName;    //设备名称
    int seconds;       //升级耗时（秒）
    int code;         //下发状态 200 成功 否则失败
    String msg;       //下发消息


}
