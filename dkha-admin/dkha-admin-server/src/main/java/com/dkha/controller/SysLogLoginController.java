

package com.dkha.controller;

import com.dkha.commons.log.annotation.LogOperation;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.SysLogLoginDTO;
import com.dkha.excel.SysLogLoginExcel;
import com.dkha.service.SysLogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 登录日志
 * @since 1.0.0
 */
@RestController
@RequestMapping("log/login")
@Api(tags="登录日志")
public class SysLogLoginController {
    @Autowired
    private SysLogLoginService sysLogLoginService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "status", value = "状态  0：失败    1：成功    2：账号已锁定", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "creatorName", value = "用户名", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('sys:log:login')")
    public Result<PageData<SysLogLoginDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SysLogLoginDTO> page = sysLogLoginService.page(params);

        return new Result<PageData<SysLogLoginDTO>>().ok(page);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("Export Log Login")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "status", value = "状态  0：失败    1：成功    2：账号已锁定", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "creatorName", value = "用户名", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('sys:log:login')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysLogLoginDTO> list = sysLogLoginService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, SysLogLoginExcel.class);
    }

}
