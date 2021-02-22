package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScConsumptionsystemRechargeDTO;
import com.dkha.service.ScConsumptionsystemRechargeService;
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
 * 消费系统充值信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@RestController
@RequestMapping("/scconsumptionsystemrecharge")
@Api(tags="消费系统充值信息")
public class ScConsumptionsystemRechargeController {
    @Autowired
    private ScConsumptionsystemRechargeService scConsumptionsystemRechargeService;

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
    //@PreAuthorize("hasAuthority(':scconsumptionsystemrecharge:page')")
    public Result<PageData<ScConsumptionsystemRechargeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScConsumptionsystemRechargeDTO> page = scConsumptionsystemRechargeService.page(params);

        return new Result<PageData<ScConsumptionsystemRechargeDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemrecharge:info')")
    public Result<ScConsumptionsystemRechargeDTO> get(@PathVariable("id") String id){
        ScConsumptionsystemRechargeDTO data = scConsumptionsystemRechargeService.get(id);

        return new Result<ScConsumptionsystemRechargeDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemrecharge:save')")
    public Result save(@RequestBody ScConsumptionsystemRechargeDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scConsumptionsystemRechargeService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemrecharge:update')")
    public Result update(@RequestBody ScConsumptionsystemRechargeDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scConsumptionsystemRechargeService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemrecharge:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scConsumptionsystemRechargeService.delete(ids);

        return new Result();
    }

}
