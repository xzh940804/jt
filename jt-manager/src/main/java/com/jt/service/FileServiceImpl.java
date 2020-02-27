package com.jt.service;

import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements  FileService{
    /**
     * 步骤:
     * 	 1.准备文件上传根目录  D:\1_JT\images
     * 	 2.校验文件类型. jpg|png|gif......
     * 	 3.为了检索快速,需要分目录存储   1.按照类型 2.按照商品类目 3.按照时间
     * 	 4.文件名称不能重复  UUID
     */

    @Value("${image.localDirPath}")
    private String localDirPath;  // = "D:/1_JT/images/";
    @Value("${image.urlPath}")	//
    private String urlPath;

    @Override
    public ImageVO uploadFile(MultipartFile uploadFile) {
        ImageVO imageVo=null;
        String fileName=uploadFile.getOriginalFilename();
        fileName=fileName.toLowerCase();
        if(!fileName.matches("^.+\\.(jpg|png|gif)$")){
            return  ImageVO.fail();
        }
        try{
            BufferedImage bufferedImage= ImageIO.read(uploadFile.getInputStream());
            int height  =bufferedImage.getHeight();
            int width=bufferedImage.getWidth();
            if(height==0||width==0){
                return ImageVO.fail();
            }
            String datePath=new SimpleDateFormat("yyyy/MM/dd/") .format(new Date());;
            String fileLocalPath=localDirPath+datePath;
            File fileDir=new File(fileLocalPath);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }

            String uuid= UUID.randomUUID().toString();
            int index= fileName.lastIndexOf(".");
            String type=fileName.substring(index);
            String uuidName=uuid+type;

            String realFilePath=fileLocalPath+uuidName;
            uploadFile.transferTo(new File(realFilePath));
            String url=urlPath+datePath+uuidName;
            imageVo = ImageVO.success(url, width, height);
        }catch (Exception e){
            e.printStackTrace();
            return ImageVO.fail();
        }
        return imageVo;
    }
}
