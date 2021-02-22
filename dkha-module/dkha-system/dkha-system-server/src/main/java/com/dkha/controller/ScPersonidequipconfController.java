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
import com.dkha.dto.ScPersonidequipDTO;
import com.dkha.dto.ScPersonidequipconfDTO;
import com.dkha.excel.ScPersonidequipconfExcel;
import com.dkha.service.ScPersonidequipconfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 人证配置信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@RestController
@RequestMapping("scpersonidequipconf")
@Api(tags="人证配置信息")
public class ScPersonidequipconfController {
    @Autowired
    private ScPersonidequipconfService scPersonidequipconfService;


    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('demo:scpersonidequipconf:info')")
    public Result<ScPersonidequipconfDTO> get(@PathVariable("id") String id){
        ScPersonidequipconfDTO data = scPersonidequipconfService.get(id);

        return new Result<ScPersonidequipconfDTO>().ok(data);
    }

    @GetMapping("getByEquipsn/{equipsn}")
    @ApiOperation("设备编号查询信息")
//    @PreAuthorize("hasAuthority('demo:scpersonidequip:info')")
    public Result<ScPersonidequipconfDTO> getByEquipsn(@PathVariable("equipsn") String equipsn){
        AssertUtils.isNull("equipsn",equipsn);
        ScPersonidequipconfDTO data = scPersonidequipconfService.getByEquipsn(equipsn);
        return new Result<ScPersonidequipconfDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('demo:scpersonidequipconf:save')")
    public Result save(@RequestBody ScPersonidequipconfDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scPersonidequipconfService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('demo:scpersonidequipconf:update')")
    public Result update(@RequestBody ScPersonidequipconfDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scPersonidequipconfService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('demo:scpersonidequipconf:delete')")
    public Result delete(@PathVariable("id") Long id){

        scPersonidequipconfService.delete(id);

        return new Result();
    }
    /**
     * 导入人证设备信息
     * @param file
     * @return
     */
    @PostMapping("/importExcel")
    @ApiOperation(value = "导入人证设备信息")
    public Result importExcel (@RequestParam("file") MultipartFile file) throws Exception {
        try {
            scPersonidequipconfService.importExcel(file);
        } catch (Exception e) {
            return new Result().error(e.getMessage());
        }
        return new Result();
    }

}
