package com.wulala.awss3api;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.wulala.awss3api.service.UploadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UploadTest {

    @Resource
    UploadService uploadServer;

    @Test
    public void contextLoads() {
    }

    //@Test
    public void upload2S3Test() {

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new ProfileCredentialsProvider("default"))
                .build();

        List<Bucket> buckets = amazonS3.listBuckets();
        for (Bucket b : buckets) {
            System.out.println(b.getName());
            //if(b.getName().equals("qiuyualbumdev"))
        }
        String bucket_name = "ealbum-us-west2";
        String file_path = "d://dilation.jpg";
        String key_name = Paths.get(file_path).getFileName().toString();

        System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
        try {
            amazonS3.putObject(bucket_name, key_name, new File(file_path));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");

    }

    //@Test
    public void testGetACL() {
        //uploadServer.getObjectACL();

    }

    //@Test
    public void testSetACL() {
        String objStr = "dsssss/a7644aa905b450a9911e3372d59a7909.jpg";
        //uploadServer.putObjectPublicAccess(objStr);
        //uploadServer.getObjectACL(objStr);
    }

}
