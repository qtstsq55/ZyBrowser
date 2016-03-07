package com.zy.webbrowser.serviceworker;

import android.webkit.WebView;

public abstract class ServiceWeb {

    protected WebView webView;
    protected interface PostNext{
        public void onNext();
    };

    public ServiceWeb(WebView webView){
        this.webView = webView;
    }

    protected void postJavaScript(final String function){
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(function);
            }
        });
    }


}
