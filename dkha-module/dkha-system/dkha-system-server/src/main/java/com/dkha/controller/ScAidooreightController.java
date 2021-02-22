
package com.dkha.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.aidoor.TransPictureResult;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.AidooreightPassword;
import com.dkha.dto.FaceListDataToDoorDTO;
import com.dkha.dto.ScAidooreightDTO;
import com.dkha.dto.doorcontrol.DeviceSetingInfo;
import com.dkha.entity.ScDeptDoorEntity;
import com.dkha.excel.ScAidooreightExcel;
import com.dkha.service.ScAidooreightService;
import com.dkha.service.ScDeptDoorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 智能设备主要分为：8英寸智能门禁设备
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-14
 */
@RestController
@RequestMapping("scaidooreight")
@Api(tags="智能设备主要分为：8英寸智能门禁设备")
@EnableAsync
public class ScAidooreightController {
    private static final Logger log = LoggerFactory.getLogger(ScAidooreightController.class);
    @Autowired
    private ScAidooreightService scAidooreightService;

    @Resource
    private ScDeptDoorService scDeptDoorService;

    /**
     * 权限列表
     * @return
     */
    @GetMapping("dept/{aeId}")
    public Result<List<ScDeptDoorEntity>> promise(@PathVariable("aeId") Long aeId){

        List<ScDeptDoorEntity> deptDoorByDoorId = scDeptDoorService.findDeptDoorByDoorId(aeId);
        return  new Result().ok(deptDoorByDoorId);
    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "devicename", value = "设备名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aeClientip", value = "设备ip", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "state", value = "状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "floorid", value = "楼层位置", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userno", value = "学号/工号", paramType = "query", dataType = "String")
    })
    public Result<PageData<ScAidooreightDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScAidooreightDTO> page = scAidooreightService.page(params);

        return new Result<PageData<ScAidooreightDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<ScAidooreightDTO> get(@PathVariable("id") String id){
        ScAidooreightDTO data = scAidooreightService.get(id);

        return new Result<ScAidooreightDTO>().ok(data);
    }

    @GetMapping("getAll")
    @ApiOperation("获取所有设备信息")
    public Result<List<ScAidooreightDTO>> getAll(){
        List<ScAidooreightDTO> data = scAidooreightService.getAll();

        return new Result<List<ScAidooreightDTO>>().ok(data);
    }

    @GetMapping("getAllByType/{type}")
    @ApiOperation("获取所有设备信息")
    public Result<List<ScAidooreightDTO>> getAllByType(@PathVariable("type") String type){
        List<ScAidooreightDTO> data = scAidooreightService.getAllByType(type);
        return new Result<List<ScAidooreightDTO>>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("hasAuthority('system:scaidooreight:save')")
    public Result save(@RequestBody ScAidooreightDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scAidooreightService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("hasAuthority('system:scaidooreight:update')")
    public Result update(@RequestBody ScAidooreightDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scAidooreightService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    @PreAuthorize("hasAuthority('system:scaidooreight:delete')")
    public Result delete(@PathVariable("id") Long id){
        //效验数据

        scAidooreightService.deleteById(id);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScAidooreightDTO> list = scAidooreightService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScAidooreightExcel.class);
    }

    @PostMapping("importInfoExcel")
    @ApiOperation(value = "导入信息")
    public Result importInfoExcel(@RequestParam("file") MultipartFile file){
        scAidooreightService.importInfoExcel(file);
        return new Result("导入数据成功");
    }
    @GetMapping("allFacetoDc/{id}")
    @ApiOperation("下发人脸到门禁(单设备)")
    @PreAuthorize("hasAuthority('system:scaidooreight:allFaceto')")
    public Result<TransPictureResult> transAllFaceToOneAiDoor(@PathVariable("id") Long id) throws Exception {
        TransPictureResult transPictureResult=scAidooreightService.transAllPictureToDevice(id.toString());
        return new Result<TransPictureResult>().ok(transPictureResult);

    }

    @GetMapping("allFacetoAllDc")
    @ApiOperation("下发人脸到门禁")
    @PreAuthorize("hasAuthority('system:scaidooreight:allFaceto')")
    public Result transAllFaceToAiDoor() throws Exception {
        scAidooreightService.transAllPictureToAllDevice();
        return new Result("下发人脸到门禁成功");
    }


    @GetMapping("openRemoteDoor/{aeid}")
    @ApiOperation(value = "远程开门")
    @PreAuthorize("hasAuthority('system:scaidooreight:openRemoteDoor')")
    public Result openRemoteDoor(@PathVariable("aeid") String aeid){
        scAidooreightService.openRemoteDoor(aeid);
        return new Result();
    }

    @GetMapping("disconnectAllDevice")
    @ApiOperation(value = "断开所有的连接")
    @PreAuthorize("hasAuthority('system:scaidooreight:disconnectAll')")
    public Result disconnectAllDevice(){

        scAidooreightService.disconnectAllDevice();
        return new Result();
    }

    @GetMapping("disconnectToDevice/{aeid}")
    @ApiOperation(value = "断开指定连接设备")
    @PreAuthorize("hasAuthority('system:scaidooreight:disconnect')")
    public Result disconnectToDevice(@PathVariable("aeid") String aeid){
        scAidooreightService.disconnectToDevice(aeid);
        return new Result();
    }

    @GetMapping("rebootDevice/{aeid}")
    @ApiOperation(value = "重启设备端")
    @PreAuthorize("hasAuthority('system:scaidooreight:reboot')")
    public Result rebootDevice(@PathVariable("aeid") String aeid){
        scAidooreightService.rebootDevice(aeid);
        return new Result();
    }

    @GetMapping("cleanDeviceData/{aeid}")
    @ApiOperation(value = "重置设备端")
    @PreAuthorize("hasAuthority('system:scaidooreight:clean')")
    public Result cleanDeviceData(@PathVariable("aeid") String aeid){
        scAidooreightService.cleanDeviceData(aeid);
        return new Result();
    }

    @PostMapping("transfacelisttodoor")
    @ApiOperation(value = "下发指定人脸列表到设备")
    @PreAuthorize("hasAuthority('system:scaidooreight:transfacelisttodoor')")
    public  Result transFaceListToAiDoor(@RequestBody FaceListDataToDoorDTO facedata){
        if(facedata!=null){
            scAidooreightService.transFaceListToAiDoor(facedata);
        }
        return new Result();
    }

    @GetMapping("getdoorsetting/{aeid}")
    @ApiOperation(value = "获取门禁的设置信息")
    @PreAuthorize("hasAuthority('system:scaidooreight:setting')")
    public  Result<JSONObject> getDoorSeeting(@PathVariable("aeid") String aeid){
        JSONObject doorSetting = scAidooreightService.getDoorSetting(aeid);
        return new Result().ok(doorSetting);
    }


    @PostMapping("setdoorsetting")
    @ApiOperation(value = "设置门禁的配置信息")
    @PreAuthorize("hasAuthority('system:scaidooreight:setting')")
    public  Result setDoorSetting(@RequestBody DeviceSetingInfo deviceSetingInfo){
        if(scAidooreightService.setDoorSetting(deviceSetingInfo)){
            return  new Result();
        }else {
            return  new Result().error("设置参错误");
        }
    }


    @PostMapping("batchUpatePsw")
    @ApiOperation(value = "批量修改设备密码")
    @PreAuthorize("hasAuthority('system:scaidooreight:batchUpatePsw')")
    public  Result batchUpatePsw(@RequestBody AidooreightPassword aidooreightPassword){
        scAidooreightService.batchUpatePws(aidooreightPassword);
        return new Result();
    }



    @GetMapping("updateAlldevice")
    @ApiOperation(value = "更新所有设备到指定apk")
    @PreAuthorize("hasAuthority('system:scaidooreight:updatealldevice')")
    public  Result updateAlldevice(){
         int faildcount=scAidooreightService.updateAllDeviceApk();
         if(faildcount>0){
             return  new Result().error("有部分设备更新未成功,失败设备数："+faildcount);
         }else {
             return  new Result("更新成功");
         }
    }

    @PostMapping("updatedevicelist")
    @ApiOperation(value = "更新指定设备列表的apk")
    public Result uploadNewVersionAPKfileOfIds(@RequestBody String[] aeid){
        int faildcount=scAidooreightService.uploadNewVersionAPKfileOfIds(aeid);
        if(faildcount>0){
            return  new Result().error("有部分设备更新未成功,失败设备数："+faildcount);
        }else {
            return  new Result("更新成功");
        }
    }

}
