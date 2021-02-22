package com.dkha.controller;

import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.ScCurrenthistoryDTO;
import com.dkha.service.ScCurrenthistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("sccurrenthistory")
@Api(tags="电流采集记录")
public class ScCurrenthistoryController {
    @Autowired
    private ScCurrenthistoryService scCurrenthistoryService;



    @GetMapping("{roomId}/{startTime}/{endTime}")
    @ApiOperation("宿舍电流详情")
//    @PreAuthorize("hasAuthority('system:sccurrenthistory:info')")
    public Result<List<ScCurrenthistoryDTO>> getByRoomId(@PathVariable("roomId") Long roomId,@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime){
        List<ScCurrenthistoryDTO> data = scCurrenthistoryService.getByRoomId(roomId,startTime,endTime);

        return new Result<List<ScCurrenthistoryDTO>>().ok(data);
    }




}
