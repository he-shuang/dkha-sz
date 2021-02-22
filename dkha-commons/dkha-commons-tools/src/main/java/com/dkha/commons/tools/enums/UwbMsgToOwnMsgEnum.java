package com.dkha.commons.tools.enums;

import java.util.*;

/**
 * UWB请求返回s错误Msg与我们系统Msg对应
 *
 * @author linhuacheng
 * @version 1.0
 * @date 2020/9/9 13:49
 */
public enum UwbMsgToOwnMsgEnum {
    USERNAMEEXISTS("用户名已存在", "该用户名已存在，请重新输入。"),
    UNKNOWN("unknown", "unknown");

    private String uwbMsg;
    private String ownMsg;

    UwbMsgToOwnMsgEnum(String uwbMsg, String ownMsg) {
        this.uwbMsg = uwbMsg;
        this.ownMsg = ownMsg;
    }

    public String getUwbMsg() {
        return this.uwbMsg;
    }

    public String getOwnMsg() {
        return this.ownMsg;
    }

    /**
     * 根据msg得到枚举的ownMsg
     * Lambda表达式，比较判断（JDK 1.8）
     * @param msg
     * @return
     */
    public static UwbMsgToOwnMsgEnum getUwbMsgToOwnMsg(String msg) {
        return Arrays.asList(UwbMsgToOwnMsgEnum.values()).stream()
                .filter(uwbMsgToOwnMsgEnum -> uwbMsgToOwnMsgEnum.getUwbMsg().equals(msg))
                .findFirst().orElse(UwbMsgToOwnMsgEnum.UNKNOWN);
    }
}
