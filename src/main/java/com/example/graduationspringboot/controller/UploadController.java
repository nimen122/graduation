package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.utils.FileUploadUtil;
import com.example.graduationspringboot.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/Upload")
@Api(value = "上传文件接口", tags = "上传文件接口" )
public class UploadController {

    @RequestMapping(value = "/upload",method = RequestMethod.POST,produces = "application/form-data;charset=utf-8")
    @ApiOperation(value = "upload",notes = "upload")
    @CrossOrigin
    // @RequestParam中的file名应与前端的值保持一致
    public void upload(@RequestParam("fileList") List<MultipartFile> fileList, @RequestParam("filePath") List<String> filePath) {
        // replaceAll 用来替换windows中的\\ 为 /
        System.out.println("开始上传附件");
        System.out.println(filePath);
//        FileUploadUtil.upload(uploadParam.getFileList(),uploadParam.getFilePath()).replaceAll("\\\\", "/");
//        FileUploadUtil.upload(uploadParam.getFileList(),uploadParam.getFilePath());
        FileUploadUtil.upload(fileList,filePath);

//        return true;
    }

}
