package com.dkha.controller;


import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.log.annotation.LogOperation;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.excel.ExportExcelByPoiUtil;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dto.ScDevicebindingDTO;
import com.dkha.dto.ScRoomCountDTO;
import com.dkha.dto.ScStudentsCountDTO;
import com.dkha.dto.VisitorStatisticsDTO;
import com.dkha.entity.ScStatisticsEntity;
import com.dkha.excel.*;
import com.dkha.service.ReportFormService;
import com.dkha.service.ScStudentsOutEverydayDetailsService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;

/**
 * 报表
 */
@RestController()
@RequestMapping("/reportForm")
@Api(tags="报表")
public class ReportFormController {
    @Autowired
    MinioUtil minioUtil;
    @Autowired
    private ScStudentsOutEverydayDetailsService scStudentsOutEverydayDetailsService;

    @GetMapping("dormitoryStatistics")
    @ApiOperation("人员未归寝统计分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<ScStudentsCountDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScStudentsCountDTO> page = scStudentsOutEverydayDetailsService.findCountSum(params);
        return new Result<PageData<ScStudentsCountDTO>>().ok(page);
    }

    @GetMapping("dormitoryRoomStatistics")
    @ApiOperation("宿舍未归寝统计分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<ScStudentsCountDTO>> dormitoryRoomStatistics(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScStudentsCountDTO> page = scStudentsOutEverydayDetailsService.findCountRoomSum(params);
        return new Result<PageData<ScStudentsCountDTO>>().ok(page);
    }

    @Autowired
    private ReportFormService reportFormService;


//    @GetMapping("visitorDay")
//    @ApiOperation("访客每日")
//    public Result visitorDay(){
//        Map<String,Object> map = reportFormService.visitorDay();
//        return new Result<>().ok(map);
//    }
//
    @GetMapping("visitorFloorWeek")
    @ApiOperation("访客每层楼周")
    public Result visitorWeek(){
        List<VisitorStatisticsDTO> map = reportFormService.visitorWeek();
        return new Result<>().ok(map);
    }

    @GetMapping("visitorFloorMonth")
    @ApiOperation("访客每层楼月")
    public Result visitorMonth(){
        List<VisitorStatisticsDTO> map = reportFormService.visitorMonth();
        return new Result<>().ok(map);
    }


    @GetMapping("visitorFloorWeekMonth")
    @ApiOperation("访客每层楼周/月")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateType", value = "时间类型(1 == 周 , 2 == 月)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result visitorWeekMonth(@ApiIgnore @RequestParam Map<String, Object> params){
        List<VisitorStatisticsDTO> map = reportFormService.visitorWeekMonth(params);
        return new Result<>().ok(map);
    }


