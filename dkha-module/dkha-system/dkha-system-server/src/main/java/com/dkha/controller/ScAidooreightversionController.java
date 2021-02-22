
package com.dkha.controller;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.dto.doorcontrol.ScAidooreightversionDTO;
import com.dkha.service.ScAidooreightversionService;
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
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-21
 */
@RestController
@RequestMapping("scaidooreightversion")
@Api(tags="8寸门禁版本管理")
public class ScAidooreightversionController {
    @Autowired
    private ScAidooreightversionService scAidooreightversionService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "aevPackname", value = "程序包名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aeMainboard", value = "设备主板类型", paramType = "query", dataType = "String")
    })
    public Result<PageData<ScAidooreightversionDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScAidooreightversionDTO> page = scAidooreightversionService.page(params);

        return new Result<PageData<ScAidooreightversionDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<ScAidooreightversionDTO> get(@PathVariable("id") String id){
        ScAidooreightversionDTO data = scAidooreightversionService.get(id);

        return new Result<ScAidooreightversionDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody ScAidooreightversionDTO dto){

        scAidooreightversionService.save(dto);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody ScAidooreightversionDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        scAidooreightversionService.update(dto);
        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    public Result delete(@PathVariable("id") Long id){
        //效验数据
        scAidooreightversionService.deleteById(id);
        return new Result();
    }
    @PostMapping("uploadapkfile")
    @ApiOperation(value = "上传新的apk文件")
    public Result uploadNewVersionAPKfile(@RequestParam("file") MultipartFile file){
         String uploadpath=   scAidooreightversionService.uploadNewVersionAPKfile(file);
         return new Result().ok(uploadpath);
    }




}