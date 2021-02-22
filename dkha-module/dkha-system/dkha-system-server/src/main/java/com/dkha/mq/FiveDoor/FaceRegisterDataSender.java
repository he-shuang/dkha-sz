package com.dkha.mq.FiveDoor;

import com.dkha.commons.tools.rabbitmq.MQExchangeNameConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FaceRegisterDataSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 将C#客户端推送的数据写入到消息队列，用于缓解短时间内大量数据上传，后台处理不过来的情况
     * @param msg
     */
    public void pushFaceRegisterDataToQueue(String msg) {
        this.rabbitTemplate.convertAndSend(MQExchangeNameConfig.FACE_REGISTER_EXCHANGE, "", msg);
    }
}