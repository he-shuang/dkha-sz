package com.dkha.aidoor;

import lombok.Data;

/**
 * 人脸信息下发
 */
@Data
public class TransPictureResult {
    int transTotal;    //总的下发数量
    int successTotal;  //成功下发数量
    int failTotal;     //失败下发数量
    String deviceid;      //设备ID
    String deviceName;    //设备名称
    int seconds;       //下发总耗时（秒）
    int code;         //下发状态 200 成功 否则失败
    String msg;       //下发消息
}
