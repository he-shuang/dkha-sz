package com.dkha.enums;

/**
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-08-24 11:07
 */
public enum ScDormitoryfloorTypeEnum {

    BUILDING(0,"楼栋"),
    FLOOR(1,"楼层"),
    ROOM(2,"房间");

    private int code;
    private String value;

    ScDormitoryfloorTypeEnum(int code,String value) {
        this.value = value;
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
