package com.zy.webbrowser.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.zy.webbrowser.R;
import com.zy.webbrowser.activity.ZyWebViewBrowserActivity;
import com.zy.webbrowser.model.WebSite;

import java.util.ArrayList;
import java.util.List;

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

    public static void setTranslucentStatus(Activity context,boolean on) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static int getStatusBarHeightBySdk(Activity activity) {
        int result = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return  result;
        }
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void gotoBrowserActivity(Context context,String url){
        Intent intent = new Intent(context, ZyWebViewBrowserActivity.class);
        intent.putExtra(ZyWebViewBrowserActivity.BROWSERURLKEY, url);
        context.startActivity(intent);
    }


    public static List<WebSite> getDefaultWebsites() {
        List<WebSite> list = new ArrayList<WebSite>();
        WebSite baidu = new WebSite();
        baidu.setDrawableId(R.mipmap.app_icon);
        baidu.setName("百度");
        baidu.setUrl("https://www.baidu.com/");

        WebSite xinlang = new WebSite();
        xinlang.setDrawableId(R.mipmap.app_icon);
        xinlang.setName("新浪");
        xinlang.setUrl("http://www.sina.com.cn/");

        WebSite xiecheng = new WebSite();
        xiecheng.setDrawableId(R.mipmap.app_icon);
        xiecheng.setName("携程");
        xiecheng.setUrl("http://www.ctrip.com/?utm_source=baidu&utm_medium=cpc&utm_campaign=baidu81&campaign=CHNbaidu81&adid=index&gclid=&isctrip=T");

        WebSite taobao = new WebSite();
        taobao.setDrawableId(R.mipmap.app_icon);
        taobao.setName("淘宝");
        taobao.setUrl("https://www.taobao.com/");

        WebSite yamaxun = new WebSite();
        yamaxun.setDrawableId(R.mipmap.app_icon);
        yamaxun.setName("亚马逊");
        yamaxun.setUrl("http://www.amazon.cn/?tag=baidhydrcnnv-23&ref=pz_ic_xmo_pp108");

        WebSite tongcheng = new WebSite();
        tongcheng.setDrawableId(R.mipmap.app_icon);
        tongcheng.setName("同城");
        tongcheng.setUrl("http://gz.58.com/?utm_source=market&spm=b-31580022738699-me-f-824.bdpz_biaoti");

        WebSite ganji = new WebSite();
        ganji.setDrawableId(R.mipmap.app_icon);
        ganji.setName("赶集");
        ganji.setUrl("http://gz.ganji.com/");

        WebSite meituan = new WebSite();
        meituan.setDrawableId(R.mipmap.app_icon);
        meituan.setName("美团");
        meituan.setUrl("http://www.meituan.com/cart/");

        WebSite yihaodian = new WebSite();
        yihaodian.setDrawableId(R.mipmap.app_icon);
        yihaodian.setName("一号店");
        yihaodian.setUrl("http://www.yhd.com/?tracker_u=2225501&type=3");

        WebSite suning = new WebSite();
        suning.setDrawableId(R.mipmap.app_icon);
        suning.setName("苏宁");
        suning.setUrl("http://www.suning.com/");

        WebSite youku = new WebSite();
        youku.setDrawableId(R.mipmap.app_icon);
        youku.setName("优酷");
        youku.setUrl("http://www.youku.com/");

        WebSite shouye = new WebSite();
        shouye.setDrawableId(R.mipmap.app_icon);
        shouye.setName("首页");
        shouye.setUrl("https://www.baidu.com/");

        list.add(baidu);
        list.add(xinlang);
        list.add(xiecheng);
        list.add(taobao);
        list.add(yamaxun);
        list.add(tongcheng);
        list.add(ganji);
        list.add(meituan);
        list.add(yihaodian);
        list.add(suning);
        list.add(youku);
        list.add(shouye);

        return  list;
    }

}
