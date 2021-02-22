package com.dkha.entity;

import lombok.Data;

/**
 * UWB 报警
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/9/3 0003 17:14
 */
@Data
public class UwbAlarmItemEntity {

    private  Integer alarmType; //报警类型
    private  Long id;        //自增id
    private  Integer fenceId;   //围栏id
    private  Integer status ;   //报警开始（1）/结束（0）
    private  String time ;  //开始/结束时间
    private  String uwbid;     //uwb标签ID
}
