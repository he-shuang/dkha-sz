package com.dkha.mq.FiveDoor;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.rabbitmq.MQExchangeNameConfig;
import com.dkha.config.FiveDoorRabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FiveDoorReceiveDataSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 将C#客户端推送的人脸注册状态数据数据写入到消息队列，用于缓解短时间内大量数据上传，后台处理不过来的情况
     * @param msg
     */
    public void pushFiveDoorRegisterDataToQueue(String msg) {
        this.rabbitTemplate.convertAndSend(MQExchangeNameConfig.FIVE_DOOR_REGISTER_EXCHANGE, "", msg);
    }

    /**
     * 将C#客户端推送的五寸门禁通行记录数据写入到消息队列，用于缓解短时间内大量数据上传，后台处理不过来的情况
     * @param msg
     */
    public void pushFiveOpenLogDataToQueue(String msg) {
        this.rabbitTemplate.convertAndSend(MQExchangeNameConfig.FACE_OPEN_LOG_EXCHANGE, "", msg);
    }
}