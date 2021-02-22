package com.dkha.controller;

import com.dkha.service.GlobalValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 全局验证
 * @author xiedong
 * @version v1.0
 * @date 2020-09-09 9:52
 */
@RestController
@RequestMapping("/globalValidation")
@Api(tags="全局验证")
public class GlobalValidationController {

    @Autowired
    private GlobalValidationService globalValidationService;


    @GetMapping("/checkUwb")
    @ApiOperation("验证UWB编码")
    public Boolean checkUwb(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "uwb",required = true) String uwb){
        return globalValidationService.checkUwb(id,uwb);
    }

    @GetMapping("/checkRfid")
    @ApiOperation("验证RFID编码")
    public Boolean checkRfid(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "uwb",required = true) String rfid){
        return globalValidationService.checkRfid(id,rfid);
    }

}
