package com.dkha.enums;

/**
 * 叶子节点枚举
 */
public enum RegionLeafEnum {
    YES(1),
    NO(0);

    private int value;

    RegionLeafEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
