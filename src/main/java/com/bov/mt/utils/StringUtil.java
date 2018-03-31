package com.bov.mt.utils;

import com.bov.mt.exception.NameIsNotFileName;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class StringUtil {

    private StringUtil(){}

    //将文件名变为 当前时间戳.文件扩展名
    public static String transferFileName(String fileName){
        int index = fileName.lastIndexOf(".");
        if (index == 0) {
            throw new NameIsNotFileName("the name is not a file name");
        }
        //获取文件后缀名
        String suffix = fileName.substring(index + 1);
        //生成文件前缀
        UUID uuid = UUID.randomUUID();
        //拼接文件名
        return uuid+"."+suffix;
    }

    //将字符串替换为指定符号，值保留后四位
    public static String transerString(String origination ,String mark){
        StringBuilder sb = new StringBuilder();
        int len = origination.length();
        if (len <= 4) {
            return origination;
        }
        //先截取后四位
        String suffix = origination.substring(len - 4 );
        //将前面的替换为指定符号
        for (int i=0;i<len -4;i++) {
            sb.append(mark);
        }
        sb.append(suffix);
        return sb.toString();
    }
}
