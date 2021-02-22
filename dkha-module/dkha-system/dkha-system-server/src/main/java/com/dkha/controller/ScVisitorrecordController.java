package com.dkha.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dao.ScUwbLabelDao;
import com.dkha.dto.ScUwbLabelToInfoDTO;
import com.dkha.dto.ScVisitorrecordDTO;
import com.dkha.enums.ScUwbperRoleIdToUwbIdEnum;
import com.dkha.enums.SexTypeEnum;
import com.dkha.excel.ScVisitorrecordExcel;
import com.dkha.service.ScVisitorrecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


/**
 * 访客记录表
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scvisitorrecord")
@Api(tags="访客记录")
public class ScVisitorrecordController {
    @Autowired
    private ScVisitorrecordService scVisitorrecordService;
    @Resource
    private ScUwbLabelDao scUwbLabelDao;
    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.bucketName}")
    private String minioBucketName;

    @Autowired
    MinioUtil minioUtil;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "vrSex", value = "性别", paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "vrName", value = "姓名(模糊匹配)", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "deviceNumber", value = "设备编号", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String")
    })
//    @PreAuthorize("hasAuthority('system:scvisitorrecord:page')")
    public Result<PageData<ScVisitorrecordDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScVisitorrecordDTO> page = scVisitorrecordService.page(params);

        return new Result<PageData<ScVisitorrecordDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scvisitorrecord:info')")
    public Result<ScVisitorrecordDTO> get(@PathVariable("id") String id){
        ScVisitorrecordDTO data = scVisitorrecordService.get(id);

        return new Result<ScVisitorrecordDTO>().ok(data);
    }

    @GetMapping("/history/info/{id}")
    @ApiOperation("访客记录历史轨迹访客信息")
//    @PreAuthorize("hasAuthority('system:history:info')")
    public Result<ScUwbLabelToInfoDTO> getHistory(@PathVariable("id") String id){
        List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
        Map<Integer,Object> map = new HashMap<>();
        Map<Integer,String> map2 = new HashMap<>();
        for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++){
            Map<String,Object> curMap = uwbPerRoleNumList.get(i);
            String dict_label = String.valueOf(curMap.get("dict_label"));
            String dict_value = String.valueOf(curMap.get("dict_value"));

            if("访客".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.VISITOR.code(), dict_value);
                map2.put(ScUwbperRoleIdToUwbIdEnum.VISITOR.code(), dict_label);
                break;
            }
        }

        ScUwbLabelToInfoDTO dto = scVisitorrecordService.getMyScUwbLabelToInfo(id);
        if(dto == null){
            return new Result<ScUwbLabelToInfoDTO>().ok(dto);
        }
        dto.setDepartmentId(Long.parseLong(String.valueOf(map.get(dto.getEmptype()))));
        dto.setIconState(0);
        dto.setPersonState(1);
        dto.setPersonPic(minioUrl + "/" + minioBucketName + "/" + dto.getPersonPic());
        dto.setMoreinfo(StringTojsonString(dto.getMoreinfo(), String.valueOf(map2.get(dto.getEmptype()))));

        return new Result<ScUwbLabelToInfoDTO>().ok(dto);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scvisitorrecord:save')")
    public Result save(@RequestBody ScVisitorrecordDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        // 默认访客未归还uwb工牌
        dto.setVrReturnuwb(0);
        dto.setVrVistorbegintime(new Date());
        scVisitorrecordService.save(dto);

        return new Result();
    }

    @PutMapping("/returncard/{id}")
    @ApiOperation("还卡")
//    @PreAuthorize("hasAuthority('system:scvisitorrecord:returncard')")
    public Result returncard(@PathVariable("id") String id){

        UserDetail user = SecurityUser.getUser();
        scVisitorrecordService.returnCard(id, user.getId());

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scvisitorrecord:update')")
    public Result update(@RequestBody ScVisitorrecordDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scVisitorrecordService.update(dto);

        return new Result();
    }

    @GetMapping(value = "export")
    @ApiOperation(value = "导出")
//    @PreAuthorize("hasAuthority('system:scvisitorrecord:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScVisitorrecordDTO> list = scVisitorrecordService.list(params);
        int index = 1;
        for (ScVisitorrecordDTO scVisitorrecordDTO : list) {
            scVisitorrecordDTO.setNum(index);
            byte[] imgByte = minioUtil.getImgByte(scVisitorrecordDTO.getVrPhoneimg());
            scVisitorrecordDTO.setScPhotoimgByte(imgByte);
            index++;
        }

        ExcelUtils.exportExcelToTarget(response, null, list, ScVisitorrecordExcel.class);
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
                } else {
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
