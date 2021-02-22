package com.dkha.controller;

import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.validator.AssertUtils;
import com.dkha.commons.tools.validator.ValidatorUtils;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import com.dkha.dto.ScWorkersarchivesDTO;
import com.dkha.excel.ScWorkersarchivesExcel;
import com.dkha.service.ScWorkersarchivesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 教职工档案
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scworkersarchives")
@Api(tags="教职工档案")
public class ScWorkersarchivesController {

    @Value("${minio.url}")
    private String minioPath;

    @Autowired
    MinioUtil minioUtil;

    @Autowired
    private ScWorkersarchivesService scWorkersarchivesService;


    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "scWaname", value = "姓名", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "scEmpno", value = "职工编号", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('system:scworkersarchives:page')")
    public Result<PageData<ScWorkersarchivesDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScWorkersarchivesDTO> page = scWorkersarchivesService.page(params);

        return new Result<PageData<ScWorkersarchivesDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scworkersarchives:info')")
    public Result<ScWorkersarchivesDTO> get(@PathVariable("id") String id){
        ScWorkersarchivesDTO data = scWorkersarchivesService.get(id);

        return new Result<ScWorkersarchivesDTO>().ok(data);
    }

    @GetMapping("getAll")
    @ApiOperation("信息所有")
//    @PreAuthorize("hasAuthority('system:scworkersarchives:info')")
    public Result<List<ScWorkersarchivesDTO>> getAll(){
        List<ScWorkersarchivesDTO> data = scWorkersarchivesService.list(new HashMap<>());
        return new Result<List<ScWorkersarchivesDTO>>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scworkersarchives:save')")
    public Result save(@RequestBody ScWorkersarchivesDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scWorkersarchivesService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scworkersarchives:update')")
    public Result update(@RequestBody ScWorkersarchivesDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scWorkersarchivesService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scworkersarchives:delete')")
    public Result delete(@PathVariable("id") Long id){
        //效验数据
        AssertUtils.isNull(id, "id");

        scWorkersarchivesService.delete(id);

        return new Result();
    }

//    @GetMapping("export")
//    @ApiOperation("导出")
////    @PreAuthorize("hasAuthority('system:scworkersarchives:export')")
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<ScWorkersarchivesDTO> list = scWorkersarchivesService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, ScWorkersarchivesExcel.class);
//    }

    @PostMapping("importInfoExcel")
    @ApiOperation(value = "导入信息")
    public Result importInfoExcel(@RequestParam("file") MultipartFile file){
        scWorkersarchivesService.importInfoExcel(file);
        return new Result("导入数据成功");
    }

    @PostMapping("importImg")
    @ApiOperation(value = "导入图片")
    @PreAuthorize("hasAuthority('system:scworkersarchives:importImg')")
    public Result importImg(@RequestParam("file") MultipartFile[] file){
        scWorkersarchivesService.importImg(file);
        return new Result("导入图片成功");
    }

    @PostMapping("importRegisterInfoExcel")
    @ApiOperation(value = "职工登记绑卡导入信息")
    public Result importRegisterInfoExcel(@RequestParam("file") MultipartFile file){
        scWorkersarchivesService.importRegisterInfoExcel(file);

        return new Result("导入数据成功");
    }

    @PostMapping("pageExport")
    @ApiOperation("职工档案信息选择导出")
//    @PreAuthorize("hasAuthority('system:scworkersarchives:page')")
    public Result<List<ScWorkersarchivesDTO>> pageExport(@RequestBody List<Long> ids,
                                                             HttpServletResponse response){
        List<ScWorkersarchivesDTO> workerInfo = scWorkersarchivesService.getWorkesByIds(ids);
        workerInfo.forEach(e->{
            String filepath = e.getScPhotoimg();
            String fileName = filepath.substring(filepath.lastIndexOf("/"));
            String serverpath = MinioUtil.getUploadPath();
            String localsavepath = serverpath + fileName;
            File file = new File(localsavepath);
            if (!file.exists()) {
                localsavepath = minioUtil.downloadFileToServeLocal(filepath, fileName);
            }
            //将职工照片转换为二进制数组
            byte[] bytes = image2byte(file.getPath());
            e.setScPhotoimgByte(bytes);
        });
        //职工信息导出
        try {
            ExcelUtils.exportExcelToTarget(response,"职工档案导出表",workerInfo,ScWorkersarchivesExcel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result<List<ScWorkersarchivesDTO>>().ok(workerInfo);
    }

    /**
     * 图片转换为二进制数组
     * @param path
     * @return
     */
    public byte[] image2byte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }
}
