package com.zy.webbrowser.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.zy.webbrowser.R;
import com.zy.webbrowser.util.AndroidUtils;
import com.zy.webbrowser.view.SparkController;
import com.zy.webbrowser.view.SparkPrograssBar;
import com.zy.webbrowser.web.ZyAppWebViewClient;
import com.zy.webbrowser.web.ZyWebViewChromeClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public abstract class ZyWebViewActivity extends BaseActivity  {

	public final static int MINIMUMFONTSIZE = 8;
    public final static int MINIMUMLOGICALFONTSIZE = 8;
    public final static int DEFAULTFIXEDFONTSIZE = 13;
    public final static int DEFAULTFONTSIZE = 16;
	private List<String> historyUrls = new ArrayList<String>();
	private String mLastUrl;//上次webView加载完成后的url
	private boolean isUserClicked = false; //是否用户对界面有点击
	protected WebView webview;
	protected ZyAppWebViewClient webviewClient;
	protected ZyWebViewChromeClient webviewChromeClient;
	protected SparkPrograssBar loadingProgressBar;
    protected SparkController sparkController;
    protected String titleName;
    protected Scrollable mScrollable;
    private int oldPrograss;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(getLayoutId());
        initScroll();
		initView();
        initEngine();
		initViewsEvent();
	}

    private void initScroll(){
        mScrollable = createScrollable();
    }

    private void initView(){
        webview = (WebView) findViewById(getWebViewId());
        loadingProgressBar=(SparkPrograssBar)findViewById(getLoadingProgressBar());
        webviewClient = this.getWebViewClient();
        sparkController = new SparkController();
        sparkController.setStartX(0);//组件左上角X坐标
        sparkController.setHeight(4);//进度条高度
        sparkController.setStartY(AndroidUtils.getActionBarSize(this) -sparkController.getHeight() );//组件左上角坐标
        sparkController.setStartColor(0f);//进度条初始颜色(红色)，HSV颜色模式
        sparkController.setEndColor(120f);//进度条结束颜色(绿色)，HSV颜色模式
        sparkController.setCurColor(sparkController.getStartColor());//进度条当前颜色,可有可无
//        sparkController.setWidth(720);//进度条宽度
        sparkController.setSpeed(1);//进度步长
        sparkController.setDelay(30);//刷新频率
        sparkController.setSparkCallBack(new SparkController.SparkCallBack() {
            @Override
            public void onStartPre() {
                //开始前回调
            }

            @Override
            public void onEnd() {
                //结束后回调
            }

            @Override
            public void onUpdate(float prograss) {
                //更新回调
            }
        });
        loadingProgressBar.setSparkController(sparkController);
    }

    protected void initEngine(){
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(webviewClient);
        String userAgent = AndroidUtils.getUserAgent(this,webview);
        webview.getSettings().setUserAgentString(userAgent);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setBackgroundColor(getResources().getColor(R.color.backgroud_1));
        webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        if(VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
            webview.getSettings().setDisplayZoomControls(false);
            //android webview组件包含3个隐藏的系统接口：“accessibility”和和“ccessibilityaversal”以及“searchBoxJavaBridge_”，同样会造成远程代码执行。
            webview.removeJavascriptInterface("accessibility");
            webview.removeJavascriptInterface("ccessibilityaversal");
            webview.removeJavascriptInterface("searchBoxJavaBridge_");
        }
        if(VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            //关闭自动播放，需要用户确认
            //三星S IV 4.2.2 不支持该方法
            try {
                webview.getSettings().setMediaPlaybackRequiresUserGesture(true);
            } catch (NoSuchMethodError e) {

            }

        }
        WebSettings webSettings = webview.getSettings();
        webSettings.setMinimumFontSize(MINIMUMFONTSIZE);
        webSettings.setMinimumLogicalFontSize(MINIMUMLOGICALFONTSIZE);
        webSettings.setDefaultFontSize(DEFAULTFONTSIZE);
        webSettings.setDefaultFixedFontSize(DEFAULTFIXEDFONTSIZE);
        //webSettings.setBuiltInZoomControls(true);
        /***打开本地缓存提供JS调用**/
        webSettings.setDomStorageEnabled(true);
        // Set cache size to 10 mb by default. should be more than enough
        webSettings.setAppCacheMaxSize(1024 * 1024 * 10);
        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins
        String lightAppPath = getApplicationContext().getDir("zybrowser", Context.MODE_PRIVATE).getPath();
		String appCachePath = lightAppPath + File.separator + "cache";
		webSettings.setAppCachePath(appCachePath);
		webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
//		//启用数据库
		webSettings.setDatabaseEnabled(true);
        String databasePath = lightAppPath + File.separator + "database";
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(databasePath);

    }

    public abstract String getCurrentUrl();

	public abstract int getWebViewId();

	public abstract int getLayoutId();

	public abstract int getLoadingProgressBar();

    public abstract boolean getSupprotLongClick();

    protected abstract Scrollable createScrollable();


    public WebView getWebview() {
		return webview;
	}


	public ZyAppWebViewClient getWebViewClient() {

		return new ZyAppWebViewClient(this);

	}

	public ZyWebViewChromeClient getWebViewChromeClient() {
		webviewChromeClient = new ZyWebViewChromeClient(this) {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
                System.out.println(newProgress);
				loadingProgressBar.setVisibility(View.VISIBLE);
                if(newProgress > oldPrograss){
                   oldPrograss = newProgress;
                }else{
                   stopPrograssBar();
                }
				if(oldPrograss > 0 && oldPrograss < 100){
                    startPrograssBar(100);
				}else if(oldPrograss >= 100){
                    stopPrograssBar();
                    loadingProgressBar.setVisibility(View.GONE);
                    resetPrograssBar();
				}
			}
		};
		return webviewChromeClient;
	}



    private void startPrograssBar(int prograss){
        loadingProgressBar.slidSprk(prograss);
    }

    private void stopPrograssBar(){
        loadingProgressBar.stopSlidSpak();
    }

    private void resetPrograssBar(){
        sparkController.getSparks().clear();
        sparkController.setCurPrograss(0);
    }



    @Override
    protected void initToolBar() {
        super.initToolBar();
        mTitleBar.setRightBtnIcon(R.drawable.selector_actionbar_refresh);
        mTitleBar.setTopRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.reload();
            }
        });
        mTitleBar.setTopLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    if (webviewClient != null) {
                        webviewClient.setOnKeyBackFlag(true);
                    }
                    if (webviewChromeClient != null && webviewChromeClient.getVideoView() != null) {
                        webviewChromeClient.onHideCustomView();
                    } else {
                        webview.goBack();
                    }
                } else {
                    finish();
                }
            }
        });
    }


	@Override
	public void onDestroy() {
		super.onDestroy();
		webviewClient.onDestroy();
		try {
			webview.stopLoading();
			ViewGroup parent = (ViewGroup) webview.getParent();
			if(parent != null)
				parent.removeView(webview);
//			webview.clearCache(true);
			webview.clearHistory();
			webview.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)  && this.webview.canGoBack()) {
	    	if(webviewClient != null){
	    		webviewClient.setOnKeyBackFlag(true);
	    	}
			if(webviewChromeClient !=null && webviewChromeClient.getVideoView() != null){
				webviewChromeClient.onHideCustomView();
				return true;
			}else{
				this.webview.goBack();
				return true;
			}
	    }
	    return super.onKeyDown(keyCode, event);
	}
	

	private void initViewsEvent() {

        if (!getSupprotLongClick()) {
            webview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
        //暂未自定义下载器
        webview.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                try {
                    Uri uri = Uri.parse(arg0);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

		webviewClient.setmLightAppListener(new ZyAppWebViewClient.ZyAppListener() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                int type = 0;
                oldPrograss = 0;
                WebView.HitTestResult hr = null;
                try {
                    hr = ((WebView) view).getHitTestResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (hr != null) {
                    type = hr.getType();//首次进入的页面type为0
                }
                if (isUserClicked && type > 0) {//点击进入新的页面
                    if (hr != null && mLastUrl != null) {
                        if (historyUrls.isEmpty() || !historyUrls.get(historyUrls.size() - 1).equals(mLastUrl)) {
                            historyUrls.add(mLastUrl);
                        }
                    }
                    isUserClicked = false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mLastUrl = url;
                initTitleName(webview);
            }
        });
		
		webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                isUserClicked = true;
                return false;
            }
        });
		
		webview.setWebChromeClient(getWebViewChromeClient());
	}

    protected  void initTitleName(WebView webView){
        titleName = webView.getTitle();
        if (TextUtils.isEmpty(titleName)) {
            titleName = "ZyBrowser";
        }
        mTitleBar.setTopTitle(titleName);
    }




}
