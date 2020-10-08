package com.limaila.bms.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.LinkedHashMap;
import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/6/9 16:38
 @DESC: TODO 基于Fastjson的Utils
 @VERSION: 1.0
 ***/
public class JSONUtils {

    private JSONUtils() {
    }

    public static String toString(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T toBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }

    public static <T> List<T> toList(String jsonData, Class<T> clazz) {
        return JSON.parseArray(jsonData, clazz);
    }

    public static <T> LinkedHashMap<String, T> toMap(String jsonData) {
        return JSON.parseObject(jsonData, new TypeReference<LinkedHashMap<String, T>>() {
        });
    }

    public static JSONObject toJSONObject(String jsonData) {
        return JSON.parseObject(jsonData);
    }

    public static JSONArray toJSONArray(String jsonData) {
        return JSON.parseArray(jsonData);
    }


    public static boolean valid(String jsonData) {
        return JSON.isValid(jsonData);
    }

    public static boolean vaildArray(String jsonData) {
        return JSON.isValidArray(jsonData);
    }

    public static boolean vailObject(String jsonData) {
        return JSON.isValidObject(jsonData);
    }
}
