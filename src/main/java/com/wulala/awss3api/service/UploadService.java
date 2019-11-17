package com.wulala.awss3api.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.wulala.awss3api.config.ImageConfig;
import com.wulala.awss3api.entity.TempFileInfo;
import com.wulala.awss3api.utils.UUIDTool;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UploadService {

    @Resource
    ImageConfig imageConfig;

    public TempFileInfo upload(MultipartFile file, String userid) throws IOException {
        String baseFolder = imageConfig.getImageFolder();

        String newFileName = UUIDTool.genFileName(userid);

        String fullFilePath = baseFolder + newFileName + "." + UUIDTool.getPostPart(file);
        // 写入
        //String newFilePath = UUIDTool.changeUUIFileName(file, baseFolder, userid);
        TempFileInfo tempFileInfo = new TempFileInfo();
        tempFileInfo.setTempFileFullPath(fullFilePath);
        tempFileInfo.setTempFileName(newFileName);
        System.out.println(tempFileInfo);

        if (fullFilePath != null) {
            FileUtils.writeByteArrayToFile(new File(fullFilePath), file.getBytes());
        }
        return tempFileInfo;
    }

    public boolean move2S3(String filePath, String userid) {

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new ProfileCredentialsProvider("default"))
                .build();

//        List<Bucket> buckets = amazonS3.listBuckets();
//        for (Bucket b : buckets) {
//            System.out.println(b.getName());
//            //if(b.getName().equals("qiuyualbumdev"))
//        }
        String bucket_name = "ealbum-us-west2";
        //String file_path = "d://dilation.jpg";
        String key_name = userid + "/" + Paths.get(filePath).getFileName().toString();

        System.out.format("Uploading %s to S3 bucket %s...\n", filePath, bucket_name);
        try {
            amazonS3.putObject(bucket_name, key_name, new File(filePath));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            //System.exit(1);
            return false;
        }
        System.out.println("Done!");

        return true;
    }

}
