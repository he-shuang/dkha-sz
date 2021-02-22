package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.dto.ScAidoorfiveOpenLogDTO;
import com.dkha.service.ScAidoorfiveOpenLogService;
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
 * @author Mark
 * @since v1.0.0 2020-10-29
 */
@RestController
@RequestMapping("/scaidoorfiveopenlog")
@Api(tags = "五寸门禁系统通行记录")
public class ScAidoorfiveOpenLogController {
    @Autowired
    private ScAidoorfiveOpenLogService scAidoorfiveOpenLogService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名(模糊匹配)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceName", value = "设备名称(模糊匹配)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "serialNumber", value = "设备序列号(模糊匹配)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
//    @PreAuthorize("hasAuthority(':scaidoorfiveopenlog:page')")
    public Result<PageData<ScAidoorfiveOpenLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<ScAidoorfiveOpenLogDTO> page = scAidoorfiveOpenLogService.page(params);

        return new Result<PageData<ScAidoorfiveOpenLogDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority(':scaidoorfiveopenlog:info')")
    public Result<ScAidoorfiveOpenLogDTO> get(@PathVariable("id") String id) {
        ScAidoorfiveOpenLogDTO data = scAidoorfiveOpenLogService.get(id);

        return new Result<ScAidoorfiveOpenLogDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority(':scaidoorfiveopenlog:save')")
    public Result save(@RequestBody ScAidoorfiveOpenLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scAidoorfiveOpenLogService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority(':scaidoorfiveopenlog:update')")
    public Result update(@RequestBody ScAidoorfiveOpenLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scAidoorfiveOpenLogService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority(':scaidoorfiveopenlog:delete')")
    public Result delete(@RequestBody String[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scAidoorfiveOpenLogService.delete(ids);

        return new Result();
    }

}
