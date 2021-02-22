package com.dkha.service.impl;

import com.dkha.commons.tools.rabbitmq.MQExchangeNameConfig;
import com.dkha.commons.tools.rabbitmq.RabbitMQConfig;
import com.dkha.service.ScStudentsMqSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright(C) 2013-2020 电科惠安公司 Inc.ALL Rights Reserved.
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-09-02 9:47
 */
@Service
public class ScStudentsMqSenderServiceImpl implements ScStudentsMqSenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendStudentsInfo(String msgContent) {
        rabbitTemplate.convertAndSend(MQExchangeNameConfig.STUDENT_INFO_EXCHANGE,null,msgContent);
    }
}
