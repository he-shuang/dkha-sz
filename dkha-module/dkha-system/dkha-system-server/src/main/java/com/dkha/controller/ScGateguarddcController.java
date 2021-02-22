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
import com.dkha.dto.ScGateguarddcDTO;
import com.dkha.excel.ScGateguarddcExcel;
import com.dkha.service.ScGateguarddcService;
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
 *
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scgateguarddc")
@Api(tags="")
public class ScGateguarddcController {
    @Autowired
    private ScGateguarddcService scGateguarddcService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority('system:scgateguarddc:page')")
    public Result<PageData<ScGateguarddcDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScGateguarddcDTO> page = scGateguarddcService.page(params);

        return new Result<PageData<ScGateguarddcDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scgateguarddc:info')")
    public Result<ScGateguarddcDTO> get(@PathVariable("id") String id){
        ScGateguarddcDTO data = scGateguarddcService.get(id);

        return new Result<ScGateguarddcDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scgateguarddc:save')")
    public Result save(@RequestBody ScGateguarddcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scGateguarddcService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scgateguarddc:update')")
    public Result update(@RequestBody ScGateguarddcDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scGateguarddcService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scgateguarddc:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scGateguarddcService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:scgateguarddc:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScGateguarddcDTO> list = scGateguarddcService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScGateguarddcExcel.class);
    }

}
