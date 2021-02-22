package com.dkha.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dao.ScUwbLabelDao;
import com.dkha.dto.ScUwbLabelToInfoDTO;
import com.dkha.enums.ScUwbperRoleIdToUwbIdEnum;
import com.dkha.enums.SexTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights 成都电科慧安
 *
 * @ClassName: ScUwbLabelController
 * @program: dkha-cloud
 * @description:
 * @author: linhuacheng
 * @create: 2020/8/27 11:35
 **/
@RestController
@RequestMapping("scuwblabel")
@Api(tags="获取UWB标签绑定的信息")
public class ScUwbLabelController {
    @Resource
    private ScUwbLabelDao scUwbLabelDao;
    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.bucketName}")
    private String minioBucketName;

    @GetMapping("list")
    @ApiOperation("获取UWB标签所有信息")
    public Result<List<ScUwbLabelToInfoDTO>> myList(){
        List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
        Map<Integer,Object> map = new HashMap<>();
        Map<Integer,String> map2 = new HashMap<>();
        for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++){
            Map<String,Object> curMap = uwbPerRoleNumList.get(i);
            String dict_label = String.valueOf(curMap.get("dict_label"));
            String dict_value = String.valueOf(curMap.get("dict_value"));

            if("学生".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.STUDENT.code(), dict_value);
                map2.put(ScUwbperRoleIdToUwbIdEnum.STUDENT.code(), dict_label);
            } else if("教师".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.TEACHER.code(), dict_value);
                map2.put(ScUwbperRoleIdToUwbIdEnum.TEACHER.code(), dict_label);
            } else if("保洁".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.CLEANING.code(), dict_value);
                map2.put(ScUwbperRoleIdToUwbIdEnum.CLEANING.code(), dict_label);
            } else if("保安".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.SECURITYSTAFF.code(), dict_value);
                map2.put(ScUwbperRoleIdToUwbIdEnum.SECURITYSTAFF.code(), dict_label);
            } else if("重要设备".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.IMPEQUIPMENT.code(), dict_value);
                map2.put(ScUwbperRoleIdToUwbIdEnum.IMPEQUIPMENT.code(), dict_label);
            } else if("访客".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.VISITOR.code(), dict_value);
                map2.put(ScUwbperRoleIdToUwbIdEnum.VISITOR.code(), dict_label);
            }
        }

        List<ScUwbLabelToInfoDTO> list = scUwbLabelDao.getMyList();

        list.forEach(e ->
        {
            e.setDepartmentId(Long.parseLong(String.valueOf(map.get(e.getEmptype()))));
            e.setIconState(0);
            e.setPersonState(1);
            e.setPersonPic(minioUrl + "/" + minioBucketName + "/" + e.getPersonPic());
            e.setMoreinfo(StringTojsonString(e.getMoreinfo(), String.valueOf(map2.get(e.getEmptype()))));

        });
        return new Result<List<ScUwbLabelToInfoDTO>>().ok(list);
    }

    /**
     * 组装数据
     * @param moreinfo
     * @return
     */
    private String StringTojsonString(String moreinfo, String roleName){
        if(moreinfo == null || "null".equals(moreinfo)){
            return "";
        }

        List<JSONObject> list = new ArrayList<>();

        String[] moreinfoArr = moreinfo.split("@@");
        for(int i = 0,sizei = moreinfoArr.length;i < sizei;i++){
            String[] moreinfoCur = moreinfoArr[i].split("@");
            JSONObject jsonParam = new JSONObject();
            if(moreinfoCur.length > 0) {
                jsonParam.put("Lable", moreinfoCur[0]);
                if ("性别".equals(moreinfoCur[0])) {
//                    continue;
                    if (SexTypeEnum.BOY.code.equals(moreinfoCur[1])) {
                        jsonParam.put("Value", SexTypeEnum.BOY.value);
                    } else {
                        jsonParam.put("Value", SexTypeEnum.GIRL.value);
                    }
                } else if ("身份证".equals(moreinfoCur[0])) {
                    String curIdCard = moreinfoCur[1];
                    if (!StringUtils.isEmpty(curIdCard)) {
                        jsonParam.put("Value", curIdCard.substring(0, 6) + "********" + curIdCard.substring(14, curIdCard.length()));
                    } else {
                        jsonParam.put("Value", "");
                    }
                } else{
                    if (moreinfoCur.length > 1) {
                        jsonParam.put("Value", moreinfoCur[1]);
                    } else {
                        jsonParam.put("Value", "");
                    }
                }
            }

            list.add(jsonParam);
        }
        return list.toString();
    }
}
