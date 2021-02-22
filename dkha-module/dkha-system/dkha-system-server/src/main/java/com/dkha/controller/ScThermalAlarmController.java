package com.dkha.controller;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.dto.ScThermalAlarmDTO;
import com.dkha.service.ScThermalAlarmService;
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
 * 热成像报警表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
@RestController
@RequestMapping("/scthermalalarm")
@Api(tags="热成像报警表")
public class ScThermalAlarmController {
    @Autowired
    private ScThermalAlarmService scThermalAlarmService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    //@PreAuthorize("hasAuthority(':scthermalalarm:page')")
    public Result<PageData<ScThermalAlarmDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScThermalAlarmDTO> page = scThermalAlarmService.page(params);

        return new Result<PageData<ScThermalAlarmDTO>>().ok(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public Result<PageData<ScThermalAlarmDTO>> getThermalAlarmByPage(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScThermalAlarmDTO> page = scThermalAlarmService.getThermalAlarmByPage(params);
        return new Result<PageData<ScThermalAlarmDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
   // @PreAuthorize("hasAuthority(':scthermalalarm:info')")
    public Result<ScThermalAlarmDTO> get(@PathVariable("id") String id){
        ScThermalAlarmDTO data = scThermalAlarmService.get(id);

        return new Result<ScThermalAlarmDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    //@PreAuthorize("hasAuthority(':scthermalalarm:save')")
    public Result save(@RequestBody ScThermalAlarmDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scThermalAlarmService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
   // @PreAuthorize("hasAuthority(':scthermalalarm:update')")
    public Result update(@RequestBody ScThermalAlarmDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scThermalAlarmService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    //@PreAuthorize("hasAuthority(':scthermalalarm:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scThermalAlarmService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @PreAuthorize("hasAuthority(':scthermalalarm:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScThermalAlarmDTO> list = scThermalAlarmService.list(params);

       // ExcelUtils.exportExcelToTarget(response, null, list, ScThermalAlarmExcel.class);
    }

}
