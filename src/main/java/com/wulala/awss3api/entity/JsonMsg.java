package com.wulala.awss3api.entity;


//结果输出用的json实体类
public class JsonMsg implements java.io.Serializable {

    private static final long serialVersionUID = 4724639590165124376L;

    private boolean success = true;

    private String msg = "";

    private Object obj = null;

    private PageInfo pageInfo = null;

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
