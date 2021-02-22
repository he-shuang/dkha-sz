package com.dkha.commons.fileupload.minio;


import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.config.ModuleConfig;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.util.ResourceUtils;
/**
 * minio 上传工具类
 */
@Component
@Slf4j
public class MinioUtil {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    private ModuleConfig moduleConfig;


    /**
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     * @Title: uploadImage
     * @Description:上传图片
     */
    public JSONObject uploadImage(InputStream inputStream,String folderName, String fileName, String suffix) throws Exception {
        return upload(inputStream,folderName, fileName, suffix, "image/jpeg");
    }

    /**
     * 上传图片
     *
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     */
    public JSONObject uploadImage(InputStream inputStream, String suffix) throws Exception {
        return upload(inputStream, null,null, suffix, "image/jpeg");
    }

    /**
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     * @Title: uploadVideo
     * @Description:上传视频
     */
    public JSONObject uploadVideo(InputStream inputStream,String folderName, String fileName, String suffix) throws Exception {
        return upload(inputStream, folderName,fileName, suffix, "video/mp4");
    }


    /**
     * @param suffix
     * @return
     * @throws Exception
     * @Title: uploadVideo
     * @Description:上传文件
     */
    public JSONObject uploadFile(byte[] data,String folderName, String fileName, String suffix) throws Exception {
        return upload(new ByteArrayInputStream(data),folderName, fileName, suffix, "application/octet-stream");
    }
    /**
     * 上传字符串大文本内容
     *
     * @param str
     * @return
     * @throws Exception
     * @Title: uploadString
     * @Description:描述方法
     */
    public JSONObject uploadString(String str,String folderName, String fileName) throws Exception {
        if (!StringUtils.isEmpty(str)) {
            return new JSONObject();
        }
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return upload(inputStream,folderName, fileName, null, "text/html");
    }


    /**
     * @return
     * @throws Exception
     * @Title: upload
     * @Description:上传主功能
     */
    private JSONObject upload(InputStream inputStream,String folderName, String fileName, String suffix, String contentType) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(bucketName);
        if (!isExist) {
            // 创建一个名为asiatrip的存储桶，用于存储文件。
            minioClient.makeBucket(bucketName);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        String moduleConfigName = moduleConfig.getName();
        if(StringUtils.isNotBlank(folderName)){
            moduleConfigName = moduleConfigName+ "/" + folderName;
        }
        String objectName = moduleConfigName + "/" + ymd + "/" + (StringUtils.isBlank(fileName) ? getUniqueFileName() : fileName) + (suffix != null ? "." + suffix : "");
        minioClient.putObject(bucketName, objectName, inputStream, Long.valueOf(inputStream.available()),
                null, null, contentType);

        String url = minioClient.getObjectUrl(bucketName, objectName);
        map.put("flag", "0");
        map.put("mess", "上传成功");
        map.put("url", url);
        map.put("fullName", objectName);
        map.put("path", objectName);
        return map;
    }

