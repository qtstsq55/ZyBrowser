package com.zy.webbrowser.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/12/19.
 */
public class ZyPrefs {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor et;

    public static SharedPreferences getSp(Context context){
        if(sp == null){
           sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        et = sp.edit();
        return  sp;
    }

    public static void putString(String key,String value){
       et.putString(key,value);
       et.commit();
    }

    public static String getString(String key,String defaultValue){
        return  sp.getString(key,defaultValue);
    }

    public static void putBoolean(String key,boolean value){
        et.putBoolean(key, value);
        et.commit();
    }

    public static boolean getBoolean(String key,boolean defaultValue){
        return  sp.getBoolean(key, defaultValue);
    }

    public static void putInt(String key,int value){
        et.putInt(key,value);
        et.commit();
    }

    public static int getInt(String key,int defaultValue){
        return  sp.getInt(key,defaultValue);
    }




}
