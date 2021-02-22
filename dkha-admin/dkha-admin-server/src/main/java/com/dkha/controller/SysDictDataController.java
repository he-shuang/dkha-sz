

package com.dkha.controller;

import com.dkha.commons.log.annotation.LogOperation;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.SysDictDataDTO;
import com.dkha.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 字典数据
 */
@RestController
@RequestMapping("dict/data")
@Api(tags="字典数据")
public class SysDictDataController {
    @Autowired
    private SysDictDataService sysDictDataService;

    @GetMapping("page")
    @ApiOperation("字典数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "dictLabel", value = "字典标签", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "dictValue", value = "字典值", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('sys:dict:page')")
    public Result<PageData<SysDictDataDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        //字典类型
        PageData<SysDictDataDTO> page = sysDictDataService.page(params);

        return new Result<PageData<SysDictDataDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @PreAuthorize("hasAuthority('sys:dict:info')")
    public Result<SysDictDataDTO> get(@PathVariable("id") Long id){
        SysDictDataDTO data = sysDictDataService.get(id);

        return new Result<SysDictDataDTO>().ok(data);
    }

    @GetMapping("getByType")
    @ApiOperation("通过type获取数据")
    public Result<List<SysDictDataDTO> > getByType(String type){
        List<SysDictDataDTO> data = sysDictDataService.getByType(type);
        return new Result<List<SysDictDataDTO> >().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @PreAuthorize("hasAuthority('sys:dict:save')")
    public Result save(@RequestBody SysDictDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysDictDataService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    public Result update(@RequestBody SysDictDataDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysDictDataService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        sysDictDataService.delete(ids);

        return new Result();
    }

}