    @GetMapping("visitorFloorWeekMonthExport")
    @ApiOperation("访客每层楼周/月导出")
//    @PreAuthorize("hasAuthority('system:scstudents:export')")
    public Result visitorFloorWeekMonthExport(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        Result result = new Result();
        try {
            String dateType = (String)params.get("dateType");
//            导出数据
            List<VisitorStatisticsDTO> visitorStatisticsDTOS = reportFormService.visitorWeekMonth(params);
            if(visitorStatisticsDTOS.size() > 0){
//                表头
                String[] titleAttr = {"时间段","楼层","次数"};
                // 每列的宽度
                int[] widthAttr = { 70, 30, 30 };
                // 导出文件名
                String titleHead = "访客人次周统计报表";
                if("2".equals(dateType)){
                    titleHead = "访客人次月统计报表";
                    widthAttr = new int[]{30, 30, 30};
                }
                List<Map<String, String>> list = new ArrayList();
                /*这边是制造一些数据，注意每个list中map的key要和标题数组中的元素一致*/
                for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                    HashMap<String, String> map = new HashMap();
                    map.put("时间段",visitorStatisticsDTO.getDateStr());
                    map.put("楼层",visitorStatisticsDTO.getFloorname());
                    map.put("次数",visitorStatisticsDTO.getCount());
                    list.add(map);
                }
                // 最终的数据结构，最外层map的key就是每个sheet页的sheet名称。List为每个sheet页的数据。
                Map<String, List<Map<String, String>>> map = new HashMap();
                map.put(titleHead, list);
                ExportExcelByPoiUtil.createExcel(response, titleAttr, titleHead, widthAttr, map, new int[] { 0, 1 }/* 此处数组为需要合并的列，可能有的需求是只需要某些列里面相同内容合并 */);
               result.setMsg("导出成功");
            }else{
                result.setMsg("无数据导出");
            }
        }catch (Exception e){
            result.error("导出失败");
            e.printStackTrace();
        }
        return result;
    }




    @GetMapping("visitorWorkersWeek")
    @ApiOperation("访客工作人员被访次数周")
    public Result visitorWorkersWeek(){
        List<VisitorStatisticsDTO> map = reportFormService.visitorWorkersWeek();
        return new Result<>().ok(map);
    }

    @GetMapping("visitorWorkersMonth")
    @ApiOperation("访客工作人员被访次数月")
    public Result visitorWorkersMonth(){
        List<VisitorStatisticsDTO> map = reportFormService.visitorWorkersMonth();
        return new Result<>().ok(map);
    }


    @GetMapping("visitorWorkersWeekMonth")
    @ApiOperation("访客工作人员被访次数周/月")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateType", value = "时间类型(1 == 周 , 2 == 月)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result visitorWorkersWeekMonth(@ApiIgnore @RequestParam Map<String, Object> params){
        List<VisitorStatisticsDTO> map = reportFormService.visitorWorkersWeekMonth(params);
        return new Result<>().ok(map);
    }

    @GetMapping("visitorWorkersWeekMonthExport")
    @ApiOperation("访客工作人员被访次数周/月导出")
