package com.dkha.controller;


import com.dkha.commons.log.annotation.LogOperation;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.AlarmMassge;
import com.dkha.dto.ScDevicebindingDTO;
import com.dkha.entity.ScDevicebindingEntity;
import com.dkha.entity.ScPmalarmEntity;
import com.dkha.entity.ScTransformalarmEntity;
import com.dkha.enums.TypeEnum;
import com.dkha.service.ScDevicebindingService;
import com.dkha.service.ScPmalarmService;
import com.dkha.service.ScTransformalarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 星网云联设备位置绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@RestController
@RequestMapping("scdevicebinding")
@Api(tags="星网云联设备位置绑定表")
public class ScDevicebindingController {
    @Autowired
    private ScDevicebindingService scDevicebindingService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    //@RequiresPermissions("demo:scdevicebinding:page")
    public Result<PageData<ScDevicebindingDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScDevicebindingDTO> page = scDevicebindingService.page(params);

        return new Result<PageData<ScDevicebindingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
   // @RequiresPermissions("demo:scdevicebinding:info")
    public Result<ScDevicebindingDTO> get(@PathVariable("id") Long id){
        ScDevicebindingDTO data = scDevicebindingService.get(id);

        return new Result<ScDevicebindingDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    //@RequiresPermissions("demo:scdevicebinding:save")
    public Result save(@RequestBody ScDevicebindingDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        dto.setCreateDate(new Date());
        scDevicebindingService.save(dto);

        return new Result<ScDevicebindingDTO>().ok(dto);
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    //@RequiresPermissions("demo:scdevicebinding:update")
    public Result update(@RequestBody ScDevicebindingDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scDevicebindingService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
   // @RequiresPermissions("demo:scdevicebinding:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        scDevicebindingService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
   // @RequiresPermissions("demo:scdevicebinding:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScDevicebindingDTO> list = scDevicebindingService.list(params);
       // ExcelUtils.exportExcelToTarget(response, null, list, ScDevicebindingExcel.class);
    }

    @GetMapping("badDevice")
    @ApiOperation("设备信息")
    //@RequiresPermissions("demo:scdevicebinding:page")
    public Result<List<ScDevicebindingEntity>> badingDe(){
        List<ScDevicebindingEntity> bading = scDevicebindingService.getBadingDevice();
        return  new Result<List<ScDevicebindingEntity>>().ok(bading);
    }
    @GetMapping("getDevice")
    @ApiOperation("所有设备信息")
    //@RequiresPermissions("demo:scdevicebinding:page")
    public Result<List<ScDevicebindingEntity>>getDevice(){
        List<ScDevicebindingEntity> badingDevice = scDevicebindingService.getAllDevice();
        return  new Result<List<ScDevicebindingEntity>>().ok(badingDevice);
    }
    @GetMapping("getNoBangding")
    @ApiOperation("所有未绑定设备信息")
    //@RequiresPermissions("demo:scdevicebinding:page")
    public Result<List<ScDevicebindingEntity>> getNoBangding(){
        List<ScDevicebindingEntity> badingDevic = scDevicebindingService.getNoBangding();
        return  new Result<List<ScDevicebindingEntity>>().ok(badingDevic);
    }

    @GetMapping("getIsAlarm")
    @ApiOperation("正在报警的设备信息")
    //@RequiresPermissions("demo:scdevicebinding:page")
    public Result<List<AlarmMassge>> getIsAlarm(){
        List<AlarmMassge> badingDevic = scDevicebindingService.getIsAlarm();
        return  new Result<List<AlarmMassge>>().ok(badingDevic);
    }
    @GetMapping("getAlarmHistory")
    @ApiOperation("报警历史设备信息")
    public Result<List<AlarmMassge>> getAlarmHistoryDC() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //近三天
        String startTime =  sdf.format(new Date().getTime()-(1000*60*60*24*3));
        String endTime = sdf.format(new Date().getTime());
        List<AlarmMassge> badingDevic = scDevicebindingService.getAlarmHistory(startTime,endTime);

        return new Result<List<AlarmMassge>>().ok(null);
    }

    @PutMapping("handleAlarm")
    @ApiOperation("处理设备报警")
    public Result handleAlarm(@RequestBody AlarmMassge alarmMassge) {
        scDevicebindingService.handleAlarm(alarmMassge);
        return new Result();
    }
}