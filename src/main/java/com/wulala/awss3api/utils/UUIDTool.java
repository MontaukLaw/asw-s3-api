package com.wulala.awss3api.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

public class UUIDTool {

    public static String genFileName(String userId) {
        String uuid = UUID.randomUUID().toString();

        //加密方式
        String algorithmName = "md5";


        //加密轮数
        int hashIterations = 2;

        //计算
        SimpleHash newFilePrefix = new SimpleHash(algorithmName, uuid, userId, hashIterations);
        System.out.println(newFilePrefix);

        return newFilePrefix.toString();

    }

    public static String getPostPart(MultipartFile file) {

        String postPart = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
        if (postPart.equals("")) {
            return null;
        }
        return postPart;
    }

    public static String changeUUIFileName(MultipartFile file, String baseFolder, String userid) {


        String postPart = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
        if (postPart.equals("")) {
            return null;
        }

        String newFileName = genFileName(userid);

        String fullFilePath = baseFolder + genFileName(userid) + "." + postPart;
        System.out.println("fullFilePath: " + fullFilePath);

        return newFileName;
    }


}