//    @PreAuthorize("hasAuthority('system:scstudents:export')")
    public Result visitorWorkersWeekMonthExport(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        Result result = new Result();
        try {
            String dateType = (String)params.get("dateType");
//            导出数据
            List<VisitorStatisticsDTO> visitorStatisticsDTOS = reportFormService.visitorWorkersWeekMonth(params);
            if(visitorStatisticsDTOS.size() > 0){
//             表头
            String[] titleAttr = {"时间段","教职工姓名","被访次数"};

            // 每列的宽度
            int[] widthAttr = { 70, 30, 30 };
            // 导出文件名
            String titleHead = "教职工被访周统计报表";
            if("2".equals(dateType)){
                titleHead = "教职工被访月统计报表";
                widthAttr = new int[]{30, 30, 30};
            }
            List<Map<String, String>> list = new ArrayList();
            /*这边是制造一些数据，注意每个list中map的key要和标题数组中的元素一致*/
            for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                HashMap<String, String> map = new HashMap();
                map.put("时间段",visitorStatisticsDTO.getDateStr());
                map.put("教职工姓名",visitorStatisticsDTO.getName());
                map.put("被访次数",visitorStatisticsDTO.getCount());
                list.add(map);
            }
            // 最终的数据结构，最外层map的key就是每个sheet页的sheet名称。List为每个sheet页的数据。
            Map<String, List<Map<String, String>>> map = new HashMap();
            map.put(titleHead, list);
            ExportExcelByPoiUtil.createExcel(response, titleAttr, titleHead, widthAttr, map, new int[] { 0, 1 }/* 此处数组为需要合并的列，可能有的需求是只需要某些列里面相同内容合并 */);

            result.setMsg("导出成功");
        }else{
            result.setMsg("无数据导出");
        }
        }catch (Exception e){
            result.error("导出失败");
            e.printStackTrace();
        }
        return result;
    }


    @GetMapping("roomWeek")
    @ApiOperation("宿舍周")
    public Result roomWeek(){
        List<ScRoomCountDTO> map = reportFormService.roomWeek();
        return new Result<>().ok(map);
    }
    @GetMapping("roomMonth")
    @ApiOperation("宿舍月")
    public Result roomMonth(){
        List<ScRoomCountDTO> map = reportFormService.getByRoomCountMoth();
        return new Result<>().ok(map);
    }
    @GetMapping("abnormalWeek")
    @ApiOperation("宿舍周 按照人统计次数")
    public Result abnormalWeek(){
        List<ScRoomCountDTO>  map = reportFormService.getByabnormalCountWeek();
        return new Result<>().ok(map);
    }
    @GetMapping("abnormalMonth")
    @ApiOperation("宿舍月 按照人统计次数")
    public Result abnormalMonth(){
        List<ScRoomCountDTO>  map = reportFormService.getByabnormalCountMoth();
        return new Result<>().ok(map);
    }


    @GetMapping("getByAcademicBuildingWeek")
    @ApiOperation("教学楼统计异常类型  按周统计")
    public Result getByAcademicBuildingWeek(){
        List<ScRoomCountDTO>  map = reportFormService.getByAcademicBuildingWeek();
        return new Result<>().ok(map);
    }
    @GetMapping("getByAcademicBuildingMonths")
    @ApiOperation("教学楼统计异常类型  按月统计")
    public Result getByAcademicBuildingMonths(){
        List<ScRoomCountDTO>  map = reportFormService.getByAcademicBuildingMonths();
        return new Result<>().ok(map);
    }


    @GetMapping("getByAcademicBuildingPersonneMonths")
    @ApiOperation("教学楼统计异常类型  按 人 周统计")
    public Result getByAcademicBuildingPersonneMonths(){
        List<ScRoomCountDTO>  map = reportFormService.getByAcademicBuildingPersonneMonths();
        return new Result<>().ok(map);
    }
    @GetMapping("getByAcademicBuildingPersonneWeeks")
    @ApiOperation("教学楼统计异常类型  按 人 月统计")
    public Result getByAcademicBuildingPersonneWeeks(){
        List<ScRoomCountDTO>  map = reportFormService.getByAcademicBuildingPersonneWeeks();
        return new Result<>().ok(map);
    }

    @GetMapping("dormitoryStatisticsExport")
    @ApiOperation("人员未归寝统计导出")
    public void dormitoryStatisticsExport(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScStudentsCountDTO> list  = scStudentsOutEverydayDetailsService.findList(params);
        for (ScStudentsCountDTO scVisitorrecordDTO : list) {
            byte[] imgByte = minioUtil.getImgByte(scVisitorrecordDTO.getScPhotoimg());
            scVisitorrecordDTO.setScPhotoimgByte(imgByte);
        }
        ExcelUtils.exportExcelToTarget(response, null, list, ScStudentsCountExcel.class);
    }

    @GetMapping("findCountRoomExport")
    @ApiOperation("宿舍统计未归导出")
    public void findCountRoomExport(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScStudentsCountDTO> list  = scStudentsOutEverydayDetailsService.findCountRoomExcel(params);
      /*  for (ScStudentsCountDTO scVisitorrecordDTO : list) {
            byte[] imgByte = minioUtil.getImgByte(scVisitorrecordDTO.getScPhotoimg());
            scVisitorrecordDTO.setScPhotoimgByte(imgByte);
        }*/
        ExcelUtils.exportExcelToTarget(response, null, list, ScStudentsRoomExcel.class);
    }

    @GetMapping("roomStatistics")
    @ApiOperation("宿舍统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numType", value = "次数类型(1 == 次数 , 2 == 人数)", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = "dateType", value = "时间类型(1 == 周 , 2 == 月)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result roomStatistics(@ApiIgnore @RequestParam Map<String, Object> params){
        List<ScStatisticsEntity> map = reportFormService.roomStatistics(params);
        return new Result<>().ok(map);
    }
    @GetMapping("academicStatistics")
    @ApiOperation("教学楼统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numType", value = "次数类型(1 == 次数 , 2 == 人数)", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = "dateType", value = "时间类型(1 == 周 , 2 == 月)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result academicStatistics(@ApiIgnore @RequestParam Map<String, Object> params){
        List<ScStatisticsEntity> map = reportFormService.academicStatistics(params);
        return new Result<>().ok(map);
    }


    @GetMapping("exportgetByCountWeek")
    @ApiOperation("宿舍楼按照周   次统计导出")
    @LogOperation("导出")
    // @RequiresPermissions("demo:scdevicebinding:export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numType", value = "次数类型(1 == 次数 , 2 == 人数)", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = "dateType", value = "时间类型(1 == 周 , 2 == 月)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public void exportgetByCountWeek (@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        String numType = (String)params.get("numType");
        String dateType = (String)params.get("dateType");
        List<ScStatisticsEntity>  scStatisticsEntities = reportFormService.roomStatistics(params);
        if(scStatisticsEntities.size() > 0) {
            List<Map<String, String>> list = new ArrayList();
            for (ScStatisticsEntity statisticsEntity : scStatisticsEntities) {
                HashMap<String, String> map = new HashMap();
                map.put("时间段", "1".equals(dateType) ? statisticsEntity.getWeeks() : statisticsEntity.getMonths());
                map.put("未进未出次数", String.valueOf(statisticsEntity.getType1()));
                map.put("只进未出次数", String.valueOf(statisticsEntity.getType2()));
                map.put("只出未进次数", String.valueOf(statisticsEntity.getType3()));
                map.put("未进未出人数", String.valueOf(statisticsEntity.getTypestudent1()));
                map.put("只进未出人数", String.valueOf(statisticsEntity.getTypestudent2()));
                map.put("只出未进人数", String.valueOf(statisticsEntity.getTypestudent3()));
                list.add(map);
            }
            // 最终的数据结构，最外层map的key就是每个sheet页的sheet名称。List为每个sheet页的数据。
            Map<String, List<Map<String, String>>> map = new HashMap();
            String[] titleAttr = null;
            String titleHead = null;
            int[] widthAttr = {30, 30, 30, 30};
            //时间类型(1 == 周 , 2 == 月)
            if ("1".equals(dateType)) {
                // 每列的宽度
                widthAttr = new int[]{70, 30, 30, 30};
                //次数类型(1 == 次数 , 2 == 人数)
                if ("1".equals(numType)) {
//                表头
                    titleAttr = new String[]{"时间段", "未进未出次数", "只进未出次数", "只出未进次数"};
                    // 导出文件名
                    titleHead = "宿舍楼周次数报表";
                } else {
//                表头
                    titleAttr = new String[]{"时间段", "未进未出人数", "只进未出人数", "只出未进人数"};
                    // 导出文件名
                    titleHead = "宿舍楼周人数报表";
                }
            } else {
                if ("1".equals(numType)) {
                    titleAttr = new String[]{"时间段", "未进未出次数", "只进未出次数", "只出未进次数"};
                    // 导出文件名
                    titleHead = "宿舍楼月人次报表";
                } else {
                    titleAttr = new String[]{"时间段", "未进未出人数", "只进未出人数", "只出未进人数"};
                    // 导出文件名
                    titleHead = "宿舍楼月人数报表";
                }
            }
            map.put(titleHead, list);
            ExportExcelByPoiUtil.createExcel(response, titleAttr, titleHead, widthAttr, map, new int[]{0}/* 此处数组为需要合并的列，可能有的需求是只需要某些列里面相同内容合并 */);
        }
    }

   /* @GetMapping("exportgetByCountMonths")
    @ApiOperation("宿舍楼按照月   次统计导出")
    @LogOperation("导出")
    // @RequiresPermissions("demo:scdevicebinding:export")
    public void exportgetByCountMonths(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScStatisticsEntity>  list = reportFormService.getByCountMonths(params);
        ExcelUtils.exportExcelToTarget(response, null, list, ScStatisticsMonthExcel.class);
    }*/


    @GetMapping("exportgetByAcademicWeek")
    @ApiOperation(" 教学楼按照周   次统计导出")
    @LogOperation("导出")
    // @RequiresPermissions("demo:scdevicebinding:export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numType", value = "次数类型(1 == 次数 , 2 == 人数)", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = "dateType", value = "时间类型(1 == 周 , 2 == 月)", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public void exportgetByAcademicWeek(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        String numType = (String)params.get("numType");
        String dateType = (String)params.get("dateType");
        List<ScStatisticsEntity>  scStatisticsEntities = reportFormService.academicStatistics(params);
        if(scStatisticsEntities.size() > 0) {
            List<Map<String, String>> list = new ArrayList();
            for (ScStatisticsEntity statisticsEntity : scStatisticsEntities) {
                HashMap<String, String> map = new HashMap();
                map.put("时间段", "1".equals(dateType) ? statisticsEntity.getWeeks() : statisticsEntity.getMonths());
                map.put("未进未出次数", String.valueOf(statisticsEntity.getType1()));
                map.put("只进未出次数", String.valueOf(statisticsEntity.getType2()));
                map.put("只出未进次数", String.valueOf(statisticsEntity.getType3()));
                map.put("未进未出人数", String.valueOf(statisticsEntity.getTypestudent1()));
                map.put("只进未出人数", String.valueOf(statisticsEntity.getTypestudent2()));
                map.put("只出未进人数", String.valueOf(statisticsEntity.getTypestudent3()));
                list.add(map);
            }
            // 最终的数据结构，最外层map的key就是每个sheet页的sheet名称。List为每个sheet页的数据。
            Map<String, List<Map<String, String>>> map = new HashMap();
            String[] titleAttr = null;
            String titleHead = null;
            int[] widthAttr = {30, 30, 30, 30};
            //时间类型(1 == 周 , 2 == 月)
            if ("1".equals(dateType)) {
                // 每列的宽度
                widthAttr = new int[]{70, 30, 30, 30};
                //次数类型(1 == 次数 , 2 == 人数)
                if ("1".equals(numType)) {
//                表头
                    titleAttr = new String[]{"时间段", "未进未出次数", "只进未出次数", "只出未进次数"};
                    // 导出文件名
                    titleHead = "教学楼周次数报表";
                } else {
//                表头
                    titleAttr = new String[]{"时间段", "未进未出人数", "只进未出人数", "只出未进人数"};
                    // 导出文件名
                    titleHead = "教学楼周人数报表";
                }
            } else {
                if ("1".equals(numType)) {
                    titleAttr = new String[]{"时间段", "未进未出次数", "只进未出次数", "只出未进次数"};
                    // 导出文件名
                    titleHead = "教学楼月人次报表";
                } else {
                    titleAttr = new String[]{"时间段", "未进未出人数", "只进未出人数", "只出未进人数"};
                    // 导出文件名
                    titleHead = "教学楼月人数报表";
                }
            }
            map.put(titleHead, list);
            ExportExcelByPoiUtil.createExcel(response, titleAttr, titleHead, widthAttr, map, new int[]{0}/* 此处数组为需要合并的列，可能有的需求是只需要某些列里面相同内容合并 */);
        }
    }

   /* @GetMapping("exportgetByAcademicMonths")
    @ApiOperation(" 教学楼按照月  次统计导出")
    @LogOperation("导出")
    // @RequiresPermissions("demo:scdevicebinding:export")
    public void exportgetByAcademicMonths(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScStatisticsEntity>  list = reportFormService.getByAcademicMonths(params);
        ExcelUtils.exportExcelToTarget(response, null, list, ScStatisticsStudentMonthExcel.class);
    }*/
}
