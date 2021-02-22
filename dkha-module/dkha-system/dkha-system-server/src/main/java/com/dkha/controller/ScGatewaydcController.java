package com.dkha.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
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
import com.dkha.dto.ScGatewaydcDTO;
import com.dkha.dto.ScModbusdevicedcDTO;
import com.dkha.enums.DeviceTypeEnum;
import com.dkha.enums.OperationTypeEnum;
import com.dkha.excel.ScGatewaydcExcel;
import com.dkha.service.ScGatewaydcService;
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
 * 网关302设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scgatewaydc")
@Api(tags="网关302设备信息")
public class ScGatewaydcController {
    @Autowired
    private ScGatewaydcService scGatewaydcService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority('system:scgatewaydc:page')")
    public Result<PageData<ScGatewaydcDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScGatewaydcDTO> page = scGatewaydcService.page(params);

        return new Result<PageData<ScGatewaydcDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scgatewaydc:info')")
    public Result<ScGatewaydcDTO> get(@PathVariable("id") String id){
        ScGatewaydcDTO data = scGatewaydcService.get(id);

        return new Result<ScGatewaydcDTO>().ok(data);
    }
    @GetMapping("info/{id}")
    @ApiOperation(value = "详情")
    public  Result<ScGatewaydcDTO> getInfo(@PathVariable("id") String id){
        ScGatewaydcDTO info = scGatewaydcService.getInfo(id);
        return new Result<ScGatewaydcDTO>().ok(info);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scgatewaydc:save')")
    public Result save(@RequestBody ScGatewaydcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scGatewaydcService.save(dto);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scgatewaydc:update')")
    public Result update(@RequestBody ScGatewaydcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        scGatewaydcService.update(dto);
        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scgatewaydc:delete')")
    public Result delete(@PathVariable(value = "id") String id){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
        scGatewaydcService.delete(id);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:scgatewaydc:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScGatewaydcDTO> list = scGatewaydcService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScGatewaydcExcel.class);
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入302网关设备信息")
    public Result  importExcel(@RequestParam("file") MultipartFile file) throws Exception {

        try {
            scGatewaydcService.importExcel(file);
        } catch (Exception e) {
         return   new Result<>().error(e.getMessage());
        }
        return new Result();
    }
}
