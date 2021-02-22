package com.dkha.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScDormitoryDTO;
import com.dkha.dto.ScDormitoryfloorDTO;
import com.dkha.dto.UwbFloorDTO;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.service.ScDormitoryfloorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 楼栋，楼层信息
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scdormitoryfloor")
@Api(tags="楼栋，楼层信息")
public class ScDormitoryfloorController {
    @Autowired
    private ScDormitoryfloorService scDormitoryfloorService;

    @GetMapping("page")
    @ApiOperation("列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "上级ID", paramType = "query", dataType="String"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "query", dataType="String",required = true)
    })
//    @PreAuthorize("hasAuthority('system:scdormitoryfloor:page')")
    public Result<List<ScDormitoryfloorDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        List<ScDormitoryfloorDTO> list = scDormitoryfloorService.list(params);
        return new Result<List<ScDormitoryfloorDTO>>().ok(list);
    }

    @GetMapping("tree/{dfPurpose}")
    @ApiOperation("树形数据")
    public Result<List<Map<String, Object>>> tree(@PathVariable(value = "dfPurpose") Integer dfPurpose){
        List<Map<String, Object>> list = scDormitoryfloorService.getTreeList(dfPurpose);
        return new Result<List<Map<String, Object>>>().ok(list);
    }

    @GetMapping("roleTree")
    @ApiOperation("树形数据(角色区分)")
    public Result<List<Map<String, Object>>> roleTree(){
        List<Map<String, Object>> list = scDormitoryfloorService.roleTree();
        return new Result<List<Map<String, Object>>>().ok(list);
    }



    @GetMapping("roomTree/{dfPurpose}")
    @ApiOperation("房间树形数据")
    public Result<List<Map<String, Object>>> roomTree(@PathVariable(value = "dfPurpose") Integer dfPurpose){
        List<Map<String, Object>> list = scDormitoryfloorService.roomTree(dfPurpose);
        return new Result<List<Map<String, Object>>>().ok(list);
    }


    @GetMapping("getByFloorId/{floorId}")
    @ApiOperation("获取房间")
    public Result<List<ScDormitoryDTO>> getByFloorId(@PathVariable(value = "floorId") Long floorId){
        List<ScDormitoryDTO> entities = scDormitoryfloorService.getByFloorId(floorId);
        return new Result<List<ScDormitoryDTO>>().ok(entities);
    }

    @GetMapping("{id}/{type}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scdormitoryfloor:info')")
    public Result<ScDormitoryfloorDTO> get(@PathVariable("id") Long id,@PathVariable("type") Integer type){
        ScDormitoryfloorDTO data = scDormitoryfloorService.get(id,type);

        return new Result<ScDormitoryfloorDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("hasAuthority('system:scdormitoryfloor:save')")
    public Result save(@RequestBody ScDormitoryfloorDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scDormitoryfloorService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("hasAuthority('system:scdormitoryfloor:update')")
    public Result update(@RequestBody ScDormitoryfloorDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scDormitoryfloorService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}/{type}")
    @ApiOperation("删除")
    @PreAuthorize("hasAuthority('system:scdormitoryfloor:delete')")
    public Result delete(@PathVariable("id") Long id,@PathVariable("type") Integer type){
        //效验数据
        AssertUtils.isNull(id, "id");

        scDormitoryfloorService.delete(id,type);

        return new Result();
    }


    @GetMapping("uwbbuildingtree")
    @ApiOperation("uwb树形楼层数据")
    public Result< List<UwbFloorDTO>> uwbbuildingtree(){
        List<UwbFloorDTO> uwbjsonstr = scDormitoryfloorService.uwbbuildingtree();
        return new Result<List<UwbFloorDTO>>().ok(uwbjsonstr);
    }

    public static void main(String[] args) {

    }

}
