package com.wulala.awss3api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ImageConfig {
    @Value("${image.folder.windows}")
    private String winImageFolder = "";

    @Value("${image.folder.linux}")
    private String linuxImageFolder = "";

    public String getImageFolder() {

        Properties props = System.getProperties();
        String osName = props.getProperty("os.name"); //操作系统名称
        //String osArch = props.getProperty("os.arch"); //操作系统构架
        //String osVersion = props.getProperty("os.version"); //操作系统版本
        //System.out.println(osName);
        if (osName.indexOf("Windows") > -1) {
            return winImageFolder;
        } else {
            return linuxImageFolder;
        }
    }
}
