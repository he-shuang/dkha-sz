

package com.dkha.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.rabbitmq.MQExchangeNameConfig;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.MqAlarmMsgDTO;
import com.dkha.entity.ScThermalAlarmEntity;
import com.dkha.enums.AlarmTypeEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 测试
 */
@RestController
public class TestController {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@GetMapping("test")
	public Result delete(){
		ScThermalAlarmEntity scThermalAlarmEntity = new ScThermalAlarmEntity();
		//报警时间
		scThermalAlarmEntity.setTfaAlarmtime(new Date());
		//温度报警值
		scThermalAlarmEntity.setTfaEcurrent((float) 12.0);
		//报警设备id
		scThermalAlarmEntity.setTfId(1314882247282778113L);
		//热成像设备编号
		scThermalAlarmEntity.setTfDevicesn("1314882247282778113L");

		MqAlarmMsgDTO RecordMsg = new MqAlarmMsgDTO();
		RecordMsg.setAlarmtype(AlarmTypeEnum.PIPELINE_ALARM); //报警记录
		RecordMsg.setAlarmObject(scThermalAlarmEntity);
		rabbitTemplate.convertAndSend(MQExchangeNameConfig.MQ_SCHOOL_ACK_ALARM_EXCHANGE,null, JSONObject.toJSONString(RecordMsg));
		return new Result();
	}

}
