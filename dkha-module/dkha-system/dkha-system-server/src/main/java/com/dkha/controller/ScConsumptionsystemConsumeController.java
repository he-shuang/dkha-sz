package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScConsumptionsystemConsumeDTO;
import com.dkha.service.ScConsumptionsystemConsumeService;
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
 * 消费系统的消费记录
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@RestController
@RequestMapping("/scconsumptionsystemconsume")
@Api(tags="消费系统的消费记录")
public class ScConsumptionsystemConsumeController {
    @Autowired
    private ScConsumptionsystemConsumeService scConsumptionsystemConsumeService;

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
    //@PreAuthorize("hasAuthority(':scconsumptionsystemconsume:page')")
    public Result<PageData<ScConsumptionsystemConsumeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScConsumptionsystemConsumeDTO> page = scConsumptionsystemConsumeService.page(params);

        return new Result<PageData<ScConsumptionsystemConsumeDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemconsume:info')")
    public Result<ScConsumptionsystemConsumeDTO> get(@PathVariable("id") String id){
        ScConsumptionsystemConsumeDTO data = scConsumptionsystemConsumeService.get(id);

        return new Result<ScConsumptionsystemConsumeDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemconsume:save')")
    public Result save(@RequestBody ScConsumptionsystemConsumeDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scConsumptionsystemConsumeService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemconsume:update')")
    public Result update(@RequestBody ScConsumptionsystemConsumeDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scConsumptionsystemConsumeService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    //@PreAuthorize("hasAuthority(':scconsumptionsystemconsume:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scConsumptionsystemConsumeService.delete(ids);

        return new Result();
    }


}
