package com.wulala.awss3api.controller;

import com.amazonaws.services.s3.transfer.Upload;
import com.wulala.awss3api.config.ImageConfig;
import com.wulala.awss3api.entity.JsonMsg;
import com.wulala.awss3api.entity.TempFileInfo;
import com.wulala.awss3api.service.UploadService;
import com.wulala.awss3api.utils.UUIDTool;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileUploadController extends BaseController {

    @Autowired
    UploadService uploadService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonMsg fileUpload(MultipartFile file, String sn) {
        long sysTick = new Date().getTime();
        try {
            System.out.println("sn: " + sn);
            TempFileInfo tempFileInfo = uploadService.upload(file, sn);
            String newFilePath = tempFileInfo.getTempFileFullPath();
            String newFileName = tempFileInfo.getTempFileName();
            return movetoS3(newFilePath, sn, newFileName);


        } catch (IOException e) {
            return feedbackErrorJson("Upload to temp folder failed");
        }
    }

    @PostMapping(value = "/moveToS3")
    public JsonMsg movetoS3(String filePath, String sn, String newFileName) {
        long sysTick = new Date().getTime();
        if (uploadService.move2S3(filePath, sn)) {

            //删除暂存文件
            File tempFile = new File(filePath);
            if (tempFile.exists()) {
                boolean deleteResult = tempFile.delete();
                if (!deleteResult) {
                    return feedbackErrorJson("Remove temp file failed");
                }
            }
            // 保存到输出Json
            String cost = "Request cost " + (new Date().getTime() - sysTick) + " mini seconds";
            return feedbackJson(newFileName, cost);

        } else {
            return feedbackErrorJson("Upload to S3 failed");
        }

    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void uploadToS3Test() {


    }

}
