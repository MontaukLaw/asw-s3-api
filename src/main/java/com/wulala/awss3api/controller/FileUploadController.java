package com.wulala.awss3api.controller;

import com.wulala.awss3api.config.ImageConfig;
import com.wulala.awss3api.entity.JsonMsg;
import com.wulala.awss3api.utils.UUIDTool;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/file")
public class FileUploadController extends BaseController {
    @Resource
    ImageConfig imageConfig;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonMsg fileUpload(MultipartFile file) {
        JsonMsg json = new JsonMsg();

        try {
            SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyyMMdd/HHmmss");
            // 获取日期,组成目录名
            //String subFolderName = dateformat1.format(new Date()) + "/";

            String baseFolder = imageConfig.getImageFolder();
            //System.out.println("baseFolder: " + baseFolder);

            //String imageAddress = subFolderName + file.getOriginalFilename();
            //System.out.println(imageAddress);
            // 组成长文件名

            // 写入
            String newFilePath = UUIDTool.changeUUIFileName(file, baseFolder);
            if (newFilePath != null) {
                FileUtils.writeByteArrayToFile(new File(newFilePath), file.getBytes());
            }

            // 保存到输出Json
            return feedbackJson(newFilePath);
        } catch (IOException e) {
            return feedbackErrorJson("Upload failed");
        }

    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void uploadToS3Test() {


    }

}
