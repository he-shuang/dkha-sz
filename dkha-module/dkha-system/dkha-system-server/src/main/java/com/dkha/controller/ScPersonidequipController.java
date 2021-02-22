package com.dkha.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScPersonidequipDTO;
import com.dkha.entity.ScPersonidequipEntity;
import com.dkha.excel.ScPersonidequipExcel;
import com.dkha.service.ScPersonidequipService;
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
 * 人证识别设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@RestController
@RequestMapping("scpersonidequip")
@Api(tags="人证识别设备")
public class ScPersonidequipController {
    @Autowired
    private ScPersonidequipService scPersonidequipService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "pieDevicename", value = "设备名称", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "pieStatus", value = "设备状态", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:page')")
    public Result<PageData<ScPersonidequipDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScPersonidequipDTO> page = scPersonidequipService.page(params);

        return new Result<PageData<ScPersonidequipDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:info')")
    public Result<ScPersonidequipDTO> get(@PathVariable("id") String id){
        ScPersonidequipDTO data = scPersonidequipService.get(id);

        return new Result<ScPersonidequipDTO>().ok(data);
    }


    @GetMapping("getAll")
    @ApiOperation("获取所有设备")
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:info')")
    public Result<List<ScPersonidequipDTO>> getAll(){
        List<ScPersonidequipDTO> data = scPersonidequipService.getAll();

        return new Result<List<ScPersonidequipDTO>>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:save')")
    public Result save(@RequestBody ScPersonidequipDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scPersonidequipService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:update')")
    public Result update(@RequestBody ScPersonidequipDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scPersonidequipService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:delete')")
    public Result delete(@PathVariable(value = "id") String id){
        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");

        scPersonidequipService.delete(id);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScPersonidequipDTO> list = scPersonidequipService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScPersonidequipExcel.class);
    }

}
