package cn.itcast.zz.zhbj20.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by wangdh on 2016/11/30.
 * sp的工具类
 */
public class SpUtils {
    /**
     * 获取缓存
     * @param context
     * @param key  -- json数据的url
     * @return
     */
    public static String getCache(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("cache",context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    /**
     * 设置缓存
     * @param context
     * @param key  -- url
     * @param value  -- jsonString
     */
    public static void setCache(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("cache",context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    /**
     * 存储新闻id
     * @param context
     * @param newsId
     */
    public static void setReadNewsIds(Context context, int newsId) {
        SharedPreferences sp = context.getSharedPreferences("cache",context.MODE_PRIVATE);
        //获取
        String readNewsIds = sp.getString("readNewsIds", "");
        //如果有
        if(!TextUtils.isEmpty(readNewsIds)){
            readNewsIds += "#"+newsId;
        }else{
            //如果没有
            readNewsIds = String.valueOf(newsId);
        }
        sp.edit().putString("readNewsIds",readNewsIds).commit();
    }

    public static String getReadNewsIds(Context context) {
        SharedPreferences sp = context.getSharedPreferences("cache",context.MODE_PRIVATE);
        return sp.getString("readNewsIds","");
    }


}
