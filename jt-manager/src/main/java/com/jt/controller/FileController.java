package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    @RequestMapping("/pic/upload")
    public ImageVO uploadFile(MultipartFile uploadFile){
       return fileService.uploadFile(uploadFile);
    }
    @RequestMapping("/file")
    public  String file(MultipartFile fileImage) throws IOException {
        String fileDir="D:/1_JT/images/";
        File file=new File(fileDir);
        if(!file.exists()){
            file.mkdirs();
        }
        String name=fileImage.getName();
        System.out.println(name);
        String fileName=fileImage.getOriginalFilename();
        System.out.println(fileName);
        String realPath="D:/1_JT/images/"+fileName;
        fileImage.transferTo(new File(realPath));
        return "文件上传成功";
    }
}
