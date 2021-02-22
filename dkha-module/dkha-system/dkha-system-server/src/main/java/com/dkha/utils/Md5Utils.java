package com.dkha.utils;

import java.security.MessageDigest;

/**
 * md5加密工具类
 * @author
 * @time
 */
public class Md5Utils {

    private static final String CHAR_MD5_NAME = "MD5";
    private static final String TYPE_ENCODING_METHOD = "utf-8";

    /**
     * MD5加密
     * @param origin 字符
     * @return
     */
    public static String encode(String origin){
        String resultString = null;
        try{
            MessageDigest md = MessageDigest.getInstance(CHAR_MD5_NAME);
            resultString = bytes2hex(md.digest(origin.getBytes(TYPE_ENCODING_METHOD)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultString;
    }

    private static String bytes2hex(byte[] buffer) {
        StringBuilder resultSb = new StringBuilder();
        if (buffer == null || buffer.length == 0) {
            return resultSb.toString();
        }
        for (byte aBuffer : buffer) {
            resultSb.append(byte2hex(aBuffer));
        }
        return resultSb.toString();
    }

    private static String byte2hex(byte buffer) {
        String temp = Integer.toHexString(buffer & 0xFF);
        if (temp.length() == 1) {
            temp = "0" + temp;
        }
        return temp;
    }
}
