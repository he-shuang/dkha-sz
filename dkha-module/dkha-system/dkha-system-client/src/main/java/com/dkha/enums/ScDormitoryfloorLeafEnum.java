package com.dkha.enums;

/**
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-08-24 11:07
 */
public enum ScDormitoryfloorLeafEnum {

    YES(1),
    NO(0);

    private int value;

    ScDormitoryfloorLeafEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
