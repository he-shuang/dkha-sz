package com.dkha.model.aidoor.request;

public class PersonAddFace  {

    /**
     * 照片名称
     */
    private String imageName;

    /**
     * 照片base64编码
     */
    private String imageBase64;

    /**
     * 照片base64编码md5值
     */
    private String imageMD5;

    private String faceId;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getImageMD5() {
        return imageMD5;
    }

    public void setImageMD5(String imageMD5) {
        this.imageMD5 = imageMD5;
    }

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
}
