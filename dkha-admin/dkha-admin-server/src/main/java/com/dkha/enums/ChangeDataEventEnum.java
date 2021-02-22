package com.dkha.enums;

import lombok.Getter;

/**
 * fence   com.dkha.application.module.enums
 *
 * @author：黄玉刚 -成都电安惠科有限公司
 * @date： 2020/2/8 12:29
 * @Description ：TODO:
 */
@Getter
public enum ChangeDataEventEnum {

    ELECTRICVALUE(1,"electricValue"),
    ELECTRICVALUE_WRING(2,"报警阈值改变");

    private Integer code;
    private String msg;
    ChangeDataEventEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
