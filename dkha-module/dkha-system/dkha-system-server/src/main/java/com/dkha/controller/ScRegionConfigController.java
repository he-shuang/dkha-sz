package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScRegionConfigDTO;
import com.dkha.dto.UwbRegionDTO;
import com.dkha.service.ScRegionConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 区域配置/uwb围栏关联
 *
 * @author Mark
 * @since v1.0.0 2020-09-01
 */
@RestController
@RequestMapping("/scregionconfig")
@Api(tags="区域配置-uwb围栏关联")
public class ScRegionConfigController {
    @Autowired
    private ScRegionConfigService scRegionConfigService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @PreAuthorize("hasAuthority(':scregionconfig:page')")
    public Result<PageData<ScRegionConfigDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScRegionConfigDTO> page = scRegionConfigService.page(params);

        return new Result<PageData<ScRegionConfigDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority(':scregionconfig:info')")
    public Result<ScRegionConfigDTO> get(@PathVariable("id") String id){
        ScRegionConfigDTO data = scRegionConfigService.get(id);

        return new Result<ScRegionConfigDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority(':scregionconfig:save')")
    public Result save(@RequestBody ScRegionConfigDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scRegionConfigService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority(':scregionconfig:update')")
    public Result update(@RequestBody ScRegionConfigDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scRegionConfigService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority(':scregionconfig:delete')")
    public Result delete(@PathVariable("id") Long id){

        scRegionConfigService.deleteById(id);

        return new Result();
    }

    @GetMapping("getUwbRegionList")
    @ApiOperation("获取UWB区域信息")
//    @PreAuthorize("hasAuthority(':scregionconfig:info')")
    public Result<List<UwbRegionDTO>> getUwbRegionList(){
        List<UwbRegionDTO> data = scRegionConfigService.getUwbRegionList();
        return new Result<List<UwbRegionDTO>>().ok(data);
    }

}
