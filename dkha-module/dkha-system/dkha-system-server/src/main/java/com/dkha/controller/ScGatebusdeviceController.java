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
import com.dkha.dto.ScGatebusdeviceDTO;
import com.dkha.excel.ScGatebusdeviceExcel;
import com.dkha.service.ScGatebusdeviceService;
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
 * 485通讯总线下挂载的设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scgatebusdevice")
@Api(tags="485通讯总线下挂载的设备信息")
public class ScGatebusdeviceController {
    @Autowired
    private ScGatebusdeviceService scGatebusdeviceService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority('system:scgatebusdevice:page')")
    public Result<PageData<ScGatebusdeviceDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScGatebusdeviceDTO> page = scGatebusdeviceService.page(params);

        return new Result<PageData<ScGatebusdeviceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scgatebusdevice:info')")
    public Result<ScGatebusdeviceDTO> get(@PathVariable("id") String id){
        ScGatebusdeviceDTO data = scGatebusdeviceService.get(id);

        return new Result<ScGatebusdeviceDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scgatebusdevice:save')")
    public Result save(@RequestBody ScGatebusdeviceDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scGatebusdeviceService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scgatebusdevice:update')")
    public Result update(@RequestBody ScGatebusdeviceDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scGatebusdeviceService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scgatebusdevice:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scGatebusdeviceService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:scgatebusdevice:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScGatebusdeviceDTO> list = scGatebusdeviceService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScGatebusdeviceExcel.class);
    }

}
