package com.bov.mt.utils;

import com.bov.mt.entity.mongo.MYItemInfo;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component("itemUtils")
public class ItemUtil {

    //处理我的办件时间
    public void dateToString(List<MYItemInfo> items){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        for (MYItemInfo item : items) {
            Date saveTime = item.getSaveTime();
            String time = sdf.format(saveTime);
            item.setTime(time);
        }
    }

    //处理单个时间
    public String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        return sdf.format(date);
    }
}
