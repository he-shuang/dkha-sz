package com.dkha.controller;

import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.dto.ScFaceverificationDTO;
import com.dkha.dto.ScStudentsCountDTO;
import com.dkha.excel.ScFaceverificationExcel;
import com.dkha.service.ScFaceverificationService;
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
 * 刷脸或卡记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@RestController
@RequestMapping("/scfaceverification")
@Api(tags="8寸刷脸或卡记录")
public class ScFaceverificationController {
    @Autowired
    private ScFaceverificationService scFaceverificationService;

    @Autowired
    private MinioUtil minioUtil;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "aeId", value = "8寸门禁ID", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "recognitionName", value = "记录人员名称(模糊匹配)", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "type" , value = "1 == 教学楼通行记录  2=  宿舍通行记录", paramType = "query", dataType = "int")
    })
    public Result<PageData<ScFaceverificationDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScFaceverificationDTO> page = scFaceverificationService.page(params);

        return new Result<PageData<ScFaceverificationDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<ScFaceverificationDTO> get(@PathVariable("id") String id){
        ScFaceverificationDTO data = scFaceverificationService.get(id);

        return new Result<ScFaceverificationDTO>().ok(data);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScFaceverificationDTO> list = scFaceverificationService.list(params);
        for (ScFaceverificationDTO scVisitorrecordDTO : list) {
            byte[] imgByte = minioUtil.getImgByte(scVisitorrecordDTO.getImageUrl());
            scVisitorrecordDTO.setImageUrlByte(imgByte);
        }
        ExcelUtils.exportExcelToTarget(response, null, list, ScFaceverificationExcel.class);
    }

}
