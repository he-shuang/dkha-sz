package com.dkha.enums;

import lombok.Getter;

/**
 * 报警消息类型
 */
@Getter
public enum OperationTypeEnum {
    ADD("1","新增"),
    DELETE("2","删除"),
    UPDATE("3","修改");



    private String code;
    private String msg;

    OperationTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
