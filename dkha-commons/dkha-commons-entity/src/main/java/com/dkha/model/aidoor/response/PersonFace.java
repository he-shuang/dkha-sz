package com.dkha.model.aidoor.response;

public class PersonFace {
    private String imageName;
    private String imageBase64;
    private String imageMD5;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getImageMD5() {
        return imageMD5;
    }

    public void setImageMD5(String imageMD5) {
        this.imageMD5 = imageMD5;
    }
}
