package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScPmalarmDTO;
import com.dkha.excel.ScPmalarmExcel;
import com.dkha.service.ScPmalarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * PM2.5设备报警信息
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scpmalarm")
@Api(tags="环境传感器报警")
public class ScPmalarmController {
    @Autowired
    private ScPmalarmService scPmalarmService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "pmaAddress", value = "采集地址(模糊匹配)", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "devicename", value = "设备名称", paramType = "query", dataType = "String")
    })
//    @PreAuthorize("hasAuthority('system:scpmalarm:page')")
    public Result<PageData<ScPmalarmDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScPmalarmDTO> page = scPmalarmService.page(params);

        return new Result<PageData<ScPmalarmDTO>>().ok(page);
    }

//    @GetMapping("{id}")
//    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scpmalarm:info')")
//    public Result<ScPmalarmDTO> get(@PathVariable("id") String id){
//        ScPmalarmDTO data = scPmalarmService.get(id);
//
//        return new Result<ScPmalarmDTO>().ok(data);
//    }

//    @PostMapping
//    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scpmalarm:save')")
//    public Result save(@RequestBody ScPmalarmDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
//
//        scPmalarmService.save(dto);
//
//        return new Result();
//    }

//    @PutMapping
//    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scpmalarm:update')")
//    public Result update(@RequestBody ScPmalarmDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
//
//        scPmalarmService.update(dto);
//
//        return new Result();
//    }

//    @DeleteMapping
//    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scpmalarm:delete')")
//    public Result delete(@RequestBody String[] ids){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
//
//        scPmalarmService.delete(ids);
//
//        return new Result();
//    }

//    @GetMapping("export")
//    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:scpmalarm:export')")
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<ScPmalarmDTO> list = scPmalarmService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, ScPmalarmExcel.class);
//    }

}
