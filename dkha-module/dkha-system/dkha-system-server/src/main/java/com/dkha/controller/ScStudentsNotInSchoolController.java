package com.dkha.controller;


import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScStudentsNotInSchoolDTO;
import com.dkha.excel.ScStudentsNotInSchoolExcel;
import com.dkha.excel.ScTransformerroomExcel;
import com.dkha.service.ScStudentsNotInSchoolService;
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
 * <p>
 * 学生未归寝每日统计 前端控制器
 * </p>
 *
 * @author Spring
 * @since 2020-12-07
 */
@RestController
@RequestMapping("scstudentsnotinschool")
@Api(tags="疑似夜间不在园区学生管理")
public class ScStudentsNotInSchoolController {

    @Autowired
    private ScStudentsNotInSchoolService scStudentsNotInSchoolService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "scStuname", value = "姓名", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "scNo", value = "学号", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "drNum", value = "宿舍号", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "scSchool", value = "学院", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "date", value = "日期", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority('system:sctransformerroom:page')")
    public Result<PageData<ScStudentsNotInSchoolDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScStudentsNotInSchoolDTO> page = scStudentsNotInSchoolService.page(params);

        return new Result<PageData<ScStudentsNotInSchoolDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:info')")
    public Result<ScStudentsNotInSchoolDTO> get(@PathVariable("id") String id){
        ScStudentsNotInSchoolDTO data = scStudentsNotInSchoolService.get(id);

        return new Result<ScStudentsNotInSchoolDTO>().ok(data);
    }

    @GetMapping("test")
    @ApiOperation("测试")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:info')")
    public Result<ScStudentsNotInSchoolDTO> getTest(){
        ScStudentsNotInSchoolDTO data = scStudentsNotInSchoolService.getTest();


        return new Result<ScStudentsNotInSchoolDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:save')")
    public Result save(@RequestBody ScStudentsNotInSchoolDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scStudentsNotInSchoolService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:update')")
    public Result update(@RequestBody ScStudentsNotInSchoolDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scStudentsNotInSchoolService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scStudentsNotInSchoolService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:sctransformerroom:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScStudentsNotInSchoolDTO> list = scStudentsNotInSchoolService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScStudentsNotInSchoolExcel.class);
    }
}
