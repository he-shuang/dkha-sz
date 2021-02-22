package com.dkha.dto.doorcontrol;

import lombok.Data;

/**
 * 人脸数据
 */
@Data
public class PersonFaceBase64 {
   private  String imageName; //	照片名	String
    private  String  imageBase64; //	照片Base64编码	String
    private  String imageMD5; //	照片Base64编码MD5值	String
}
