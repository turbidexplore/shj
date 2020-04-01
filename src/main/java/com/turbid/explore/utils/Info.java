package com.turbid.explore.utils;

import lombok.Data;

@Data
public class Info {

    public final static String SUCCESS="SUCCESS";

    public final static String ERROR="ERROR";

    //状态
    private Integer status;

    //消息
    private String message;

    //数据
    private Object data;

    public static Info SUCCESS(Object data){
        Info info=new Info();
        info.setStatus(200);
        info.setMessage(SUCCESS);
        info.setData(data);
        return info;
    }

    public static Info ERROR(String msg){
        Info info=new Info();
        info.setStatus(-1);
        info.setMessage(msg);
        info.setData(null);
        return info;
    }
}
