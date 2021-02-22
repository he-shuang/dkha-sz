package com.dkha.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dao.FvScDeviceDao;
import com.dkha.dao.ScAidoorfivePersonlistDao;
import com.dkha.dto.*;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScAidoorfivePersonlistEntity;
import com.dkha.mq.FiveDoor.FiveDoorReceiveDataSender;
import com.dkha.service.FvScDeviceService;
import com.dkha.service.ScAidoorfiveOpenLogService;
import com.dkha.service.ScStudentsService;
import com.dkha.service.ScWorkersarchivesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 设备表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@RestController
@RequestMapping("/fvscdevice")
@Api(tags = "5寸门禁设备")
@Slf4j
public class FvScDeviceController {
    @Autowired
    private FvScDeviceService fvScDeviceService;
    @Autowired
    private FvScDeviceDao fvScDeviceDao;
    @Autowired
    private ScAidoorfivePersonlistDao scAidoorfivePersonlistDao;
    @Autowired
    private FiveDoorReceiveDataSender fiveDoorRegisterDataSender;
    @Autowired
    private ScAidoorfiveOpenLogService scAidoorfiveOpenLogService;
    @Autowired
    private ScStudentsService scStudentsService;
    @Autowired
    private ScWorkersarchivesService scWorkersarchivesService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "fName", value = "设备名称(模糊匹配)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "fserial", value = "设备系列号(模糊匹配)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eqType", value = "设备状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "fstate", value = "设备在线状况", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String")
    })
    public Result<PageData<FvScDeviceDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDate = String.valueOf(params.get("startDate"));
        String endDate = String.valueOf(params.get("endDate"));
        String eqType = String.valueOf(params.get("eqType"));
        if (!"null".equals(startDate) && !StringUtils.isBlank(startDate)) {
            params.put("startDate", String.valueOf(simpleDateFormat.parse(startDate).getTime() / 1000));
        }
        if (!"null".equals(endDate) && !StringUtils.isBlank(endDate)) {
            params.put("endDate", String.valueOf(simpleDateFormat.parse(endDate).getTime() / 1000));
        }
        if (!"null".equals(eqType) && !StringUtils.isBlank(eqType)) {
            params.put("eqType", eqType);
        }
        PageData<FvScDeviceDTO> page = fvScDeviceService.page(params);

        List<FvScDeviceDTO> list = page.getList();
        //查询人数
        for (FvScDeviceDTO fvScDeviceDTO : list) {
            Long personNum = scAidoorfivePersonlistDao.getPersonNum(fvScDeviceDTO.getFSerial(),fvScDeviceDTO.getFPassword());
            fvScDeviceDTO.setFFaceTotal(personNum.intValue());
        }

        return new Result<PageData<FvScDeviceDTO>>().ok(page);
    }

    @GetMapping(value = {"{id}","{id}/{name}"})
    @ApiOperation("信息")
    public Result<FvScDeviceDTO> get(@PathVariable("id") String id,@PathVariable(value = "name",required = false) String name) {
        FvScDeviceDTO data = fvScDeviceService.getMyOne(id);
        if (data != null) {
            QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("serial", data.getFSerial());
            queryWrapper.eq("password", data.getFPassword());
            queryWrapper.like(StringUtils.isNotEmpty(name),"username",name);

            List<ScAidoorfivePersonlistEntity> scAidoorfivePersonlistEntities = scAidoorfivePersonlistDao.selectList(queryWrapper);
            data.setPersonList(scAidoorfivePersonlistEntities);
            return new Result<FvScDeviceDTO>().ok(data);
        } else {
            return new Result<FvScDeviceDTO>().error("查询值错误，查询不到信息");
        }


    }

    @GetMapping("pageEqToFace")
    @ApiOperation("设备对应下发人员分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "fDeviceId", value = "设备ID", paramType = "query", required = true, dataType = "String")
    })
    public Result<PageData<FvScDeviceFaceDTO>> pageEqToFace(@ApiIgnore @RequestParam Map<String, Object> params) throws Exception {

        PageData<FvScDeviceFaceDTO> page = fvScDeviceService.pageEqToFace(params);

        return new Result<PageData<FvScDeviceFaceDTO>>().ok(page);
    }

    @PostMapping("saveEqToFace")
    @ApiOperation("设备人脸下发")
    public Result saveEqToFace(@RequestBody DoorAndPersonListDTO doorAndPersonListDTO) throws Exception {

        fvScDeviceService.saveEqToFace(doorAndPersonListDTO, null);
        return new Result();
    }

    @PostMapping("saveEqToFaceTwo")
    @ApiOperation("二楼设备人脸下发")
    public Result saveEqToFaceTwo(@RequestBody DoorAndPersonListDTO doorAndPersonListDTO) throws Exception {
        fvScDeviceService.saveEqToFaceTwo(doorAndPersonListDTO, null);
        return new Result();
    }

    @PostMapping("saveEqToFaceTwoAll")
    @ApiOperation("二楼设备所有人脸下发")
    public Result saveEqToFaceTwoAll(@RequestBody AidoorfivePassword aidoorfivePassword) throws Exception {
        DoorAndPersonListDTO doorAndPersonListDTO = new DoorAndPersonListDTO();
        List<String> allStudentId = scStudentsService.getAllId();
        List<String> allWorkerId = scWorkersarchivesService.getAllId();
        List<String> ids = new ArrayList<>();
        ids.addAll(allStudentId);
        ids.addAll(allWorkerId);

        doorAndPersonListDTO.setFiveDoors(Collections.singletonList(aidoorfivePassword));
        doorAndPersonListDTO.setIds(ids);

        fvScDeviceService.saveEqToFaceTwo(doorAndPersonListDTO, null);
        return new Result();
    }

    @PostMapping("setUpScree")
    @ApiOperation("设备屏保下发")
    public Result setUpScree(@RequestParam("file") MultipartFile file) throws Exception {
        fvScDeviceService.setUpScree(file);
        return new Result();
    }

    @PostMapping("deleteFace")
    @ApiOperation("删除人脸")
    public Result updatePermissionFace(@RequestParam("serial") String serial,
                                       @RequestParam("password") String password,
                                       @RequestParam("imgId") String imgId) throws Exception {
        //删除已经下发的人脸
        fvScDeviceService.deletePermissionFace(serial, password, imgId);
        //删除人脸下发记录
        QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("img_id", imgId);
        scAidoorfivePersonlistDao.delete(queryWrapper);
        return new Result();
    }

    @PostMapping("doorOpen")
    @ApiOperation("远程开门")
    public Result doorOpen(@RequestParam("serial") String serial,
                           @RequestParam("password") String password) throws Exception {
        fvScDeviceService.doorOpen(serial, password);
        return new Result();
    }

    @PostMapping("doorReset")
    @ApiOperation("重启设备")
    public Result doorReset(@RequestParam("serial") String serial,
                            @RequestParam("password") String password) throws Exception {
        if (fvScDeviceService.doorReset(serial, password)) {
            return new Result("设备重启成功！");
        } else {
            return new Result().error("设备重启失败！");
        }
    }

    @PostMapping("redownFace")
    @ApiOperation("重新下发")
    public Result reSendDownFace(@RequestParam("serial") String serial,
                                 @RequestParam("password") String password) throws Exception {

        //设备序列号、密码集合
        List<AidoorfivePassword> fiveDoors = new ArrayList<>();
        //人员ID集合
        List<String> ids = new ArrayList<>();

        //根据设备序列号和设备密码从下发记录中获取相关数据
        QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("serial", serial);
        queryWrapper.eq("password", password);
        List<ScAidoorfivePersonlistEntity> listfivelist = scAidoorfivePersonlistDao.selectList(queryWrapper);

        //判断下发记录中有无相关数据，如果存在则重新进行下发
        if (listfivelist.size() > 0) {

            //传入设备参数
            AidoorfivePassword aidoorfivePassword = new AidoorfivePassword();
            aidoorfivePassword.setFSerial(serial);
            aidoorfivePassword.setFPassword(password);
            aidoorfivePassword.setFName(listfivelist.get(0).getDrNum());
            //将设备参数放入集合作为人脸下发参数之一
            fiveDoors.add(aidoorfivePassword);

            //遍历下发记录将人员ID存入集合
            for (ScAidoorfivePersonlistEntity scAidoorfivePersonlistEntity : listfivelist) {
                ids.add(scAidoorfivePersonlistEntity.getUserId());
            }

            //人脸下发参数存入
            DoorAndPersonListDTO doorAndPersonListDTO = new DoorAndPersonListDTO();
            doorAndPersonListDTO.setFiveDoors(fiveDoors);
            doorAndPersonListDTO.setIds(ids);

            /*下发之前先删除旧的下发记录和旧的权限*/
            //删除下发记录
            scAidoorfivePersonlistDao.delete(queryWrapper);
            //删除设备中的所有权限
            fvScDeviceService.deletePermissionFaceAll(serial, password, listfivelist);

            //人脸下发
            fvScDeviceService.saveEqToFace(doorAndPersonListDTO, listfivelist);
            return new Result();
        } else {
            return new Result().error("该设备没有历史人脸信息！");
        }

    }


    @PostMapping("faceRegister")
    @ApiOperation("接收人脸注册状态")
    public void faceRegister(@RequestParam("serial") String serial,
                             @RequestParam("userId") String userId,
                             @RequestParam("status") String status
    ) throws Exception {

        JSONObject registerJsonObject = new JSONObject();
        registerJsonObject.put("serial", serial);
        registerJsonObject.put("userId", userId);
        registerJsonObject.put("status", status);
        //将接收到的人脸注册状态参数放进消息队列
//        fiveDoorRegisterDataSender.pushFiveDoorRegisterDataToQueue(registerJsonObject.toString());

    }

    @PostMapping("faceOpenLog")
    @ApiOperation("接收人脸通行记录")
    public void faceOpenLog(@RequestParam("serial") String serial,
                            @RequestParam("count") String count,
                            @RequestParam("cardNo") String cardNo,
                            @RequestParam("openTime") String openTime,
                            @RequestParam("openType") String openType,
                            @RequestParam("status") String status
    ) throws Exception {
        // log.error("接收到的开门记录:{}", "设备系列号:" + serial + " /人脸十进制ID:" + cardNo + " /开门时间戳:" + openTime);

        JSONObject openLogJsonObject = new JSONObject();
        openLogJsonObject.put("serial", serial);
        openLogJsonObject.put("cardNo", cardNo);
        openLogJsonObject.put("openTime", openTime);
        //将接收到的五寸门禁通行记录所需参数放进消息队列
        fiveDoorRegisterDataSender.pushFiveOpenLogDataToQueue(openLogJsonObject.toString());
    }

    public static void main(String[] args) {
        Date date = DateUtils.secondToDate(1603790951);
        String format = DateUtils.format(DateUtils.secondToDate(1603790951), DateUtils.DATE_TIME_PATTERN);
        System.out.println(format);
    }

}