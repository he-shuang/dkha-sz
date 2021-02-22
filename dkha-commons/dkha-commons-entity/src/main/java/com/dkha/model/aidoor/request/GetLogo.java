package com.dkha.model.aidoor.request;

public class GetLogo {

    public static final int TYPE_MAIN = 0;

    public static final int TYPE_SECOND = 1;

    public static final int TYPE_BOTH = 2;

    private int type;

    private String mainLogoId;

    private String secondLogoId;

    public String getMainLogoId() {
        return mainLogoId;
    }

    public void setMainLogoId(String mainLogoId) {
        this.mainLogoId = mainLogoId;
    }

    public String getSecondLogoId() {
        return secondLogoId;
    }

    public void setSecondLogoId(String secondLogoId) {
        this.secondLogoId = secondLogoId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isMain() {
        return type == TYPE_MAIN;
    }

    public boolean isSecond() {
        return type == TYPE_SECOND;
    }

    public boolean isBoth() {
        return type == TYPE_BOTH;
    }
}
