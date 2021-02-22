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
import com.dkha.dto.AlarmMassge;
import com.dkha.dto.DeviceMassge;
import com.dkha.dto.ScTransformerdcDTO;
import com.dkha.enums.DeviceTypeEnum;
import com.dkha.enums.OperationTypeEnum;
import com.dkha.excel.ScTransformerdcExcel;
import com.dkha.service.ScTransformerdcService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 互感器设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("sctransformerdc")
@Api(tags="互感器设备信息")
public class ScTransformerdcController {
    @Autowired
    private ScTransformerdcService scTransformerdcService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority('system:sctransformerdc:page')")
    public Result<PageData<ScTransformerdcDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScTransformerdcDTO> page = scTransformerdcService.page(params);

        return new Result<PageData<ScTransformerdcDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:sctransformerdc:info')")
    public Result<ScTransformerdcDTO> get(@PathVariable("id") String id){
        ScTransformerdcDTO data = scTransformerdcService.get(id);
Map  map = new HashMap();
        return new Result<ScTransformerdcDTO>().ok(data);
    }

    @GetMapping("info/{id}")
    @ApiOperation("详情")
//    @PreAuthorize("hasAuthority('system:sctransformerdc:info')")
    public Result<ScTransformerdcDTO> info(@PathVariable("id") String id){
        ScTransformerdcDTO data = scTransformerdcService.info(id);

        return new Result<ScTransformerdcDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:sctransformerdc:save')")
    public Result save(@RequestBody ScTransformerdcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scTransformerdcService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:sctransformerdc:update')")
    public Result update(@RequestBody ScTransformerdcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scTransformerdcService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:sctransformerdc:delete')")
    public Result delete(@PathVariable(value = "id") String id){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");

        scTransformerdcService.delete(id);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:sctransformerdc:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScTransformerdcDTO> list = scTransformerdcService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScTransformerdcExcel.class);
    }

    @PostMapping("importInfoExcel")
    @ApiOperation(value = "导入互感器设备信息")
    public Result importInfoExcel(@RequestParam("file") MultipartFile file){
        try {
            scTransformerdcService.importInfoExcel(file);
        } catch (Exception e) {
            return new Result().error(e.getMessage());
        }
        return new Result("导入数据成功");
    }

}
