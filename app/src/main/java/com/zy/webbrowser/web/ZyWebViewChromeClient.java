package com.zy.webbrowser.web;

import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.zy.webbrowser.R;
import com.zy.webbrowser.activity.ZyWebViewActivity;

public class ZyWebViewChromeClient extends WebChromeClient {

	private static String TAG = "ZyWebViewChromeClient";
	private View videoView ;
	private CustomViewCallback videoCallBack;
	private ViewGroup parentView;
	private ZyWebViewActivity activity;

	public ZyWebViewChromeClient(ZyWebViewActivity activity){
		super();
		this.activity = activity;
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,JsResult result) {
		if(message != null){
			Log.i(TAG, message);
		}
		return super.onJsAlert(view, url, message, result);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage cm) {
		Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
		return true;
	}

	public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
		super.onGeolocationPermissionsShowPrompt(origin, callback);
	}

	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		activity.getTitleBar().setVisibility(View.GONE);
		if(videoView != null){
			callback.onCustomViewHidden();
			return;
		}
		parentView = (ViewGroup) activity.findViewById(R.id.webview_layout_main);
		parentView.addView(view);
		videoView = view;
		videoCallBack = callback;
	}

	@Override
	public void onHideCustomView() {
		if(videoView == null){
			return;
		}
		activity.getTitleBar().setVisibility(View.VISIBLE);
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		parentView.removeView(videoView);
		videoView = null;
		videoCallBack.onCustomViewHidden();
	}


	public View getVideoView() {
		return videoView;
	}
}
