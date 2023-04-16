package com.example.graduationspringboot.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUploadUtil {
    /**
     * 上传文件
     *
     */
    //MultipartFile富文本类型
    public static void upload(List<MultipartFile> fileList, List<String> filePath) {

        // 文件存储位置，文件的目录要存在才行，可以先创建文件目录，然后进行存储
//        String filePath = "D:/hfutjavaproject/小组/nihenkaixinduibudui/后端/work1.0/src/main/resources/imgs/"+fileName+'.'+ multipartFile.getOriginalFilename().split("\\.")[multipartFile.getOriginalFilename().split("\\.").length-1];
//        File file = new File(filePath);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        // 文件存储
//        try {
//            multipartFile.transferTo(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return multipartFile.getOriginalFilename().split("\\.")[1];
        String commonPath = "E:/graduation/imgs/";
        for (int i = 0;i<fileList.size();i++){
            String subFilePath = filePath.get(i);
            String path = commonPath + subFilePath;
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 文件存储
            try {
                fileList.get(i).transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}


