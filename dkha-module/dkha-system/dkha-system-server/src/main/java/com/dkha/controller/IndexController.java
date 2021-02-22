package com.dkha.controller;

import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.ScCurrenthistoryDTO;
import com.dkha.dto.ScFaceverificationDTO;
import com.dkha.dto.ScRoomcurrentEverydayDTO;
import com.dkha.dto.ScStudentsOutHistoryDTO;
import com.dkha.entity.ScCurrenthistorySumEntity;
import com.dkha.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 首页数据
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-08-29 10:19
 */

@RestController
@RequestMapping("index")
@Api(tags="首页数据")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("dataInfo")
    @ApiOperation("报警/档案/统计")
    public Result dataInfo(){
        Map<String,Object> map = indexService.dataInfo();
        return new Result<>().ok(map);
    }


    @GetMapping("aidooreight")
    @ApiOperation("2号楼出入统计")
    public Result aidooreight(){
        Map<String,Object> map = indexService.aidooreight();
        return new Result<>().ok(map);
    }

    @GetMapping("visitorrecordStatistics")
    @ApiOperation("2号楼访客出入统计")
    public Result visitorrecordStatistics(){
        Map<String,Object> map = indexService.visitorrecordStatistics();
        return new Result<>().ok(map);
    }

    @GetMapping("twoBuildingPeerRecord")
    @ApiOperation("2号楼通行记录")
    public Result twoBuildingPeerRecord(){
        List<ScFaceverificationDTO> map = indexService.twoBuildingPeerRecord();
        return new Result<>().ok(map);
    }

    @GetMapping("layeredStatistics")
    @ApiOperation("2号楼每层楼人数统计")
    public Result layeredStatistics(){
        Map<String,Object> map = indexService.layeredStatistics();
        return new Result<>().ok(map);
    }

    @GetMapping("dormitoryCheckInStatistics")
    @ApiOperation("宿舍入住人数统计")
    public Result dormitoryCheckInStatistics(){
        Map<String,Object> map = indexService.dormitoryCheckInStatistics();
        return new Result<>().ok(map);
    }


    @GetMapping("notReturnedStatistics")
    @ApiOperation("未归人员一周统计")
    public Result notReturnedStatistics(){
        List<ScStudentsOutHistoryDTO> map = indexService.notReturnedStatistics();
        return new Result<>().ok(map);
    }


    @GetMapping("dormitoryPeerRecord")
    @ApiOperation("宿舍通行记录")
    public Result dormitoryPeerRecord(){
        List<ScFaceverificationDTO> map = indexService.dormitoryPeerRecord();
        return new Result<>().ok(map);
    }


    @GetMapping("visitorInformation")
    @ApiOperation("访客信息")
    public Result visitorInformation(){
        List<Map<String, Object>> list = indexService.visitorInformation();
        return new Result<>().ok(list);
    }

    @GetMapping("temperatureWarning")
    @ApiOperation("温度预警")
    public Result temperatureWarning(){
        List<Map<String, Object>> list = indexService.temperatureWarning();
        return new Result<>().ok(list);
    }



    @GetMapping("roomCurrentAlarm")
    @ApiOperation("宿舍电流报警")
    public Result roomCurrentAlarm(){
        //List<ScRoomcurrentEverydayDTO> list = indexService.roomCurrentAlarm();
        List<ScCurrenthistorySumEntity>  list = indexService.getByTop();
        return new Result<>().ok(list);
    }

    @GetMapping("uwbLabelType")
    @ApiOperation("uwb标签类别")
    public Result uwbLabelType(){
        Map<String, Object> list = indexService.uwbLabelType();
        return new Result<>().ok(list);
    }



    @GetMapping("/thermalTop")
    @ApiOperation("弱电井的温度（热成像设备）")
    public Result thermalTop(){
        List<Map<String,Object>> map = indexService.thermalTop();
        return new Result().ok(map);
    }



}
