package com.zy.webbrowser.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.zy.webbrowser.R;


public class ZyWebViewBrowserActivity extends ZyWebViewActivity implements ObservableScrollViewCallbacks {

    public static final String  BROWSERURLKEY = "browserurlkey";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(this);
        initDatas();
        initEvents();
        handleBusiness();
    }

    private void initDatas(){
        url =  UrlEndoe(getIntent().getStringExtra(BROWSERURLKEY));
    }

    private void initEvents(){
        mScrollable.setScrollViewCallbacks(this);
        im_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                if(webview!= null && webview.canGoForward()){
                    webview.goForward();
                }
            }
        });
    }


    protected void handleBusiness() {
        if(TextUtils.isEmpty(url)){
            Toast.makeText(this,"链接无效",Toast.LENGTH_SHORT).show();
            return;
        }
        this.webview.loadUrl(url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public String getCurrentUrl() {
        return url;
    }

    @Override
    public int getWebViewId() {
        return R.id.webview;
    }

    @Override
    public int getLoadingProgressBar() {
        return R.id.webview_progressbar;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_browser;
    }


    @Override
    public boolean getSupprotLongClick() {
        return false;
    }

    @Override
    protected Scrollable createScrollable() {
        ObservableWebView webView = (ObservableWebView) findViewById(getWebViewId());
        return webView;
    }

    @Override
    public int getBottomTitleId() {
        return R.id.im_bottom_title;
    }

    @Override
    public int getBottomBackId() {
        return R.id.im_bottom_back;
    }

    @Override
    public int getBottomForwardId() {
        return R.id.im_bottom_forward;
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Log.e("DEBUG", "onUpOrCancelMotionEvent: " + scrollState);
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    protected boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mTitleBar) == 0;
    }

    protected boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mTitleBar) == -mTitleBar.getHeight();
    }

    protected void showToolbar() {
        moveToolbar(0);
    }

    protected void hideToolbar() {
        moveToolbar(-mTitleBar.getHeight());
    }

    protected void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mTitleBar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mTitleBar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mTitleBar, translationY);
                ViewHelper.setTranslationY((View) mScrollable, translationY);
            }
        });
        animator.start();
    }




}
