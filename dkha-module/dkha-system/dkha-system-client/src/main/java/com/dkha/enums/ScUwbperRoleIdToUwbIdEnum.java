package com.dkha.enums;

public enum ScUwbperRoleIdToUwbIdEnum {

    STUDENT(-1,"学生"),
    TEACHER(0,"教师"),
    CLEANING(1,"保洁"),
    SECURITYSTAFF(2,"保安"),
    IMPEQUIPMENT(-2,"重要设备"),
    VISITOR(-3,"访客");

    private int code;
    private String value;

    ScUwbperRoleIdToUwbIdEnum(int code,String value) {
        this.value = value;
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
