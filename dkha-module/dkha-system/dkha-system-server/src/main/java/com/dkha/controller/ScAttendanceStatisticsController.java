package com.dkha.controller;

import com.dkha.commons.tools.excel.ExportExcelByPoiUtil;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.ScAttendanceStatisticsDTO;
import com.dkha.dto.ScAttendanceStatisticsDataDTO;
import com.dkha.service.ScAttendanceStatisticsService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 考勤统计
 *
 * @author Mark
 * @since v1.0.0 2020-12-14
 */
@RestController
@RequestMapping("/scattendancestatistics")
@Api(tags = "考勤统计")
public class ScAttendanceStatisticsController {
    @Autowired
    private ScAttendanceStatisticsService scAttendanceStatisticsService;


    @GetMapping("dataInfo")
    @ApiOperation("信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scWaname", value = "职工姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "scWaid", value = "职工ID", paramType = "query", dataType = "String")
    })
//    @PreAuthorize("hasAuthority(':scattendancestatistics:dataInfo')")
    public Result<List<ScAttendanceStatisticsDataDTO>> dataInfo(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<ScAttendanceStatisticsDataDTO> page = scAttendanceStatisticsService.dataInfo(params);

        return new Result<List<ScAttendanceStatisticsDataDTO>>().ok(page);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "scWaname", value = "职工姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "scWaid", value = "职工ID", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        //导出数据
        List<ScAttendanceStatisticsDTO> scAttendanceStatisticsDTOS = scAttendanceStatisticsService.list(params);
        if (scAttendanceStatisticsDTOS.size() > 0) {
            //表头
            String[] titleAttr = {"姓名", "房间名称", "次数"};
            // 每列的宽度
            int[] widthAttr = {30, 70, 30};
            // 导出文件名
            String titleHead = "考勤分析报表";
            if (params.get("startDate") != null && params.get("endDate") != null) {
                titleHead = "考勤分析报表（" + params.get("startDate") + " ~ " + params.get("endDate") + "）";
            }
            List<Map<String, String>> list = new ArrayList();
            /*这边是制造一些数据，注意每个list中map的key要和标题数组中的元素一致*/
            for (ScAttendanceStatisticsDTO scAttendanceStatisticsDTO : scAttendanceStatisticsDTOS) {
                HashMap<String, String> map = new HashMap();
                map.put("姓名", scAttendanceStatisticsDTO.getScWaname());
                map.put("房间名称", scAttendanceStatisticsDTO.getAeDevicename());
                map.put("次数", scAttendanceStatisticsDTO.getStNum().toString());
                list.add(map);
            }
            // 最终的数据结构，最外层map的key就是每个sheet页的sheet名称。List为每个sheet页的数据。
            Map<String, List<Map<String, String>>> map = new HashMap();
            map.put(titleHead, list);
            ExportExcelByPoiUtil.createExcel(response, titleAttr, titleHead, widthAttr, map, new int[]{0}/* 此处数组为需要合并的列，可能有的需求是只需要某些列里面相同内容合并 */);

        } else {
          throw new RenException(-10001,"无数据导出");
        }

    }

}
