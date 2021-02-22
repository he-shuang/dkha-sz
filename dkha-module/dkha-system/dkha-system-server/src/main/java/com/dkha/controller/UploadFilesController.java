package com.dkha.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.tools.utils.FileFormatVerify;
import com.dkha.commons.tools.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xiedong
 * @version v1.0
 */
@RestController
@Api(tags="MInio文件上传")
public class UploadFilesController {

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file,@RequestParam("folderName") String folderName) throws Exception {
        if (file.isEmpty()) {
            return new Result<Map<String, Object>>().error("上传文件为空");
        }
        FileFormatVerify fileFormatVerify = new FileFormatVerify();
        if(!fileFormatVerify.suffixVerify(file)){
            return new Result<Map<String, Object>>().error("上传文格式不正确");
        }

        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        String originalFilename = file.getOriginalFilename();
        JSONObject jsonObject = minioUtil.uploadFile(file.getBytes(),folderName, originalFilename.substring(0,originalFilename.indexOf(".")), suffix);
        Map<String, Object> data = new HashMap<>(1);
        data.put("src", jsonObject.getString("url"));
        data.put("path", jsonObject.getString("path"));
        return new Result<Map<String, Object>>().ok(data);
    }
}
