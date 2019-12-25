package com.wulala.awss3api.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
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

    public TempFileInfo upload(MultipartFile file, String sn) throws IOException {
        String baseFolder = imageConfig.getImageFolder();

        String newFileName = UUIDTool.genFileName(sn) + "." + UUIDTool.getPostPart(file);

        String fullFilePath = baseFolder + newFileName;
        // 写入
        //String newFilePath = UUIDTool.changeUUIFileName(file, baseFolder, userid);
        TempFileInfo tempFileInfo = new TempFileInfo();
        tempFileInfo.setTempFileFullPath(fullFilePath);
        tempFileInfo.setTempFileName(newFileName);
        System.out.println(tempFileInfo);

        FileUtils.writeByteArrayToFile(new File(fullFilePath), file.getBytes());
        return tempFileInfo;
    }

    public boolean move2S3(String filePath, String userid) {

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new ProfileCredentialsProvider("default"))
                .build();

        String bucket_name = imageConfig.getBucketName();
        //String file_path = "d://dilation.jpg";
        String key_name = userid + "/" + Paths.get(filePath).getFileName().toString();

        System.out.format("Uploading %s to S3 bucket %s...\n", filePath, bucket_name);
        try {
            amazonS3.putObject(bucket_name, key_name, new File(filePath));
            //打开公网访问
            putObjectPublicAccess(amazonS3, bucket_name, key_name);

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            //System.exit(1);
            return false;
        }
        System.out.println("Done!");

        return true;
    }

    public void getObjectACL(String objKey) {

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new ProfileCredentialsProvider("default"))
                .build();
        String bucket_name = imageConfig.getBucketName();
        //String key = "dsssss/a900ab4f0fa09746fecfe8274665f684.jpg";
        //String key = "dsssss/a7644aa905b450a9911e3372d59a7909.jpg";

        try {
            AccessControlList acl = amazonS3.getObjectAcl(bucket_name, objKey);
            List<Grant> grants = acl.getGrantsAsList();
            for (Grant grant : grants) {
                System.out.format("%s: %s\n", grant.getGrantee().getIdentifier(),
                        grant.getPermission().toString());
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

    }

    public void putObjectPublicAccess(AmazonS3 amazonS3, String bucket_name, String objKey) {

        try {
            AccessControlList acl = amazonS3.getObjectAcl(bucket_name, objKey);
            GroupGrantee groupGrantee = GroupGrantee.parseGroupGrantee("http://acs.amazonaws.com/groups/global/AllUsers");

            Permission permission = Permission.parsePermission("READ");
            acl.grantPermission(groupGrantee, permission);
            amazonS3.setObjectAcl(bucket_name, objKey, acl);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }


}
