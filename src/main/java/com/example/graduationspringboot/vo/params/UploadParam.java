package com.example.graduationspringboot.vo.params;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadParam {

    private List<MultipartFile> fileList;

    private List<String> filePath;

}
