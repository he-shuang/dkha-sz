

package com.dkha.controller;

import com.dkha.commons.log.annotation.LogOperation;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.SysDeptDTO;
import com.dkha.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 部门管理
 * @since 1.0.0
 */
@RestController
@RequestMapping("dept")
@Api(tags="部门管理")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @GetMapping("list")
    @ApiOperation("列表")
    @PreAuthorize("hasAuthority('sys:dept:list')")
    public Result<List<SysDeptDTO>> list(){
        List<SysDeptDTO> list = sysDeptService.list(new HashMap<>(1));

        return new Result<List<SysDeptDTO>>().ok(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @PreAuthorize("hasAuthority('sys:dept:info')")
    public Result<SysDeptDTO> get(@PathVariable("id") Long id){
        SysDeptDTO data = sysDeptService.get(id);

        return new Result<SysDeptDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("Save Dept")
    @PreAuthorize("hasAuthority('sys:dept:save')")
    public Result save(@RequestBody SysDeptDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysDeptService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("Update Dept")
    @PreAuthorize("hasAuthority('sys:dept:update')")
    public Result update(@RequestBody SysDeptDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysDeptService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    @LogOperation("Delete Dept")
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    public Result delete(@PathVariable("id") Long id){
        //效验数据
        AssertUtils.isNull(id, "id");

        sysDeptService.delete(id);

        return new Result();
    }
}
