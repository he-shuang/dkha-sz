package com.dkha.model.aidoor.request;

public class SetLogo {

    /**
     * 操作类型：1:设置主Logo 2:删除主Logo 3:设置副Logo 4:删除副Logo
     */
    private int operation;

    private String mainLogoBase64;

    private String mainLogoId;

    private String viceLogoBase64;

    private String secondLogoId;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getMainLogoBase64() {
        return mainLogoBase64;
    }

    public void setMainLogoBase64(String mainLogoBase64) {
        this.mainLogoBase64 = mainLogoBase64;
    }

    public String getMainLogoId() {
        return mainLogoId;
    }

    public void setMainLogoId(String mainLogoId) {
        this.mainLogoId = mainLogoId;
    }

    public String getViceLogoBase64() {
        return viceLogoBase64;
    }

    public void setViceLogoBase64(String viceLogoBase64) {
        this.viceLogoBase64 = viceLogoBase64;
    }

    public String getSecondLogoId() {
        return secondLogoId;
    }

    public void setSecondLogoId(String secondLogoId) {
        this.secondLogoId = secondLogoId;
    }
}
