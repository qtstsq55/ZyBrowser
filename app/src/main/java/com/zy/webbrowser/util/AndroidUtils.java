package com.zy.webbrowser.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;
import android.webkit.WebView;

import com.zy.webbrowser.R;

public class AndroidUtils {

    public static String getVersionName(Context conText) {
        PackageManager pm = conText.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(conText.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "3.0.0";
    }

    public static String getUserAgent(Context context,WebView webView){
        return  "Zy/" + AndroidUtils.getVersionName(context)
                + ";" + "Android " + Build.VERSION.RELEASE + ";" + Build.BRAND
                + ";" + Build.MODEL + ";deviceName:" + Build.BRAND + " " + Build.MODEL
                + ";os:Android " + Build.VERSION.RELEASE
                + ";brand:" + Build.BRAND
                + ";model:" + Build.MODEL
                + webView.getSettings().getUserAgentString();
    }

    public static int getActionBarSize(Context context) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

}
