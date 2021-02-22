package com.dkha.controller;

import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dto.AidoorfivePassword;
import com.dkha.dto.DoorAndPersonListDTO;
import com.dkha.dto.ScDormitorypersonDTO;
import com.dkha.dto.ScDormitorypersonInfoDTO;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.excel.ScDormitorypersonExcel;
import com.dkha.service.FvScDeviceService;
import com.dkha.service.ScDormitoryService;
import com.dkha.service.ScDormitorypersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 宿舍当前入住人员信息
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scdormitoryperson")
@Api(tags="宿舍当前入住人员信息")
public class ScDormitorypersonController {
    @Autowired
    private ScDormitorypersonService scDormitorypersonService;
    @Autowired
    private ScDormitoryDao scDormitoryDao;
    @Autowired
    private FvScDeviceService fvScDeviceService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "floorId", value = "楼栋/楼层/房间ID", paramType = "query", dataType="Long"),
        @ApiImplicitParam(name = "type", value = "类型", paramType = "query", dataType="Long"),
        @ApiImplicitParam(name = "scStuname", value = "姓名", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "scNo", value = "学号", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('system:scdormitoryperson:page')")
    public Result<PageData<ScDormitorypersonInfoDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScDormitorypersonInfoDTO> page = scDormitorypersonService.page(params);

        return new Result<PageData<ScDormitorypersonInfoDTO>>().ok(page);
    }


    @GetMapping("getRoomCheckInInfo")
    @ApiOperation("房间入住信息统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "floorId", value = "楼栋/楼层/房间ID", paramType = "query", dataType="Long"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "query", dataType="Long"),
            @ApiImplicitParam(name = "scStuname", value = "姓名", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "scNo", value = "学号", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('system:scdormitoryperson:page')")
    public Result<Map<String,Object>> getRoomCheckInInfo(@ApiIgnore @RequestParam Map<String, Object> params){
        Map<String,Object> roomCheckInInfo = scDormitorypersonService.getRoomCheckInInfo(params);

        return new Result<Map<String,Object>>().ok(roomCheckInInfo);
    }


    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("hasAuthority('system:scdormitoryperson:save')")
    public Result save(@RequestBody List<ScDormitorypersonDTO> dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        List<String> ids = new ArrayList<>();
        List<AidoorfivePassword> fiveDoors = new ArrayList<>();
        //获取设备下发参数集合
        for (ScDormitorypersonDTO scDormitoryperson : dto) {

            if (fiveDoors.size()==0){
                //根据房间ID获取房间实体
                ScDormitoryEntity dormitoryEntity = scDormitoryDao.getById(scDormitoryperson.getDrId());
                //根据房间号查询设备数据(设备密码,序列号,房间名称)
                List<FvScDeviceEntity> byDrNum = fvScDeviceService.getByDrNum(dormitoryEntity.getDrNum());
                AidoorfivePassword aidoorfivePassword = new AidoorfivePassword();
                aidoorfivePassword.setFPassword(byDrNum.get(0).getFPassword());
                aidoorfivePassword.setFSerial(byDrNum.get(0).getFSerial());
                aidoorfivePassword.setFName(byDrNum.get(0).getFName());
                fiveDoors.add(aidoorfivePassword);
            }
            ids.add(scDormitoryperson.getScStdid().toString());
        }

        if(fiveDoors==null||fiveDoors.size()==0){
            throw  new RuntimeException("该人员所示门禁设备信息未查询到!");
        }
        DoorAndPersonListDTO doorAndPersonListDTO = new DoorAndPersonListDTO();
        doorAndPersonListDTO.setIds(ids);
        doorAndPersonListDTO.setFiveDoors(fiveDoors);
        /**
         * 保存入住人员和下发人员权限信息
         */
        scDormitorypersonService.save(dto,doorAndPersonListDTO);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("hasAuthority('system:scdormitoryperson:update')")
    public Result update(@RequestBody List<ScDormitorypersonDTO> dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        List<String> ids = new ArrayList<>();
        List<AidoorfivePassword> fiveDoors = new ArrayList<>();
        //获取设备下发参数集合
        for (ScDormitorypersonDTO scDormitoryperson : dto) {

            if (fiveDoors.size()==0){
                //根据房间ID获取房间实体
                ScDormitoryEntity dormitoryEntity = scDormitoryDao.getById(scDormitoryperson.getDrId());
                //根据房间号查询设备数据(设备密码,序列号,房间名称)
                List<FvScDeviceEntity> byDrNum = fvScDeviceService.getByDrNum(dormitoryEntity.getDrNum());
                AidoorfivePassword aidoorfivePassword = new AidoorfivePassword();
                aidoorfivePassword.setFPassword(byDrNum.get(0).getFPassword());
                aidoorfivePassword.setFSerial(byDrNum.get(0).getFSerial());
                aidoorfivePassword.setFName(byDrNum.get(0).getFName());
                fiveDoors.add(aidoorfivePassword);
            }
            ids.add(scDormitoryperson.getScStdid().toString());
        }

        DoorAndPersonListDTO doorAndPersonListDTO = new DoorAndPersonListDTO();
        doorAndPersonListDTO.setIds(ids);
        doorAndPersonListDTO.setFiveDoors(fiveDoors);

        scDormitorypersonService.update(dto,doorAndPersonListDTO);

        return new Result();
    }

    @PostMapping("id")
    @ApiOperation("删除")
    @PreAuthorize("hasAuthority('system:scdormitoryperson:delete')")
    public Result delete(@RequestBody String[] id){
        //效验数据
        //AssertUtils.isNull(id, "id");
        scDormitorypersonService.delete(id);
        return new Result();
    }


    @GetMapping("getOutNum")
    @ApiOperation(value = "获取外出人数")
    public Result getOutNum(){
        Map<String,Object> num = scDormitorypersonService.getOutNum();

        return new Result<Map<String,Object>>().ok(num);
    }

    @PostMapping("importInfoExcel")
    @ApiOperation("宿舍入住人员信息导入")
    @PreAuthorize("hasAuthority('system:scdormitoryperson:import')")
    public Result importPerson(@RequestParam("file") MultipartFile file){
        scDormitorypersonService.importInfoExcel(file);
        return new Result("宿舍入住人员导入完成");
    }

    @GetMapping("exportInfoExcel")
    @ApiOperation("宿舍未归人员信息导出")
//    @PreAuthorize("hasAuthority('system:scdormitoryperson:export')")
    public void exportInfoExcel(HttpServletResponse response) throws Exception {
        scDormitorypersonService.exportInfoExcel(response);
    }
}
