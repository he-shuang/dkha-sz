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
import com.dkha.dto.ScOccupancyhistoryDTO;
import com.dkha.excel.ScOccupancyhistoryExcel;
import com.dkha.service.ScOccupancyhistoryService;
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
 * 某房间的入住历史记录
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scoccupancyhistory")
@Api(tags="某房间的入住历史记录")
public class ScOccupancyhistoryController {
    @Autowired
    private ScOccupancyhistoryService scOccupancyhistoryService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('system:scoccupancyhistory:page')")
    public Result<PageData<ScOccupancyhistoryDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScOccupancyhistoryDTO> page = scOccupancyhistoryService.page(params);

        return new Result<PageData<ScOccupancyhistoryDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @PreAuthorize("hasAuthority('system:scoccupancyhistory:info')")
    public Result<ScOccupancyhistoryDTO> get(@PathVariable("id") String id){
        ScOccupancyhistoryDTO data = scOccupancyhistoryService.get(id);

        return new Result<ScOccupancyhistoryDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("hasAuthority('system:scoccupancyhistory:save')")
    public Result save(@RequestBody ScOccupancyhistoryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scOccupancyhistoryService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("hasAuthority('system:scoccupancyhistory:update')")
    public Result update(@RequestBody ScOccupancyhistoryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scOccupancyhistoryService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("hasAuthority('system:scoccupancyhistory:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scOccupancyhistoryService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @PreAuthorize("hasAuthority('system:scoccupancyhistory:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScOccupancyhistoryDTO> list = scOccupancyhistoryService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScOccupancyhistoryExcel.class);
    }

}
