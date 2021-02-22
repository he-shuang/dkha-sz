package com.dkha.controller;


import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.DataAnalysisDTO;
import com.dkha.service.DataAnalysisServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/dataAnalysis")
@Api(tags="数据分析")
public class DataAnalysisController {


    @Autowired
    private DataAnalysisServer dataAnalysisServer;

    @GetMapping("notInOrOut")
    @ApiOperation("未进未出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型(2 == 教学楼闸机 , 1 == 宿舍闸机, 3 == 宿舍房间, 4 == UWB)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "num", value = "次数", paramType = "query", dataType="int"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result notInOrOut(@ApiIgnore @RequestParam Map<String, Object> params){
        List<DataAnalysisDTO> dataAnalysisDTOS = dataAnalysisServer.notInOrOut(params);
        return new Result<>().ok(dataAnalysisDTOS);
    }

    @GetMapping("onlyInNotOut")
    @ApiOperation("只进未出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型(2 == 教学楼闸机 , 1 == 宿舍闸机, 3 == 宿舍房间, 4 == UWB)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "num", value = "次数", paramType = "query", dataType="int"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result onlyInNotOut(@ApiIgnore @RequestParam Map<String, Object> params){
        List<DataAnalysisDTO> dataAnalysisDTOS = dataAnalysisServer.onlyInNotOut(params);
        return new Result<>().ok(dataAnalysisDTOS);
    }

    @GetMapping("onlyOutNotIn")
    @ApiOperation("只出未进")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型(2 == 教学楼闸机 , 1 == 宿舍闸机, 3 == 宿舍房间, 4 == UWB)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "num", value = "次数", paramType = "query", dataType="int"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result onlyOutNotIn(@ApiIgnore @RequestParam Map<String, Object> params){
        List<DataAnalysisDTO> dataAnalysisDTOS = dataAnalysisServer.onlyOutNotIn(params);
        return new Result<>().ok(dataAnalysisDTOS);
    }
}
