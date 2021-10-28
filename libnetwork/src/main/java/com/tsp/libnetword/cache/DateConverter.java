package com.tsp.libnetword.cache;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 时间转换
 * version: 1.0
 */
public class DateConverter {
    @TypeConverter
    public static Long date2Long(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date long2Date(Long data) {
        return new Date(data);
    }
}
