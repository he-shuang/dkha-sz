package com.dkha.dto;/**
 * UWB Alarm报警消息
 *
 * @version 1.0
 * @author Administrator
 * @date 2020/8/28 20:10
 */

import lombok.Data;

/**
 *
 * @Author yangjun
 * @Date2020/8/28 20:10
 */
@Data
public class AlarmMassge {
    private String deid;  //设备ID（平台）
    private String id;    //报警存储数据的Id
    private String device;//设备序列号
    private String alarmType; //报警类型
    private Integer status;  //状态 1是报警 0 是未报警
    private String time;     //报警时间
}