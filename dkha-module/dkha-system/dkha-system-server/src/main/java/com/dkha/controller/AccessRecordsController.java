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
import com.dkha.dto.AccessRecordsDTO;
import com.dkha.service.AccessRecordsService;
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
 * 出入记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-30
 */
@RestController
@RequestMapping("/accessrecords")
@Api(tags="出入记录")
public class AccessRecordsController {
    @Autowired
    private AccessRecordsService accessRecordsService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "name", value = "姓名", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "studentNum", value = "学号", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority(':accessrecords:page')")
    public Result<PageData<AccessRecordsDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<AccessRecordsDTO> page = accessRecordsService.page(params);

        return new Result<PageData<AccessRecordsDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority(':accessrecords:info')")
    public Result<AccessRecordsDTO> get(@PathVariable("id") String id){
        AccessRecordsDTO data = accessRecordsService.get(id);

        return new Result<AccessRecordsDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority(':accessrecords:save')")
    public Result save(@RequestBody AccessRecordsDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        accessRecordsService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority(':accessrecords:update')")
    public Result update(@RequestBody AccessRecordsDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        accessRecordsService.update(dto);

        return new Result();
    }





}
