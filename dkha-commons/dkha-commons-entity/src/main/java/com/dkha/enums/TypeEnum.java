package com.dkha.enums;

import lombok.Getter;

/**
 * 报警消息类型
 */
@Getter
public enum TypeEnum {
    TYPE_ELECTRIC("407","电流"),
    TYPE_PM("401","PM2.5"),
    TYPE_HUMIDITY("405","湿度"),
    TYPE_TEMPERATURE("403","温度");




    private String code;
    private String msg;

    TypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
