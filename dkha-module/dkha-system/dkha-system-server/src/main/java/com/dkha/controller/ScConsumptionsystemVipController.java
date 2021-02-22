package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScConsumptionsystemVipDTO;
import com.dkha.service.ScConsumptionsystemVipService;
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
 * 消费系统会员信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@RestController
@RequestMapping("/scconsumptionsystemvip")
@Api(tags="消费系统会员信息")
public class ScConsumptionsystemVipController {
    @Autowired
    private ScConsumptionsystemVipService scConsumptionsystemVipService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "name", value = "会员名称", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "cardId", value = "会员卡号", paramType = "query", dataType="String")
    })
    //@PreAuthorize("hasAuthority(':scconsumptionsystemvip:page')")
    public Result<PageData<ScConsumptionsystemVipDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScConsumptionsystemVipDTO> page = scConsumptionsystemVipService.page(params);

        return new Result<PageData<ScConsumptionsystemVipDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemvip:info')")
    public Result<ScConsumptionsystemVipDTO> get(@PathVariable("id") String id){
        ScConsumptionsystemVipDTO data = scConsumptionsystemVipService.get(id);

        return new Result<ScConsumptionsystemVipDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemvip:save')")
    public Result save(@RequestBody ScConsumptionsystemVipDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scConsumptionsystemVipService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemvip:update')")
    public Result update(@RequestBody ScConsumptionsystemVipDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scConsumptionsystemVipService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemvip:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scConsumptionsystemVipService.delete(ids);

        return new Result();
    }



}