    /**
     * 获取上传对象信息
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public ObjectStat objectStat(String bucketName, String objectName) throws Exception {
        MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
        ObjectStat objectStat = minioClient.statObject(bucketName, objectName);
        return objectStat;
    }

    /**
     * 删除多个
     *
     * @param bucketName
     * @param objectNames
     * @return
     * @throws Exception
     */
    public JSONObject removeObjects(String bucketName, List<String> objectNames) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
        for (Result<DeleteError> errorResult : minioClient.removeObjects(bucketName, objectNames)) {
            DeleteError error = errorResult.get();
            System.out.println("Failed to remove '" + error.objectName() + "'. Error:" + error.message());
            map.put("results", error);
        }
//        Iterable<Result<DeleteError>> results =minioClient.removeObjects(bucketName,objectNames);
//        map.put("results",results);
        return map;
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public JSONObject removeObject(String bucketName, String objectName) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
        minioClient.removeObject(bucketName, objectName);
        map.put("results", "ok");
        return map;
    }

    /**
     * 根据存储箱和前缀查询对象集合
     *
     * @param bucketName
     * @param prefix     文件对象前缀可以不传）
     * @return
     */
    public Iterable<Result<Item>> listObjects(String bucketName, String prefix) {
        Iterable<Result<Item>> myObjects = new ArrayList<>();
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            // Check whether 'mybucket' exists or not.
            boolean found = minioClient.bucketExists(bucketName);
            if (found) {
                //如果prefix为null或者‘’
                if ("".equals(prefix) || prefix == null) {
                    myObjects = minioClient.listObjects(bucketName);
                } else {
                    //前缀查询
                    myObjects = minioClient.listObjects(bucketName, prefix);
                }
            } else {
                System.out.println("mybucket does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e);
        }
        return myObjects;
    }

    /**
     * 根据桶和前缀输出里面所有的信息
     *
     * @param bucketName
     * @param prefix
     */
    public void printObjectsMsg(String bucketName, String prefix) {
        Iterable<Result<Item>> results = listObjects(bucketName, prefix);
        for (Result<Item> result : results) {
            Item item = null;
            try {
                item = result.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info(item.lastModified() + ", " + item.size() + ", " + item.objectName());
        }
    }
    /**
     * 获取当前系统路径
     */
    public static String getUploadPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) path = new File("");
        File upload = new File(path.getAbsolutePath(), "static/upload/");
        if (!upload.exists()) upload.mkdirs();
        return upload.getAbsolutePath();
    }
    /**
     * 以流的形式下载文件
     *
     * @param objectName 对象名称
     * @return
     */
    public String downloadFileToServeLocal(String objectName, String fileName) {
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            InputStream inputStream = minioClient.getObject(bucketName, objectName);
            BufferedInputStream br = new BufferedInputStream(inputStream);
            String filepath = getUploadPath();

            String savepath=filepath  + fileName;

            File filedownload=new File(savepath);
            if(!filedownload.exists()){
                filedownload.createNewFile();
            }
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(new File(savepath))) ;
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            br.close();
            out.close();
            return savepath;
        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }

    }
    public void downloadFile(String objectName, HttpServletResponse response, String fileName) {
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            InputStream inputStream = minioClient.getObject(bucketName, objectName);
            OutputStream os = response.getOutputStream();
            BufferedInputStream br = new BufferedInputStream(inputStream);
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            response.setContentType("application/octet-stream");
//            response.setHeader("Access-Control-Allow-Origin", "localhost:8001");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
            OutputStream out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            br.close();
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public byte[] getImgByte(String objectName) {
        byte[] bytes = null;
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            InputStream inputStream = minioClient.getObject(bucketName, objectName);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len=inputStream.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            inputStream.close();
            bytes = outStream.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return bytes;
    }


    public   String downloadFiletoBase64( String objectName) {
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            InputStream inputStream = minioClient.getObject(bucketName, objectName);
            BufferedInputStream br = new BufferedInputStream(inputStream);
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;

            while ((len = br.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            br.close();

            byte[] data=outputStream.toByteArray();
            if(data!=null&&data.length>0)
                return Base64.getMimeEncoder().encodeToString(data);
            else
                return  "";

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return  "";
    }

    /**
     * 获取唯一文件名
     *
     * @return
     */
    public String getUniqueFileName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 通过图片路径获取桶名称
     *
     * @param fileUrl
     * @return 桶名称
     */
    public String getBucketNameByUrl(String fileUrl) {
        String[] splitArray = fileUrl.split("/");
        return splitArray[3];
    }


    /**
     * 通过图片路径获取对象名称/文件名称
     *
     * @param fileUrl
     * @return
     */
    public String getObjectNameByUrl(String fileUrl) {
        String[] splitArray = fileUrl.split(getBucketNameByUrl(fileUrl));
        String splitResult = splitArray[1].replaceFirst("/", "");
        //临时地址处理
        if (splitResult.contains("?")) {
            String[] split = splitResult.split("\\?");
            return split[0];
        }
        return splitResult;
    }

    /**
     * 获取文件名称 包括后缀
     *
     * @param fileUrl
     * @return
     */
    public String getFileName(String fileUrl) {
        String[] split = fileUrl.split("/");
        return split[split.length - 1];
    }

    /**
     * 截取minio有效的url 将问号后面的所有内容去掉
     *
     * @param minioUrl
     * @return
     */
    public String interceptValidUrl(String minioUrl) {
        if (StringUtils.isNotEmpty(minioUrl)) {
            return minioUrl.substring(0, minioUrl.indexOf("?"));
        }
        return null;
    }


}
