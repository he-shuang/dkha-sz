package com.dkha.enums;

/**
 * 报警消息类型
 */
public enum AlarmTypeEnum {
    /**
     * 电流报警
     */
    ELECTRIC_ALARM(0),
    /**
     * 环境互感器报警，包括PM2.5 温度，湿度
     */
    ENV_ALARM(1),

    /**
     * 电流报警
     */
    ELECTRIC_RECORD(2),

    /**
     * 环境互感器报警，包括PM2.5 温度，湿度
     */
    ENV_RECORD(3),

    /**
     * 管道报警
     */
    PIPELINE_ALARM(4);

    private int value;

    AlarmTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }


}
