package com.chanjay.smartpeking.utils;

import android.content.Context;

/**
 * 缓存的工具
 */
public class CacheUtil {

    /**
     * 设置缓存
     * key 是url, value是json
     */
    public static void setCache(Context ctx,String key,String value){
        PrefUtil.setString(ctx,key,value);
    }

    /**
     * 获取缓存的key是url
     */
    public static String getCache(Context ctx,String key){
        return PrefUtil.getString(ctx,key,null) ;
    }
}
