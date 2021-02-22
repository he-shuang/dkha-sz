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
import com.dkha.dto.ScTransformalarmDTO;
import com.dkha.excel.ScTransformalarmExcel;
import com.dkha.service.ScTransformalarmService;
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
 * 电流互感器房间电流信息报警
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("sctransformalarm")
@Api(tags="电流报警")
public class ScTransformalarmController {
    @Autowired
    private ScTransformalarmService scTransformalarmService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String")
    })
//    @PreAuthorize("hasAuthority('system:sctransformalarm:page')")
    public Result<PageData<ScTransformalarmDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScTransformalarmDTO> page = scTransformalarmService.page(params);

        return new Result<PageData<ScTransformalarmDTO>>().ok(page);
    }

//    @GetMapping("{id}")
//    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:sctransformalarm:info')")
//    public Result<ScTransformalarmDTO> get(@PathVariable("id") String id){
//        ScTransformalarmDTO data = scTransformalarmService.get(id);
//
//        return new Result<ScTransformalarmDTO>().ok(data);
//    }
//
//    @PostMapping
//    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:sctransformalarm:save')")
//    public Result save(@RequestBody ScTransformalarmDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
//
//        scTransformalarmService.save(dto);
//
//        return new Result();
//    }
//
//    @PutMapping
//    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:sctransformalarm:update')")
//    public Result update(@RequestBody ScTransformalarmDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
//
//        scTransformalarmService.update(dto);
//
//        return new Result();
//    }
//
//    @DeleteMapping
//    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:sctransformalarm:delete')")
//    public Result delete(@RequestBody String[] ids){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
//
//        scTransformalarmService.delete(ids);
//
//        return new Result();
//    }
//
//    @GetMapping("export")
//    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:sctransformalarm:export')")
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<ScTransformalarmDTO> list = scTransformalarmService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, ScTransformalarmExcel.class);
//    }

}
