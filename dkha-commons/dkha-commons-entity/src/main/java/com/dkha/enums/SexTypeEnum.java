package com.dkha.enums;

public enum SexTypeEnum {
    BOY("1","男"),
    GIRL("0","女");

    public String code;
    public String value;

    SexTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
