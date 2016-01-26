package com.zy.webbrowser.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.zy.webbrowser.R;


public class ZyWebViewBrowserActivity extends ZyWebViewActivity implements ObservableScrollViewCallbacks {

    private static final String TAG = ZyWebViewBrowserActivity.class.getSimpleName();
    private Activity mAct = ZyWebViewBrowserActivity.this;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(this);
        initEvents();
        url = UrlEndoe("https://www.baidu.com/");
        handleBusiness();
    }

    private void initEvents(){
        mScrollable.setScrollViewCallbacks(this);
    }


    private void handleBusiness() {
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

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mTitleBar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mTitleBar) == -mTitleBar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-mTitleBar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
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
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mScrollable).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) mScrollable).requestLayout();
            }
        });
        animator.start();
    }




}