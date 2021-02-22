package com.dkha.commons.tools.rabbitmq;

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
public class RabbitMQConfig {

    //1. 初始化队列 ============================================================================================

    @Bean
    public Queue immediateMqQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(MQQueueNameConfig.MQ_ACK_WARMING_QUEUE);
    }
    @Bean
    public Queue currentMqQueue() {
        return new Queue(MQQueueNameConfig.MQ_ACK_CURRENTVALUE_QUEUE);
    }


    @Bean
    public Queue studentQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(MQQueueNameConfig.STUDENT_INFO_QUEUE);
    }





    /**
     * UWB 开灯控制队列
     * @return
     */
    @Bean
    public Queue uwbLightingQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(MQQueueNameConfig.UWB_ACK_LIGHTING_QUEUE);
    }

    //2. 初始化交换机 ============================================================================================

    @Bean
    public FanoutExchange mqAckFanoutExchange() {
        return new FanoutExchange(MQExchangeNameConfig.MQ_ACK_WARMING_EXCHANGE);
    }
    @Bean
    public FanoutExchange studentFanoutExchange() {
        return new FanoutExchange(MQExchangeNameConfig.STUDENT_INFO_EXCHANGE);
    }
    @Bean
    public FanoutExchange mqAckelectricExchange() {
        return new FanoutExchange(MQExchangeNameConfig.MQ_ACK_CURRENTVALUE_EXCHANGE);
    }

    @Bean
    public FanoutExchange uwbLightinExchange() {
        return new FanoutExchange(MQExchangeNameConfig.UWB_ACK_LIGHTING_EXCHANGE);
    }




    //3. 绑定队列和交换机 ============================================================================================
    @Bean
    public Binding ackBindingMq() {
        return BindingBuilder.bind(immediateMqQueue()).to(mqAckFanoutExchange());
    }

    @Bean
    public Binding studentBindingMq() {
        return BindingBuilder.bind(studentQueue()).to(studentFanoutExchange());
    }

    @Bean
    public Binding ackBindingelectricMq() {
        return BindingBuilder.bind(currentMqQueue()).to(mqAckelectricExchange());
    }

    @Bean
    public Binding ackBindingLightingMq() {
        return BindingBuilder.bind(uwbLightingQueue()).to(uwbLightinExchange());
    }

    // 首页数据采集报警队列
    @Bean
    public Queue currHomeMqQueue() {
        return new Queue(MQQueueNameConfig.MQ_HOME_SCHOOL_ACK_ALARM_QUEUE);
    }


    //2. 初始化交换机 首页数据采集报警队列============================================================================================
    @Bean
    public FanoutExchange mqHomeAckelectrExchange() {
        return new FanoutExchange(MQExchangeNameConfig.MQ_SCHOOL_ACK_ALARM_EXCHANGE);
    }
    //3. 绑定队列和交换机 首页数据采集报警队列============================================================================================
    @Bean
    public Binding ackHomeBindinlectricMq() {
        return BindingBuilder.bind(currHomeMqQueue()).to(mqHomeAckelectrExchange());
    }




}
