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
import com.dkha.dto.ScGateaccesscontrolDTO;
import com.dkha.excel.ScGateaccesscontrolExcel;
import com.dkha.service.ScGateaccesscontrolService;
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
 * 门禁同行记录
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scgateaccesscontrol")
@Api(tags="门禁同行记录")
public class ScGateaccesscontrolController {
    @Autowired
    private ScGateaccesscontrolService scGateaccesscontrolService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('system:scgateaccesscontrol:page')")
    public Result<PageData<ScGateaccesscontrolDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScGateaccesscontrolDTO> page = scGateaccesscontrolService.page(params);

        return new Result<PageData<ScGateaccesscontrolDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @PreAuthorize("hasAuthority('system:scgateaccesscontrol:info')")
    public Result<ScGateaccesscontrolDTO> get(@PathVariable("id") String id){
        ScGateaccesscontrolDTO data = scGateaccesscontrolService.get(id);

        return new Result<ScGateaccesscontrolDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("hasAuthority('system:scgateaccesscontrol:save')")
    public Result save(@RequestBody ScGateaccesscontrolDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scGateaccesscontrolService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("hasAuthority('system:scgateaccesscontrol:update')")
    public Result update(@RequestBody ScGateaccesscontrolDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scGateaccesscontrolService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("hasAuthority('system:scgateaccesscontrol:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scGateaccesscontrolService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @PreAuthorize("hasAuthority('system:scgateaccesscontrol:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScGateaccesscontrolDTO> list = scGateaccesscontrolService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScGateaccesscontrolExcel.class);
    }

}
