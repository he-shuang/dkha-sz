package com.dkha.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class FileUtils {

    /**
     * 保存图片至本地
     * @param base64
     */
    public static void saveImageBase64(String base64, String imageName){
        saveImageBase64(base64, imageName, "jpg");
    }

    /**
     * 保存图片至本地
     * @param base64
     */
    public static void saveImageBase64Png(String base64, String imageName){
        saveImageBase64(base64, imageName, "png");
    }

    /**
     * 保存图片至本地
     * @param base64
     */
    public static void saveImageBase64(String base64, String imageName, String format){
        BufferedImage image;
        byte[] imageByte;
        try {
            imageByte = Base64.getMimeDecoder().decode(base64);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(new ByteArrayInputStream(imageByte));
            bis.close();
            File outputfile = new File("d:\\" + imageName);
            ImageIO.write(image, format, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片转base64
     * @param imagePath
     * @return
     */
    public static String image2Base64(String imagePath) {
        InputStream is = null;
        byte[] data = null;
        try {
            is = new FileInputStream(imagePath);
            data = new byte[is.available()];
            is.read(data);
            is.close();
            return Base64.getMimeEncoder().encodeToString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public  static String image2Base64ByMino(String miniourl,String bucketName,String imagePath){
        HttpURLConnection conn=null;
        URL url=null;
        try{
            url=new URL(miniourl+"/"+bucketName+"/"+imagePath);
            conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);//超时时间5秒
            if(conn.getResponseCode()==200){
              InputStream is=  conn.getInputStream();
                byte[] data = null;
                data = new byte[is.available()];
                is.read(data);
                is.close();
                return Base64.getMimeEncoder().encodeToString(data);
            }
        }catch (Exception e){

        }
        return "";
    }

}
