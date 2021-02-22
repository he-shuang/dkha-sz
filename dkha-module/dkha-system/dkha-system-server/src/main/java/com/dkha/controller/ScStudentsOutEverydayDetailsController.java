package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScStudentsOutEverydayDetailsDTO;
import com.dkha.service.ScStudentsOutEverydayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 每日学生未归详情
 *
 * @author Mark 
 * @since v1.0.0 2020-10-15
 */
@RestController
@RequestMapping("/scstudentsouteverydaydetails")
@Api(tags="每日学生未归详情")
public class ScStudentsOutEverydayDetailsController {
    @Autowired
    private ScStudentsOutEverydayDetailsService scStudentsOutEverydayDetailsService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "date", value = "时间", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority(':scstudentsouteverydaydetails:page')")
    public Result<PageData<ScStudentsOutEverydayDetailsDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScStudentsOutEverydayDetailsDTO> page = scStudentsOutEverydayDetailsService.page(params);

        return new Result<PageData<ScStudentsOutEverydayDetailsDTO>>().ok(page);
    }



}
