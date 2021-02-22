package com.dkha.commons.tools.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileFormatVerify {


    private static Map<String, Object> fileFormat = null;

    /**
     * @Description  将常用需要校验的格式和区别其他格式的前几个字节写入map
     * @Param []
     * @return
     **/
    public  FileFormatVerify (){
        //if(fileFormat == null ){
        fileFormat = new HashMap<String, Object>();

        //JPEG
        fileFormat.put("jpg","FFD8FFE0");
        //PNG
        fileFormat.put("png","89504E47");
        fileFormat.put("zip","504B0304");
        fileFormat.put("rar","52617221");
        //office类型，包括doc、xls和ppt
//           fileFormat.put("office","D0CF11E0");
        /**
         * docx ,xlsx和zip   相同  doc 和 xls 相同
         */
        fileFormat.put("docx","504b0304");
        fileFormat.put("doc","d0cf11e0");

        fileFormat.put("xls","d0cf11e0");
        fileFormat.put("xlsx","504b0304");
        //fileFormat.put("pdf","255044462D312E");
        fileFormat.put("pdf","255044462");
        // }
    }

    /**
     * @Description 根据传入的文件获得后缀,获得指定文件格式byte[]数组中的前8位字符
     *              将传入文件转化为byte[]数组,取前8位.判断传入文件的前8位和我们指定好的文件byte[]的前8位是否相同,
     *              如果相同则文件格式没有被篡改,反之,文件后缀格式被篡改
     * @Param [file]
     * @return boolean 返回true 表示文件格式验证通过, 返回false 文件格式验证失败
     **/
    public boolean suffixVerify(MultipartFile file){
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        //根据文件的后缀获取,获取文件的byte[]的前8位
        if(fileFormat.containsKey(fileType.toLowerCase())){
            String fileByte8 = String.valueOf(fileFormat.get(fileType.toLowerCase())).toLowerCase();
            //获取传入文件的byte[]的前8位
            byte[] bytes = inputStream2ByteArray(file);
            String compareByte = bytesToHexString(bytes);
            //如果传入文件的byte[]的前8位和我们定义好的byte[]的前8位相同,验证通过.
            if (compareByte.startsWith(fileByte8)){
                //如果格式校验成功
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }



    /**
     * @Description  将file文件转化为byte[]
     * @Param [file]
     * @return byte[]
     **/
    public  byte[] inputStream2ByteArray(MultipartFile file){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream fis = null;
        byte[] buffer = null;

        try {
            fis = new ByteArrayInputStream(file.getBytes());


            //不用读取全部文件,只读文件前面的部分
            byte[] b = new byte[1024];
            fis.read(b);
            bos.write(b, 0, 1024);


            /**
             byte[] b = new byte[4];
             int n;
             while ((n = fis.read(b)) != -1){
             bos.write(b, 0, n);
             }
             */
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }finally {
            try {
                if(fis !=null){
                    fis.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                if(bos !=null){
                    bos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return buffer;
    }


    /**
     * @Description  取byte[]前8位的为字符串
     * @Param [src]
     * @return java.lang.String
     **/
    public  String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        //return stringBuilder.toString().substring(0, 8);
        return stringBuilder.toString();
    }

}
