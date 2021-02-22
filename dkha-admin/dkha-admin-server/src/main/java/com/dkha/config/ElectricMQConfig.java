package com.dkha.config;

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
public class ElectricMQConfig {
    /**
     * 电流值队列
     */
    public static final String MQ_UPDATE_CURRENTVALUE_QUEUE = "mq.update.electric.queue";
    /**
     * 电流值交换机
     */
    public static final String MQ_UPDATE_CURRENTVALUE_EXCHANGE = "mq.update.electric.exchange";



    @Bean
    public Queue cupateMqQueue() {
        return new Queue(MQ_UPDATE_CURRENTVALUE_QUEUE);
    }


    //2. 初始化交换机 ============================================================================================
    @Bean
    public FanoutExchange mqupdateExchange() {
        return new FanoutExchange(MQ_UPDATE_CURRENTVALUE_EXCHANGE);
    }
    //3. 绑定队列和交换机 ============================================================================================
    @Bean
    public Binding updateBindinlectricMq() {
        return BindingBuilder.bind(cupateMqQueue()).to(mqupdateExchange());
    }


}
