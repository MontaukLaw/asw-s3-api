package com.wulala.awss3api.entity;

public class TempFileInfo {

    private String tempFileName;
    private String tempFileFullPath;

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public String getTempFileFullPath() {
        return tempFileFullPath;
    }

    public void setTempFileFullPath(String tempFileFullPath) {
        this.tempFileFullPath = tempFileFullPath;
    }

    @Override
    public String toString() {
        return "TempFileInfo{" +
                "tempFileName='" + tempFileName + '\'' +
                ", tempFileFullPath='" + tempFileFullPath + '\'' +
                '}';
    }
}
