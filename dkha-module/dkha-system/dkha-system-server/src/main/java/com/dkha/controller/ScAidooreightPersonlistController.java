package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.dto.ScAidooreightPersonlistDTO;
import com.dkha.excel.ScAidooreightPersonlistExcel;
import com.dkha.service.ScAidooreightPersonlistService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 8英寸智能门禁设备具体的人脸信息
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-16
 */
@RestController
@RequestMapping("/scaidooreightpersonlist")
@Api(tags="8英寸智能门禁设备具体的人脸信息")
public class ScAidooreightPersonlistController {
    @Autowired
    private ScAidooreightPersonlistService scAidooreightPersonlistService;

    @DeleteMapping("{userno}")
    @ApiOperation("一键删除用户图像")
    public Result delUserAll(@PathVariable("userno") String userNo){

        if (StringUtils.isEmpty(userNo)){
            return new Result();
        }

        scAidooreightPersonlistService.delAllByUser(userNo);
        return new Result();
    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "aeId", value = "设备ID", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "username", value = "姓名", paramType = "query", dataType="String")
    })
    public Result<PageData<ScAidooreightPersonlistDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScAidooreightPersonlistDTO> page = scAidooreightPersonlistService.page(params);

        return new Result<PageData<ScAidooreightPersonlistDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<ScAidooreightPersonlistDTO> get(@PathVariable("id") String id){
        ScAidooreightPersonlistDTO data = scAidooreightPersonlistService.get(id);

        return new Result<ScAidooreightPersonlistDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody ScAidooreightPersonlistDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scAidooreightPersonlistService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody ScAidooreightPersonlistDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scAidooreightPersonlistService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("hasAuthority('system:scaidooreightpersonlist:delete')")
    public Result delete(@RequestBody String[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scAidooreightPersonlistService.delete(ids);
        return new Result();
    }
    @DeleteMapping("batchDelete")
    @ApiOperation("批量删除")
    @PreAuthorize("hasAuthority('system:scaidooreightpersonlist:deleteBatch')")
    public Result batchDelete(@ApiParam(
            required = true,value = "{" +
            "\"userids\":[\"1\"]," +
            "\"aeids\":[\"2\"]" +
            "}"
    ) @RequestBody Map<String, String[]> params){
        scAidooreightPersonlistService.batchDelete(params);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScAidooreightPersonlistDTO> list = scAidooreightPersonlistService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScAidooreightPersonlistExcel.class);
    }

}