package com.wulala.awss3api.controller;

import com.amazonaws.services.s3.transfer.Upload;
import com.wulala.awss3api.config.ImageConfig;
import com.wulala.awss3api.entity.JsonMsg;
import com.wulala.awss3api.entity.TempFileInfo;
import com.wulala.awss3api.service.UploadService;
import com.wulala.awss3api.utils.UUIDTool;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileUploadController extends BaseController {

    @Autowired
    UploadService uploadService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonMsg fileUpload(MultipartFile file, String userid) {
        long sysTick = new Date().getTime();
        try {
            System.out.println("userid: " + userid);
            TempFileInfo tempFileInfo = uploadService.upload(file, userid);
            String newFilePath = tempFileInfo.getTempFileFullPath();
            String newFileName = tempFileInfo.getTempFileName();
            if (uploadService.move2S3(newFilePath, userid)) {

                //删除暂存文件
                File tempFile = new File(newFilePath);
                if (tempFile.exists()) {
                    boolean deleteResult = tempFile.delete();
                    if(!deleteResult ){
                        return  feedbackErrorJson("Remove temp file failed");
                    }
                }
                // 保存到输出Json
                String outputMsg = newFileName + "," + "Request cost " + (new Date().getTime() - sysTick) + " mini seconds";

                return feedbackJson(outputMsg);

            } else {
                return feedbackErrorJson("Upload to S3 failed");
            }

        } catch (IOException e) {
            return feedbackErrorJson("Upload to temp folder failed");
        }
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void uploadToS3Test() {


    }

}
