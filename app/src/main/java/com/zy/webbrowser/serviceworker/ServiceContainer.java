package com.zy.webbrowser.serviceworker;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.zy.webbrowser.log.ZyLog;

public class ServiceContainer extends ServiceWeb {

    private String injecJs;
    public ServiceContainer(WebView webView){
        super(webView);
    }

    @JavascriptInterface
    public void register(String jspath,String scope){
        if(TextUtils.isEmpty(jspath) || TextUtils.isEmpty(scope)){
            ZyLog.d("jspath or scope is invalid") ;
            return;
        }
        ZyLog.d("register called " + jspath + "   "+ scope);
        injecJs = "var injectscript = document.createElement(\"script\");";
        injecJs += "injectscript.src=" + "\"" + jspath + "\"" +";";
        //手动添加注册回调
        injecJs += "injectscript.onload = function(){global.registerSuccessCallBack();};";
        injecJs += "document.body.appendChild(injectscript);";
        postJavaScript("javascript:" + injecJs);
    }

}
