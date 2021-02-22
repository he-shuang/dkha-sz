

package com.dkha.controller;

import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.log.annotation.LogOperation;
import com.dkha.commons.tools.exception.ErrorCode;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.dto.SysMenuDTO;
import com.dkha.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 * @since 1.0.0
 */
@RestController
@RequestMapping("menu")
@Api(tags="菜单管理")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("nav")
    @ApiOperation("导航")
    public Result<List<SysMenuDTO>> nav(){
        List<SysMenuDTO> list = sysMenuService.getUserMenuNavList(SecurityUser.getUser());


        return new Result<List<SysMenuDTO>>().ok(list);
    }

    @GetMapping("permissions")
    @ApiOperation("权限标识")
    public Result<Set<String>> permissions(){
        Set<String> set = sysMenuService.getUserPermissions(SecurityUser.getUser());

        return new Result<Set<String>>().ok(set);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @ApiImplicitParam(name = "type", value = "菜单类型 0：菜单 1：按钮  null：全部", paramType = "query", dataType="int")
    public Result<List<SysMenuDTO>> list(Integer type){
        List<SysMenuDTO> list = sysMenuService.getMenuList(type);

        return new Result<List<SysMenuDTO>>().ok(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @PreAuthorize("hasAuthority('sys:menu:info')")
    public Result<SysMenuDTO> get(@PathVariable("id") Long id){
        SysMenuDTO data = sysMenuService.get(id);

        return new Result<SysMenuDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("Save Menu")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result save(@RequestBody SysMenuDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysMenuService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("Update Menu")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result update(@RequestBody SysMenuDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysMenuService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    @LogOperation("Delete Menu")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result delete(@PathVariable("id") Long id){
        //效验数据
        AssertUtils.isNull(id, "id");

        //判断是否有子菜单或按钮
        List<SysMenuDTO> list = sysMenuService.getListPid(id);
        if(list.size() > 0){
            return new Result().error(ErrorCode.SUB_MENU_EXIST);
        }

        sysMenuService.delete(id);

        return new Result();
    }

    @GetMapping("select")
    @ApiOperation("角色菜单权限")
    @PreAuthorize("hasAuthority('sys:menu:select')")
    public Result<List<SysMenuDTO>> select(){
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(SecurityUser.getUser(), null);

        return new Result<List<SysMenuDTO>>().ok(list);
    }

}
