package com.zy.webbrowser.serviceworker;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.zy.webbrowser.log.ZyLog;

import java.util.HashMap;

public class ServiceWorkerGlobalScope extends ServiceWeb {

    private static ServiceWorkerGlobalScope workerGlobalScope = null;
    private HashMap<String,String> callbackMap = new HashMap<String,String>();

    public static final String INSTALLKEY = "install";
    public static final String ACTIVATEKEY = "activate";
    public static final String FETCHKEY = "fetchkey";


    private ServiceWorkerGlobalScope(WebView webView){
        super(webView);
    }

    public static ServiceWorkerGlobalScope getWorkerGlobalScope(WebView webView){
        if(workerGlobalScope == null){
            workerGlobalScope = new ServiceWorkerGlobalScope(webView);
        }
        return workerGlobalScope;
    }

    @JavascriptInterface
    public void addEventListener(String state,String callback){
        if(TextUtils.isEmpty(state) || TextUtils.isEmpty(callback)){
            ZyLog.d("state or callback is invalid") ;
            return;
        }
        ZyLog.d(state + ":" + callback) ;
        if(INSTALLKEY.equals(state) || ACTIVATEKEY.equals(state) || FETCHKEY.equals(state)) {
            callbackMap.put(state, callback);
        }
    }

    //注册成功回调
    @JavascriptInterface
    public void registerSuccessCallBack(){
        postJavaScript("javascript:(" + addToCallBack(getCallbackMap().get(INSTALLKEY)) + ")()");
    }

    private String addToCallBack(String callback){
        return callback.substring(0,callback.length() - 1) + ";global.registerSuccessed();}";
    }

    //注册回调执行完成，进入激活
    @JavascriptInterface
    public void registerSuccessed(){
        postJavaScript("javascript:(" + getCallbackMap().get(ACTIVATEKEY) + ")()");
    }

    public HashMap<String, String> getCallbackMap() {
        return callbackMap;
    }
}
