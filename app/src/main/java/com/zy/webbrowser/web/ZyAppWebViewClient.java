package com.zy.webbrowser.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zy.webbrowser.activity.ZyWebViewActivity;

public class ZyAppWebViewClient extends WebViewClient {

	protected WebView webview;
	protected ZyWebViewActivity lightAppActivity;
	private ZyAppListener mLightAppListener;
	private String reDirectedUrl = null;
	private boolean isFirstUrl = true;
	private String mFirstUrl = "";
	private boolean mOnKeyBack = false;//是否返回键


	public ZyAppWebViewClient(ZyWebViewActivity context) {
		this.lightAppActivity = context;
		this.webview = this.lightAppActivity.getWebview();
	}

	@Override
	public void onLoadResource(WebView view, final String url) {
		super.onLoadResource(view, url);
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if(url != null && url.equals(reDirectedUrl) && mFirstUrl.equals(view.getUrl()) && mOnKeyBack){
			view.stopLoading();
			this.lightAppActivity.finish();
			mOnKeyBack = false;
			return true;//关闭页面不加载
		}
		mOnKeyBack = false;
		if (reDirectedUrl == null) {
			reDirectedUrl = url;
		}
		if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") || url.startsWith("mailto:")) { 
			try{
	            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
	            lightAppActivity.startActivity(intent); 
			}catch (Exception e){
			}
			return true;
	    }else{
	    	return super.shouldOverrideUrlLoading(view, url);
	    }
		
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		if(isFirstUrl){
			mFirstUrl = url;
			isFirstUrl = false;
		}
		super.onPageStarted(view, url, favicon);
		if(mLightAppListener != null){
			mLightAppListener.onPageStarted(view, url, favicon);
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		mOnKeyBack = false;
		if(mLightAppListener != null){
			mLightAppListener.onPageFinished(view, url);
		}
	}

    public void onDestroy() {

    }
	
	public ZyAppListener getmLightAppListener() {
		return mLightAppListener;
	}

	public void setmLightAppListener(ZyAppListener mLightAppListener) {
		this.mLightAppListener = mLightAppListener;
	}

	public interface ZyAppListener {
		public void onPageStarted(WebView view, String url, Bitmap favicon);
		public void onPageFinished(WebView view, String url);
	}

	public void setOnKeyBackFlag(boolean flag){
		mOnKeyBack = flag;
	}


}
