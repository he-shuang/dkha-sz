package com.dkha.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.DeviceMassge;
import com.dkha.dto.ScModbusdevicedcDTO;
import com.dkha.dto.ScTransformerdcDTO;
import com.dkha.enums.DeviceTypeEnum;
import com.dkha.enums.OperationTypeEnum;
import com.dkha.excel.ScModbusdevicedcExcel;
import com.dkha.service.ScModbusdevicedcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scmodbusdevicedc")
@Api(tags="485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备")
public class ScModbusdevicedcController  {
    @Autowired
    private ScModbusdevicedcService scModbusdevicedcService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "mbdDevicename", value = "设备名称", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "mbdDevicetype", value = "设备类型", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "mdbStatus", value = "设备状态", paramType = "query", dataType="String"),
    })
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:page')")
    public Result<PageData<ScModbusdevicedcDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScModbusdevicedcDTO> page = scModbusdevicedcService.page(params);

        return new Result<PageData<ScModbusdevicedcDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:info')")
    public Result<ScModbusdevicedcDTO> get(@PathVariable("id") String id){
        ScModbusdevicedcDTO data = scModbusdevicedcService.get(id);

        return new Result<ScModbusdevicedcDTO>().ok(data);
    }

    @GetMapping("info/{id}")
    @ApiOperation("详情")
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:info')")
    public Result<ScModbusdevicedcDTO> info(@PathVariable("id") String id){
        ScModbusdevicedcDTO data = scModbusdevicedcService.info(id);

        return new Result<ScModbusdevicedcDTO>().ok(data);
    }

    @GetMapping("type/{type}")
    @ApiOperation("根据设备类型获取信息")
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:info')")
    public Result<List<ScModbusdevicedcDTO>> getTypeMessage(@PathVariable("type") String type){
        List<ScModbusdevicedcDTO> typeMessage = scModbusdevicedcService.getTypeMessage(type);

        return new Result<List<ScModbusdevicedcDTO>>().ok(typeMessage);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:save')")
    public Result save(@RequestBody ScModbusdevicedcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        scModbusdevicedcService.save(dto);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:update')")
    public Result update(@RequestBody ScModbusdevicedcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        scModbusdevicedcService.update(dto);
        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:delete')")
    public Result delete(@PathVariable(value = "id") String id){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
        scModbusdevicedcService.delete(id);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:scmodbusdevicedc:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScModbusdevicedcDTO> list = scModbusdevicedcService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScModbusdevicedcExcel.class);
    }


    /**
     * 导入PIR设备信息
     * @param file
     * @return
     */
    @PostMapping("/importPIRExcel")
    @ApiOperation("导入PIR设备信息")
    public Result importPIRExcel (@RequestParam("file")MultipartFile file) throws Exception {
        try {
            scModbusdevicedcService.importPIRExcel(file);
        } catch (Exception e) {
            return new Result().error(e.getMessage());
        }
        return new Result();
    }

    /**
     * 导入PM2.5设备信息
     * @param file
     * @return
     */
    @PostMapping("/importPMExcel")
    @ApiOperation("导入PM2.5设备信息")
    public Result importPMExcel (@RequestParam("file")MultipartFile file) throws Exception {
        try {
            scModbusdevicedcService.importPMExcel(file);
        } catch (Exception e) {
            return new Result().error(e.getMessage());
        }
        return new Result();
    }

    /**
     * 智能控灯设备
     * @param file
     * @return
     */
    @PostMapping("/importSLExcel")
    @ApiOperation("导入智能控灯设备信息")
    public Result importSLExcel (@RequestParam("file")MultipartFile file) throws Exception {
        try {
            scModbusdevicedcService.importSLExcel(file);
        } catch (Exception e) {
            return new Result().error(e.getMessage());
        }
        return new Result();
    }
}
