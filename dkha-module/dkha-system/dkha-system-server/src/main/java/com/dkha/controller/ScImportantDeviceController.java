package com.dkha.controller;

import com.dkha.commons.log.annotation.LogOperation;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScImportantDeviceDTO;
import com.dkha.service.ScImportantDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;


/**
 * 重要设备信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@RestController
@RequestMapping("scimportantdevice")
@Api(tags="重要设备信息表")
public class ScImportantDeviceController {
    @Autowired
    private ScImportantDeviceService scImportantDeviceService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    //@RequiresPermissions("demo:scimportantdevice:page")
    public Result<PageData<ScImportantDeviceDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScImportantDeviceDTO> page = scImportantDeviceService.page(params);

        return new Result<PageData<ScImportantDeviceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    //@RequiresPermissions("demo:scimportantdevice:info")
    public Result<ScImportantDeviceDTO> get(@PathVariable("id") Long id){
        ScImportantDeviceDTO data = scImportantDeviceService.get(id);

        return new Result<ScImportantDeviceDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    //@RequiresPermissions("demo:scimportantdevice:save")
    public Result save(@RequestBody ScImportantDeviceDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scImportantDeviceService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    //@RequiresPermissions("demo:scimportantdevice:update")
    public Result update(@RequestBody ScImportantDeviceDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scImportantDeviceService.update(dto);

        return new Result();
    }

//    @DeleteMapping
//    @ApiOperation("删除")
//    @LogOperation("删除")
//    //@RequiresPermissions("demo:scimportantdevice:delete")
//    public Result delete(@RequestBody Long[] ids){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
//
//        scImportantDeviceService.delete(ids);
//
//        return new Result();
//    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    //@RequiresPermissions("demo:scimportantdevice:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScImportantDeviceDTO> list = scImportantDeviceService.list(params);

        //ExcelUtils.exportExcelToTarget(response, null, list, ScImportantDeviceExcel.class);
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除ByID")
    @LogOperation("删除ByID")
    //@RequiresPermissions("demo:scimportantdevice:delete")
    public Result deleteByID(@PathVariable(value = "id")  String id){

        scImportantDeviceService.deleteById(id);

        return new Result();
    }


    /**
     * 重要设备
     * @param file
     * @return
     */
    @PostMapping("/importExcel")
    @ApiOperation("导入重要设备信息")
    public Result importExcel (@RequestParam("file") MultipartFile file) throws Exception {
        try {
            scImportantDeviceService.importExcel(file);
        } catch (Exception e) {
            return new Result().error(e.getMessage());
        }

        return new Result();
    }


}
