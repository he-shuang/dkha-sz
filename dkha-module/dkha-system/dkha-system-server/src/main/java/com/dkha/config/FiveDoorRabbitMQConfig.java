package com.dkha.config;

import com.dkha.commons.tools.rabbitmq.MQExchangeNameConfig;
import com.dkha.commons.tools.rabbitmq.MQQueueNameConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置，广播模式，确认机制
 */
@Configuration
@Slf4j
public class FiveDoorRabbitMQConfig {

    /**
     * 接收人脸注册状态交换机与队列
     *
     * @return
     */
    @Bean
    public Queue fiveDoorRegisterQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(MQQueueNameConfig.FIVE_DOOR_REGISTER_QUEUE);
    }

    @Bean
    public FanoutExchange fiveDoorRegisterExchange() {
        return new FanoutExchange(MQExchangeNameConfig.FIVE_DOOR_REGISTER_EXCHANGE);
    }

    /**
     * 人脸批量注册交换机与队列
     *
     * @return
     */
    @Bean
    public Queue faceRegisterQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(MQQueueNameConfig.FACE_REGISTER_QUEUE);
    }

    @Bean
    public FanoutExchange faceRegisterExchange() {
        return new FanoutExchange(MQExchangeNameConfig.FACE_REGISTER_EXCHANGE);
    }

    /**
     * 五寸门及系统通行记录交换机与队列
     *
     * @return
     */
    @Bean
    public Queue faceOpenLogQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(MQQueueNameConfig.FACE_OPEN_LOG_QUEUE);
    }

    @Bean
    public FanoutExchange faceOpenLogExchange() {
        return new FanoutExchange(MQExchangeNameConfig.FACE_OPEN_LOG_EXCHANGE);
    }


    //接收人脸注册状态交换机与队列绑定
    @Bean
    public Binding fiveDoorRegisterMq() {
        return BindingBuilder.bind(fiveDoorRegisterQueue()).to(fiveDoorRegisterExchange());
    }

    //人脸批量注册交换机与队列绑定
    @Bean
    public Binding FaceRegisterMq() {
        return BindingBuilder.bind(faceRegisterQueue()).to(faceRegisterExchange());
    }

    //五寸门及系统通行记录交换机与队列绑定
    @Bean
    public Binding faceOpenLogrMq() {
        return BindingBuilder.bind(faceOpenLogQueue()).to(faceOpenLogExchange());
    }

}
