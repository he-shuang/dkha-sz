//package com.dkha.controller;
//
//import com.dkha.commons.tools.constant.Constant;
//import com.dkha.commons.tools.page.PageData;
//import com.dkha.commons.tools.utils.Result;
//import com.dkha.dto.FvScDoorRecordDTO;
//import com.dkha.service.FvScDoorRecordService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;
//import java.text.SimpleDateFormat;
//import java.util.Map;
//
//
///**
// * 开门记录表
// *
// * @author linhc linhc@dkay-cn.com
// * @since v1.0.0 2020-09-25
// */
//@RestController
//@RequestMapping("/fvscdoorrecord")
//@Api(tags="5寸门禁开门记录")
//public class FvScDoorRecordController {
//    @Autowired
//    private FvScDoorRecordService fvScDoorRecordService;
//
//    @GetMapping("page")
//    @ApiOperation("分页")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
//        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int"),
//        @ApiImplicitParam(name = "fName", value = "设备名称(模糊匹配)", paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "fserialNumber", value = "设备序列号(模糊匹配)", paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "deviceName", value = "设备名称(模糊匹配)", paramType = "query", dataType = "String"),
//        @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
//        @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String")
//    })
//    public Result<PageData<FvScDoorRecordDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) throws Exception {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String startDate = String.valueOf(params.get("startDate"));
//        String endDate = String.valueOf(params.get("endDate"));
//        if(!"null".equals(startDate) && !StringUtils.isBlank(startDate)){
//            params.put("startDate", String.valueOf(simpleDateFormat.parse(startDate).getTime() / 1000));
//        }
//        if(!"null".equals(endDate) && !StringUtils.isBlank(endDate)){
//            params.put("endDate", String.valueOf(simpleDateFormat.parse(endDate).getTime() / 1000));
//        }
//        PageData<FvScDoorRecordDTO> page = fvScDoorRecordService.page(params);
//
//        return new Result<PageData<FvScDoorRecordDTO>>().ok(page);
//    }
//
//    @GetMapping("{id}")
//    @ApiOperation("信息")
//    public Result<FvScDoorRecordDTO> get(@PathVariable("id") String id){
//        FvScDoorRecordDTO data = fvScDoorRecordService.getMyOne(id);
//
//        return new Result<FvScDoorRecordDTO>().ok(data);
//    }
//
//}