package com.wulala.awss3api.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class UUIDTool {

    public static String changeUUIFileName(MultipartFile file, String baseFolder) {
        String uuid = UUID.randomUUID().toString();
        String postPart = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
        if (postPart.equals("")) {
            return null;
        }
        String fullFilePath = baseFolder + uuid + "." + postPart;
        System.out.println("fullFilePath: " + fullFilePath);

        return fullFilePath;
    }
}
