package com.dkha.dto;

import com.dkha.enums.AlarmTypeEnum;
import lombok.Data;

/**
 * 采集数据报警信息消息
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/8/28 0028 14:01
 */
@Data
public class MqAlarmMsgDTO {

    private AlarmTypeEnum alarmtype; //报警类型
    private Object alarmObject;  //报警消息
}
