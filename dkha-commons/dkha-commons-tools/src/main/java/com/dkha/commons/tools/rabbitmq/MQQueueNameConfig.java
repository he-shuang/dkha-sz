package com.dkha.commons.tools.rabbitmq;

/**
 * MQ消息队列的队列名称
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/8/28 0028 14:18
 */
public class MQQueueNameConfig {

    /**
     * 队列
     */
    public static final String MQ_ACK_WARMING_QUEUE = "mq.ack.warming.queue";
    /**
     * 数据采集报警
     */
    public static final String MQ_SCHOOL_ACK_ALARM_QUEUE = "SCHOOL_COLLECTION_ALARM_QUEUE";
    /**
     * 首页数据采集报警
     */
    public static final String MQ_HOME_SCHOOL_ACK_ALARM_QUEUE = "MQ_HOME_SCHOOL_ACK_ALARM_QUEUE";


    /**
     *  UWB预警消息--队列
     */
    public static final String UWB_ACK_WARMING_QUEUE = "uwb.ack.warming.queue";

    /**
     * 电流值队列
     */
    public static final String MQ_ACK_CURRENTVALUE_QUEUE = "mq.ack.electric.queue";

    /**
     * 记录队列队列
     */
    public static final String MQ_SCHOOL_ACK_RECORD_QUEUE = "SCHOOL_COLLECTION_RECORD_QUEUE";

    /**
     * 数据更新消息队列
     */
    public static final String MQ_UPDATE_CURRENTVALUE_QUEUE="mq.update.electric.queue";

    /**
     * uwb区域灯控消息队列
     */
    public static final String UWB_ACK_LIGHTING_QUEUE = "UWB_ACK_LIGHTING_QUEUE";

    /**
     * 学生信息队列
     */
    public static final String STUDENT_INFO_QUEUE = "student.info.queue";

    /**
     * 消费系统队列
     */
    public static final String SCHOOL_CONSUMPTION_SYSTEM_QUEUE = "school.consumption.system.queue";


    /**
     * 五寸门禁注册状态接收队列
     */
    public static final String FIVE_DOOR_REGISTER_QUEUE = "FIVE_DOOR_REGISTER_QUEUE";

    /**
     * 人脸注册接收队列
     */
    public static final String FACE_REGISTER_QUEUE = "FACE_REGISTER_QUEUE";

    /**
     * 五寸门禁系统通行记录接收队列
     */
    public static final String FACE_OPEN_LOG_QUEUE = "FACE_OPEN_LOG_QUEUE";
}
