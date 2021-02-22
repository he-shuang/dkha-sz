package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.ScAidooreightDailyDTO;
import com.dkha.excel.ScAidooreightDailyExcel;
import com.dkha.service.ScAidooreightDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 8英寸智能门禁设备每日采集数量
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@RestController
@RequestMapping("/scaidooreightdaily")
@Api(tags="8英寸智能门禁设备每日采集数量")
public class ScAidooreightDailyController {
    @Autowired
    private ScAidooreightDailyService scAidooreightDailyService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int")
    })
    public Result<PageData<ScAidooreightDailyDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScAidooreightDailyDTO> page = scAidooreightDailyService.page(params);

        return new Result<PageData<ScAidooreightDailyDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<ScAidooreightDailyDTO> get(@PathVariable("id") String id){
        ScAidooreightDailyDTO data = scAidooreightDailyService.get(id);

        return new Result<ScAidooreightDailyDTO>().ok(data);
    }


    @GetMapping("export")
    @ApiOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScAidooreightDailyDTO> list = scAidooreightDailyService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScAidooreightDailyExcel.class);
    }

}