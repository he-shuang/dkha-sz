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
import com.dkha.dto.ScTransformerroomDTO;
import com.dkha.excel.ScTransformerroomExcel;
import com.dkha.service.ScTransformerroomService;
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
 * 互感器宿舍关联关系
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("sctransformerroom")
@Api(tags="互感器宿舍关联关系")
public class ScTransformerroomController {
    @Autowired
    private ScTransformerroomService scTransformerroomService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority('system:sctransformerroom:page')")
    public Result<PageData<ScTransformerroomDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScTransformerroomDTO> page = scTransformerroomService.page(params);

        return new Result<PageData<ScTransformerroomDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:info')")
    public Result<ScTransformerroomDTO> get(@PathVariable("id") String id){
        ScTransformerroomDTO data = scTransformerroomService.get(id);

        return new Result<ScTransformerroomDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:save')")
    public Result save(@RequestBody ScTransformerroomDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scTransformerroomService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:update')")
    public Result update(@RequestBody ScTransformerroomDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scTransformerroomService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scTransformerroomService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScTransformerroomDTO> list = scTransformerroomService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScTransformerroomExcel.class);
    }

}
