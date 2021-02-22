package com.dkha.service.impl;

import com.dkha.config.ElectricMQConfig;
import com.dkha.service.IRtuChangeDataEventSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 队列发送
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/8/25 0025 15:34
 */
@Service
public class RtuChangeDataEventSender implements IRtuChangeDataEventSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void sendUpdateChangeDataEvent(String dataEventEnum) {
        rabbitTemplate.convertAndSend(ElectricMQConfig.MQ_UPDATE_CURRENTVALUE_EXCHANGE,null,dataEventEnum);
    }

}
