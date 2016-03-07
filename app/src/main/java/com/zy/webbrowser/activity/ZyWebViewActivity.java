package com.zy.webbrowser.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.zy.webbrowser.R;
import com.zy.webbrowser.adapter.ZyBaseAdapter;
import com.zy.webbrowser.log.ZyLog;
import com.zy.webbrowser.model.SettingModel;
import com.zy.webbrowser.serviceworker.ServiceContainer;
import com.zy.webbrowser.serviceworker.ServiceWorkerGlobalScope;
import com.zy.webbrowser.util.AndroidUtils;
import com.zy.webbrowser.util.DialogUtil;
import com.zy.webbrowser.util.NotificationManagerUtil;
import com.zy.webbrowser.util.ZyKey;
import com.zy.webbrowser.util.ZyPrefs;
import com.zy.webbrowser.util.ZyUtil;
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
    protected ImageView im_title,im_back,im_forward;
	protected ZyAppWebViewClient webviewClient;
	protected ZyWebViewChromeClient webviewChromeClient;
	protected SparkPrograssBar loadingProgressBar;
    protected SparkController sparkController;
    protected String titleName;
    protected Scrollable mScrollable;
    private int oldPrograss;
    private List<SettingModel> settingModels;
    private ServiceContainer serviceContainer;
    private ServiceWorkerGlobalScope serviceWorkerGlobalScope;


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
        im_title = (ImageView) findViewById(getBottomTitleId());
        im_back = (ImageView) findViewById(getBottomBackId());
        im_forward = (ImageView) findViewById(getBottomForwardId());
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
		//启用数据库
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

    public abstract int getBottomTitleId();

    public abstract int getBottomBackId();

    public abstract int getBottomForwardId();



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
        final String savePath = getApplicationContext().getDir("zybrowser", Context.MODE_PRIVATE).getPath() + File.separator + "download";
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, final String contentDisposition, String mimetype, long contentLength) {
                FileDownloader.getImpl().create(url)
                        .setPath(savePath)
                        .setListener(new FileDownloadListener() {
                            @Override
                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            }

                            @Override
                            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                            }

                            @Override
                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                float prograss = soFarBytes / (float) totalBytes * 100f;
                                NotificationManagerUtil.showCommon(ZyWebViewActivity.this, (int) prograss);
                            }

                            @Override
                            protected void blockComplete(BaseDownloadTask task) {

                            }

                            @Override
                            protected void completed(BaseDownloadTask task) {
                                NotificationManagerUtil.cancel(ZyWebViewActivity.this);
                            }

                            @Override
                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            }

                            @Override
                            protected void error(BaseDownloadTask task, Throwable e) {
                            }

                            @Override
                            protected void warn(BaseDownloadTask task) {

                            }

                        }).start();
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
                ZyLog.d("onPageFinished: " + url);
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

        initServiceWorker();
		
		webview.setWebChromeClient(getWebViewChromeClient());

        im_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingModels = ZyUtil.getBottomSettings();
                ZyBaseAdapter<SettingModel> adapter = new ZyBaseAdapter<SettingModel>(ZyWebViewActivity.this,settingModels , R.layout.fag_setting_item) {
                    @Override
                    protected void dealObject(SettingModel model, ViewHolder viewHolder, int position, View view) {
                        ImageView im = (ImageView) viewHolder.getRootView().findViewById(R.id.setting_item_im);
                        TextView tv = (TextView) viewHolder.getRootView().findViewById(R.id.setting_item_tv);
                        im.setBackgroundResource(model.getDrawableId());
                        tv.setText(model.getTitle());
                    }
                };
                GridHolder holder = new GridHolder(4);
                OnItemClickListener onItemClickListener = new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        switch (position){
                            case 0:
                                AndroidUtils.getPicFromSDCard(ZyWebViewActivity.this);
                                break;
                        }
                    }
                };
                DialogUtil.showCompleteDialog(ZyWebViewActivity.this,holder, Gravity.BOTTOM,adapter,onItemClickListener,null,true);
                holder.getInflatedView().setPadding(0, 24, 0, 24);
            }
        });
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleBar.getTopLeftBtn().performClick();
            }
        });
        im_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview != null && webview.canGoForward()) {
                    webview.goForward();
                }
            }
        });
    }

    private void initServiceWorker(){
        serviceContainer = new ServiceContainer(webview);
        serviceWorkerGlobalScope = ServiceWorkerGlobalScope.getWorkerGlobalScope(webview);
        webview.addJavascriptInterface(serviceContainer, "container");
        webview.addJavascriptInterface(serviceWorkerGlobalScope, "global");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            if (requestCode == ZyKey.COVERSELECTKEY) {
                Uri uri = data.getData();
                ZyPrefs.putString(ZyKey.COVERKEY,AndroidUtils.getRealPathFromURI(ZyWebViewActivity.this, uri));
            }
        }
    }

    protected  void initTitleName(WebView webView){
        titleName = webView.getTitle();
        if (TextUtils.isEmpty(titleName)) {
            titleName = "ZyBrowser";
        }
        mTitleBar.setTopTitle(titleName);
    }

}
