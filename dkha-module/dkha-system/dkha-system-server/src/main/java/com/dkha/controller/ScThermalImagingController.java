package com.dkha.controller;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.dto.ScThermalImagingDTO;
import com.dkha.service.ScThermalImagingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 热成像设备表
 *
 * @author Mark
 * @since v1.0.0 2020-11-04
 */
@RestController
@RequestMapping("/scthermalimaging")
@Api(tags="热成像设备表")
public class ScThermalImagingController {
    @Autowired
    private ScThermalImagingService scThermalImagingService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "tfDevicename", value = "管道名称", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = "tfStatus", value = "设备状态,可选值（-1 离线 0 正常 1 停用）", paramType = "query", dataType="int")
    })
   // @PreAuthorize("hasAuthority(':scthermalimaging:page')")
    public Result<PageData<ScThermalImagingDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScThermalImagingDTO> page = scThermalImagingService.page(params);

        return new Result<PageData<ScThermalImagingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
   // @PreAuthorize("hasAuthority(':scthermalimaging:info')")
    public Result<ScThermalImagingDTO> get(@PathVariable("id") String id){
        ScThermalImagingDTO data = scThermalImagingService.get(id);

        return new Result<ScThermalImagingDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
   // @PreAuthorize("hasAuthority(':scthermalimaging:save')")
    public Result save(@RequestBody ScThermalImagingDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scThermalImagingService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
   // @PreAuthorize("hasAuthority(':scthermalimaging:update')")
    public Result update(@RequestBody ScThermalImagingDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scThermalImagingService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    //@PreAuthorize("hasAuthority(':scthermalimaging:delete')")
    public Result delete(@PathVariable(value = "id") String id){
        //效验数据
        //AssertUtils.isArrayEmpty(ids, "id");

        scThermalImagingService.delete(id);

        return new Result();
    }

   // @GetMapping("export")
   // @ApiOperation("导出")
   // @PreAuthorize("hasAuthority(':scthermalimaging:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScThermalImagingDTO> list = scThermalImagingService.list(params);

      //  ExcelUtils.exportExcelToTarget(response, null, list, ScThermalImagingExcel.class);
    }

    @GetMapping("/thermalList/{type}")
    @ApiOperation("数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tfDevicename", value = "管道名称", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = "tfIpgateway", value = "IP", paramType = "query", dataType="String")
    })

    public Result thermalList(@PathVariable("type")String type,@ApiIgnore @RequestParam Map<String, Object> params){
        List<Map<String,Object>> map = scThermalImagingService.thermalList(type,params);
        return new Result().ok(map);
    }

}
