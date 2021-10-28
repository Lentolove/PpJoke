package com.tsp.libnetword;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 将 Json 格式数据装成 JavaBean 对象
 * version: 1.0
 */
public class JsonConvert implements Convert {
    //默认的Json转 Java Bean的转换器
    @Override
    public Object convert(String response, Type type) {
        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object data1 = data.get("data");
            return JSON.parseObject(data1.toString(), type);
        }
        return null;
    }
}
