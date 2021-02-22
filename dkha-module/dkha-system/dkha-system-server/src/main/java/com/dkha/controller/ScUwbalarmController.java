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
import com.dkha.dto.ScUwbalarmDTO;
import com.dkha.excel.ScUwbalarmExcel;
import com.dkha.service.ScUwbalarmService;
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
 * uwb报警内容：工具标签报警，访客禁区报警，保密区域报警
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scuwbalarm")
@Api(tags="uwb报警")
public class ScUwbalarmController {
    @Autowired
    private ScUwbalarmService scUwbalarmService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "ubaAddress", value = "采集地址(模糊匹配)", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String")
    })
//    @PreAuthorize("hasAuthority('system:scuwbalarm:page')")
    public Result<PageData<ScUwbalarmDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScUwbalarmDTO> page = scUwbalarmService.page(params);

        return new Result<PageData<ScUwbalarmDTO>>().ok(page);
    }

//    @GetMapping("{id}")
//    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scuwbalarm:info')")
//    public Result<ScUwbalarmDTO> get(@PathVariable("id") String id){
//        ScUwbalarmDTO data = scUwbalarmService.get(id);
//
//        return new Result<ScUwbalarmDTO>().ok(data);
//    }
//
//    @PostMapping
//    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scuwbalarm:save')")
//    public Result save(@RequestBody ScUwbalarmDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
//
//        scUwbalarmService.save(dto);
//
//        return new Result();
//    }
//
//    @PutMapping
//    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scuwbalarm:update')")
//    public Result update(@RequestBody ScUwbalarmDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
//
//        scUwbalarmService.update(dto);
//
//        return new Result();
//    }
//
//    @DeleteMapping
//    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scuwbalarm:delete')")
//    public Result delete(@RequestBody String[] ids){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
//
//        scUwbalarmService.delete(ids);
//
//        return new Result();
//    }
//
//    @GetMapping("export")
//    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:scuwbalarm:export')")
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<ScUwbalarmDTO> list = scUwbalarmService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, ScUwbalarmExcel.class);
//    }

}
