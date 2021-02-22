package com.dkha.commons.tools.utils;

import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.exception.RenException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * All rights 成都电科慧安
 *
 * @ClassName: CloseableHttpClientToInterface
 * @program: dkha-cloud
 * @description:
 * @author: linhuacheng
 * @create: 2020/8/26 15:50
 **/
public class CloseableHttpClientToInterface {
    private static final Logger log = LoggerFactory.getLogger(CloseableHttpClientToInterface.class);
    /**
     * 以get方式调用第三方接口
     * @param url
     * @return
     */
    public static String doGet(String url){
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        try {
            //API_UID自定义header头
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            get.addHeader("Content-type","application/json; charset=utf-8");
            get.setHeader("Accept", "application/json");
            HttpResponse response = httpClient.execute(get);
            //返回json格式
            String res = EntityUtils.toString(response.getEntity());
            return res;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RenException(e.getMessage());
        } finally {
//            if (httpClient != null){
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                    throw new RenException(e.getMessage());
//                }
//            }
        }
    }

    /**
     * 以post方式调用第三方接口
     * @param url
     * @param userId
     * @param param
     * @return
     */
    public static String doPost(String url, String userId, String param){
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        try {
            //API_UID自定义header头
            post.addHeader("API_UID", userId);
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            post.addHeader("Content-type","application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            StringEntity s = new StringEntity(param, Charset.forName("UTF-8"));
            s.setContentEncoding("UTF-8");
            //发送json数据需要设置contentType
            s.setContentType("application/json; charset=utf-8");
            //设置请求参数
            post.setEntity(s);
            HttpResponse response = httpClient.execute(post);
            //返回json格式
            String res = EntityUtils.toString(response.getEntity());
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RenException(e.getMessage());
        } finally {
//            if (httpClient != null){
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                    throw new RenException(e.getMessage());
//                }
//            }
        }
    }

    /**
     * 以put方式调用第三方接口
     * @param url
     * @param userId
     * @param param
     * @return
     */
    public static String doPut(String url, String userId, String param){
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut(url);
        try {
            //API_UID自定义header头
            put.addHeader("API_UID", userId);
            put.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            put.addHeader("Content-type","application/json; charset=utf-8");
            put.setHeader("Accept", "application/json");

            StringEntity s = new StringEntity(param, Charset.forName("UTF-8"));
            s.setContentEncoding("UTF-8");
            //发送json数据需要设置contentType
            s.setContentType("application/json; charset=utf-8");
            //设置请求参数
            put.setEntity(s);
            HttpResponse response = httpClient.execute(put);
            //返回json格式
            String res = EntityUtils.toString(response.getEntity());
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RenException(e.getMessage());
        }finally {
//            if (httpClient != null){
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                    throw new RenException(e.getMessage());
//                }
//            }
        }
    }

    /**
     * 以delete方式调用第三方接口
     * @param url
     * @param userId
     * @return
     */
    public static String doDelete(String url, String userId){
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpDelete delete = new HttpDelete(url);
        try {
            //API_UID自定义header头
            delete.addHeader("API_UID", userId);
            delete.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            delete.addHeader("Content-type","application/json; charset=utf-8");
            delete.setHeader("Accept", "application/json");
            HttpResponse response = httpClient.execute(delete);
            //返回json格式
            String res = EntityUtils.toString(response.getEntity());
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RenException(e.getMessage());
        }finally {
//            if (httpClient != null){
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                    throw new RenException(e.getMessage());
//                }
//            }
        }
    }

    /**
     * 以post方式调用第三方接口 application/x-www-form-urlencoded
     * @return
     */
    public static String sendPost(String urlParam, Map<String, String> params, String charset) {
        StringBuffer resultBuffer = null;
        // 构建请求参数
        StringBuffer sbParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                sbParams.append(e.getKey());
                sbParams.append("=");
                sbParams.append(e.getValue());
                sbParams.append("&");
            }
        }
        URLConnection con = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        try {
            URL realUrl = new URL(urlParam);
            // 打开和URL之间的连接
            con = realUrl.openConnection();
            // 设置通用的请求属性
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            con.setDoOutput(true);
            con.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            osw = new OutputStreamWriter(con.getOutputStream(), charset);
            if (sbParams != null && sbParams.length() > 0) {
                // 发送请求参数
                osw.write(sbParams.substring(0, sbParams.length() - 1));
                // flush输出流的缓冲
                osw.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            resultBuffer = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
            String temp;
            while ((temp = br.readLine()) != null) {
                resultBuffer.append(temp);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RenException(e.getMessage());
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    osw = null;
                    log.error(e.getMessage());
                    throw new RenException(e.getMessage());
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                    log.error(e.getMessage());
                    throw new RenException(e.getMessage());
                }
            }
        }
        return resultBuffer.toString();
    }

    /**
     * 人员信息有更新推送通知星网云联
     * @param url
     * @param uwbOpUserId UWB操作用户ID
     */
    public static void uwbPersonPush(String url, String uwbOpUserId){
        String curUrlSaveUser = url + "/message/message/mqWs";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("needRecord", false);
        jsonParam.put("type", "person");
        jsonParam.put("message", "");
        doPost(curUrlSaveUser, uwbOpUserId, jsonParam.toString());
    }
    /**
     * 报警信息有更新推送通知星网云联
     * @param url
     * @param alarmMassge  用这个对象 AlarmMassge
     * alarmMassge.setAlarmType("401");
     * alarmMassge.setDeid("1299182901667041282");
     * deviceMassge.setStatus(1);
     * alarmMassge.setId("1");
     * deviceMassge.setType(DeviceTypeEnum.DEVICE_TYPE_DL.value());
     * jsonParam.put("message", JSON.toJSON(alarmMassge));
     * @param uwbOpUserId UWB操作用户ID
     */
    public static void uwbAlarmPush(String url, String alarmMassge, String uwbOpUserId){
        String curUrlSaveUser = url + "/message/message/mqWs";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("needRecord", false);
        jsonParam.put("type", "alarm");
        jsonParam.put("message", alarmMassge);
        doPost(curUrlSaveUser, uwbOpUserId, jsonParam.toString());
    }
    /**
     * 访客星网云联指定访问楼层更新规则
     * @param url
     * @param tagIdsStr 标签串"1,2"
     * @param mapId 地图ID
     * @param floor 楼层
     * @param uwbOpUserId UWB操作用户ID
     * @return
     */
    public static String uwbFkRule(String url, String tagIdsStr, Long mapId, String floor, String uwbOpUserId){
        String curUrlFenceRule = url + "/alarm/fenceRule";
        JSONObject jsonParam3 = new JSONObject();
        jsonParam3.put("alarmType", 121);
        jsonParam3.put("tagIds", tagIdsStr);

        JSONObject jsonParam4 = new JSONObject();
        jsonParam4.put("mapId", mapId);
        jsonParam4.put("floor", floor);

        jsonParam3.put("arguments", jsonParam4.toString());

        return doPost(curUrlFenceRule, uwbOpUserId, jsonParam3.toString());
    }
    /**
     * 人员信息有更新推送通知星网云联更新人员角色电围规则
     * @param url
     * @param conMessage  用这个对象 conMessage
     * {
     *    "add": {         // 新增
     *        "1": [1,2,3] // "UWB角色ID" : [标签ID,标签ID]
     *    },
     *     "delete": {     //删除
     *         "2": [4,5,6]
     *     }
     * }
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbPersonfenceCache(String url, String conMessage, String uwbOpUserId){
        String curUrlfenceCache = url + "/alarm/observe/fenceCache";
        return doPost(curUrlfenceCache, uwbOpUserId, conMessage);
    }
    /**
     * 人员信息有更新推送通知星网云联更新人员角色电围规则 (无需传参全扫描更新) 建议批量导入绑定标签时用
     * @param url
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbPersonGroupsChange(String url, String uwbOpUserId){
        String curUrlPersonGroupsChange = url + "/alarm/observe/personGroupsChange";
        return doPost(curUrlPersonGroupsChange, uwbOpUserId, "");
    }
    /**
     * UWB用户新增
     * @param url
     * @param conMessage 新增用户数据
     * {
     *   "email" : "string",
     *   "loginName" : "string",
     *   "nickname" : "string",
     *   "phone" : "string",
     *   "roleIds" : [ 0 ],
     *   "password": "string",
     *   "status" : 1//状态:0：禁用 1：正常
     * }
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbAddUser(String url, String conMessage, String uwbOpUserId){
        String curAddUser = url + "/users";
        return doPost(curAddUser, uwbOpUserId, conMessage);
    }
    /**
     * UWB用户编辑
     * @param url
     * @param uwbUserId UWB用户ID
     * @param conMessage 需要更新用户数据
     * {
     *   "email" : "string",
     *   "nickname" : "string",
     *   "phone" : "string",
     *   "roleIds" : [ 0 ],
     *   "status" : 1//状态:0：禁用 1：正常
     * }
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbUpdateUser(String url, Long uwbUserId, String conMessage, String uwbOpUserId){
        String curUpdateUser = url + "/users/" + uwbUserId;
        return doPut(curUpdateUser, uwbOpUserId, conMessage);
    }
    /**
     * UWB用户密码重置
     * @param url
     * @param uwbUserId UWB用户ID
     * @param conMessage 重置的密码
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbRepasswordUser(String url, Long uwbUserId, String conMessage, String uwbOpUserId){
        String curRepasswordUser = url + "/users/" + uwbUserId + "/password";
        return doPut(curRepasswordUser, uwbOpUserId, conMessage);
    }
    /**
     * UWB用户设置状态
     * @param url
     * @param uwbUserId UWB用户ID
     * @param conMessage 状态数据
     * {
     *   "status" : "string" //状态:0：禁用 1：正常
     * }
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbStatusUser(String url, Long uwbUserId, String conMessage, String uwbOpUserId){
        String curStatusUser = url + "/users/" + uwbUserId + "/status";
        return doPut(curStatusUser, uwbOpUserId, conMessage);
    }
    /**
     * UWB解绑标签
     * @param url
     * @param conMessage 标签ID数组串
     * [1,2,3]
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbUnbindTagAllFence(String url, String conMessage, String uwbOpUserId){
        String curUnbindTagAllFence = url + "/alarm/observe/unbindTagAllFence";
        return doPost(curUnbindTagAllFence, uwbOpUserId, conMessage);
    }
    /**
     * UWB替换标签
     * @param url
     * @param conMessage
     * {
     * 	"oldId": 3, // 原标签ID
     * 	"newId": 15 // 新标签ID
     * }
     * @param uwbOpUserId UWB操作用户ID
     */
    public static String uwbReplaceTag(String url, String conMessage, String uwbOpUserId){
        String curReplaceTag = url + "/alarm/observe/replaceTag";
        return doPost(curReplaceTag, uwbOpUserId, conMessage);
    }

}
