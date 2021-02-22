package com.dkha.commons.tools.rabbitmq;

/**
 * MQ消息队列的交换机名称
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/8/28 0028 14:19
 */
public class MQExchangeNameConfig {


    /**
     * 数据采集报警交换机
     */
    public static final String MQ_SCHOOL_ACK_ALARM_EXCHANGE = "SCHOOL_ACK_ALARM_EXCHANGE";


    /**
     * UWB预警消息--交换机
     */
    public static final String UWB_ACK_WARMING_EXCHANGE = "uwb.ack.warming.exchange";


    /**
     * 预警电流值交换机
     */
    public static final String MQ_ACK_RECORD_EXCHANGE = "SCHOOL_ACK_RECORD_EXCHANGE";


    /**
     * 数据更新消息交换机
     */
    public static final String MQ_UPDATE_ELECTRIC_EXCHANGE="mq.update.electric.exchange";

    /**
     * uwb灯控交换机
     */
    public static final String UWB_ACK_LIGHTING_EXCHANGE = "UWB_ACK_LIGHTING_EXCHANGE";

    /**
     * 学生信息交换机
     */
    public static final String STUDENT_INFO_EXCHANGE = "student.info.exchange";

    /**
     * 交换机
     */
    public static final String MQ_ACK_WARMING_EXCHANGE = "mq.ack.warming.exchange";

    /**
     * 电流值交换机
     */
    public static final String MQ_ACK_CURRENTVALUE_EXCHANGE = "mq.ack.electric.exchange";

    /**
     * 消费系统交换机
     */
    public static final String SCHOOL_CONSUMPTION_SYSTEM_EXCHANGE = "school.consumption.system.exchange";


    /**
     * 五寸门禁系统注册状态接收交换机
     */
    public static final String FIVE_DOOR_REGISTER_EXCHANGE = "FIVE_DOOR_REGISTER_EXCHANGE";

    /**
     * 人脸注册交换机
     */
    public static final String FACE_REGISTER_EXCHANGE = "FACE_REGISTER_EXCHANGE";

    /**
     * 五寸门禁系统通行记录接收交换机
     */
    public static final String FACE_OPEN_LOG_EXCHANGE = "FACE_OPEN_LOG_EXCHANGE";

}
