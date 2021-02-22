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
import com.dkha.dto.ScStudentsDTO;
import com.dkha.excel.ScStudentsExcel;
import com.dkha.service.ScStudentsService;
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
import java.util.List;
import java.util.Map;


/**
 * 学生档案信息
 *
 * @since v1.0.0 2020-08-23
 */
@RestController
@RequestMapping("scstudents")
@Api(tags="学生档案信息")
public class ScStudentsController {

    @Value("${minio.url}")
    private String minioPath;

    @Autowired
    MinioUtil minioUtil;

    @Autowired
    private ScStudentsService scStudentsService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "scStuname", value = "姓名", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "scNo", value = "学号", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "scStatus", value = "学籍状态", paramType = "query", dataType="String")
    })
    @PreAuthorize("hasAuthority('system:scstudents:page')")
    public Result<PageData<ScStudentsDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<ScStudentsDTO> page = scStudentsService.page(params);

        return new Result<PageData<ScStudentsDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @PreAuthorize("hasAuthority('system:scstudents:info')")
    public Result<ScStudentsDTO> get(@PathVariable("id") String id){
        ScStudentsDTO data = scStudentsService.get(id);

        return new Result<ScStudentsDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
//    @PreAuthorize("hasAuthority('system:scstudents:save')")
    public Result save(@RequestBody ScStudentsDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        scStudentsService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
//    @PreAuthorize("hasAuthority('system:scstudents:update')")
    public Result update(@RequestBody ScStudentsDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        scStudentsService.update(dto);
        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
//    @PreAuthorize("hasAuthority('system:scstudents:delete')")
    public Result delete(@PathVariable("id") Long id){
        //效验数据
        AssertUtils.isNull(id, "id");

        scStudentsService.delete(id);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
//    @PreAuthorize("hasAuthority('system:scstudents:export')")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<ScStudentsDTO> list = scStudentsService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, ScStudentsExcel.class);
    }

    @PostMapping("importInfoExcel")
    @ApiOperation(value = "导入信息")
    public Result importInfoExcel(@RequestParam("file") MultipartFile file){
        scStudentsService.importInfoExcel(file);
        return new Result("导入数据成功");
    }

    @PostMapping("importImg")
    @ApiOperation(value = "导入图片")
    @PreAuthorize("hasAuthority('system:scstudents:importImg')")
    public Result importImg(@RequestParam("file") MultipartFile[] file){
        scStudentsService.importImg(file);
        return new Result("导入图片成功");
    }

    @PostMapping("importRegisterInfoExcel")
    @ApiOperation(value = "学生登记绑卡导入信息")
    public Result importRegisterInfoExcel(@RequestParam("file") MultipartFile file){
        scStudentsService.importRegisterInfoExcel(file);

        return new Result("导入数据成功");
    }

    @PostMapping("getStudentsInfo")
    @ApiOperation(value = "获取所有未入住学生信息")
    public Result getStudentsInfo(@RequestBody List<Long> id){

        List<ScStudentsDTO> data = scStudentsService.getStudentsInfo(id);

        return new Result<List<ScStudentsDTO>>().ok(data);
    }

    @PostMapping("pageExport")
    @ApiOperation("学生档案信息选择导出")
//    @PreAuthorize("hasAuthority('system:scstudents:pageExport')")
    public Result<List<ScStudentsDTO>> pageExport(@RequestBody List<Long> ids,
                                                      HttpServletResponse response){
        //获取分页数据
        List<ScStudentsDTO> studentsInfo = scStudentsService.getStudentsByIds(ids);
        studentsInfo.forEach(e->{
            String filepath = e.getScPhotoimg();
            String fileName = filepath.substring(filepath.lastIndexOf("/"));
            String serverpath = MinioUtil.getUploadPath();
            String localsavepath = serverpath + fileName;
            File file = new File(localsavepath);
            if (!file.exists()) {
                localsavepath = minioUtil.downloadFileToServeLocal(filepath, fileName);
            }
            //将学生照片转换为二进制数组
            byte[] bytes = image2byte(file.getPath());
            e.setScPhotoimgByte(bytes);
        });
        //学生信息导出
        try {
            ExcelUtils.exportExcelToTarget(response,"学生档案导出表",studentsInfo,ScStudentsExcel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result<List<ScStudentsDTO>>().ok(studentsInfo);
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
