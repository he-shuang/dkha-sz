package com.dkha.commons.tools.utils;

import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.enums.UwbMsgToOwnMsgEnum;

import java.util.List;
import java.util.Map;

/**
 * All rights 成都电科慧安
 *
 * @ClassName: UwbStatusInterface
 * @program: dkha-cloud
 * @description:
 * @author: linhuacheng
 * @create: 2020/8/26 17:30
 **/
public class UwbStatusInterface {

    public static String UwbStatus(String result){
        JSONObject jsonReturn = new JSONObject();
        jsonReturn.put("code", 0);
        jsonReturn.put("msg", "");
        if(result == null || result.isEmpty()){
            jsonReturn.put("code", 500);
            jsonReturn.put("msg", "UWB请求异常");
            return jsonReturn.toString();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        if(jsonObject == null || jsonObject.isEmpty()){
            jsonReturn.put("code", 500);
            jsonReturn.put("msg", "UWB请求异常");
            return jsonReturn.toString();
        }
        Integer status = jsonObject.getInteger("status");
        // UWB返回状态2XX为正常 其他异常
        if(status.intValue() < 200 || status.intValue() >= 300){
            jsonReturn.put("code", 500);
            String msg = jsonObject.getString("msg");
            UwbMsgToOwnMsgEnum uwbMsgToOwnMsgEnum = UwbMsgToOwnMsgEnum.getUwbMsgToOwnMsg(msg);
            if(!"unknown".equals(uwbMsgToOwnMsgEnum.getOwnMsg())){
                msg = uwbMsgToOwnMsgEnum.getOwnMsg();
            }
            jsonReturn.put("msg", msg);
            return jsonReturn.toString();
        }

        return jsonReturn.toString();
    }
}
