
package com.zy.webbrowser.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zy.webbrowser.R;
import com.zy.webbrowser.util.AndroidUtils;

public class SelectBaseActivity extends ZyWebViewActivity implements ObservableScrollViewCallbacks {

    private static final String STATE_SLIDING_STATE = "slidingState";
    private static final int SLIDING_STATE_TOP = 0;
    private static final int SLIDING_STATE_MIDDLE = 1;
    private static final int SLIDING_STATE_BOTTOM = 2;

    private View mHeader;
    private View mHeaderBar;
    private TextView mTitle;
    private TextView mToolbarTitle;
    private View mImageView;
    private TouchInterceptionFrameLayout mInterceptionLayout;

    private int mActionBarSize;
    private int mIntersectionHeight;
    private int mHeaderBarHeight;
    private int mSlidingSlop;
    private int mColorPrimary;
    private int mFlexibleSpaceImageHeight;

    private int mSlidingState;
    private boolean mMoved;
    private float mInitialY;
    private float mMovedDistanceY;
    private float mScrollYOnDownMotion;
    private ObservableWebView webView;
    private boolean mHeaderColorIsChanging;
    private boolean mHeaderColorChangedToBottom;
    private boolean mHeaderIsAtBottom;
    private boolean mHeaderIsNotAtBottom;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(this);
        initDatas();
        initViews();
        initEvents();
        if (savedInstanceState == null) {
            mSlidingState = SLIDING_STATE_BOTTOM;
        }
        url = UrlEndoe("https://www.baidu.com/");
        webView.loadUrl(url);
    }

    private void initDatas(){
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mIntersectionHeight = getResources().getDimensionPixelSize(R.dimen.intersection_height);
//        mSlidingSlop = getResources().getDimensionPixelSize(R.dimen.sliding_slop);
        mActionBarSize = AndroidUtils.getActionBarSize(this);
        mSlidingSlop = mHeaderBarHeight = mActionBarSize;
        mColorPrimary = getResources().getColor(R.color.guide_fc5);
    }

    private void initViews(){
        mHeader = findViewById(R.id.header);
        mHeaderBar = findViewById(R.id.header_bar);
        mImageView = findViewById(R.id.select_image);
        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.scroll_wrapper);
        mTitle = (TextView) findViewById(R.id.title);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
    }

    private void initEvents(){
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideOnClick();
            }
        });
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        mTitle.setText(mTitleBar.getTopTitle());
        mToolbarTitle.setText(mTitleBar.getTopTitle());
        ViewHelper.setAlpha(mToolbarTitle, 0);
        ViewHelper.setTranslationY(mTitle, (mHeaderBarHeight - mActionBarSize) / 2);
        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                changeSlidingState(mSlidingState, false);
            }
        });
    }

    @Override
    public String getCurrentUrl() {
        return url;
    }

    @Override
    public int getWebViewId() {
        return R.id.select_webview;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select;
    }

    @Override
    public int getLoadingProgressBar() {
        return R.id.select_progressbar;
    }

    @Override
    public boolean getSupprotLongClick() {
        return false;
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        ViewHelper.setScaleY(mTitleBar, 0);
        mTitleBar.setTitlebarBackground(Color.TRANSPARENT);
        mTitleBar.setTopTitle("Welcome");
    }

    @Override
    protected void initTitleName(WebView webView) {
//        super.initTitleName(webView);
    }

    protected Scrollable createScrollable() {
        webView = (ObservableWebView) findViewById(getWebViewId());
        webView.setScrollViewCallbacks(this);
        return webView;
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSlidingState = savedInstanceState.getInt(STATE_SLIDING_STATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SLIDING_STATE, mSlidingState);
        super.onSaveInstanceState(outState);
    }


    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            final int minInterceptionLayoutY = -mIntersectionHeight;
            Rect fabRect = new Rect();
            if (fabRect.contains((int) ev.getX(), (int) ev.getY())) {
                return false;
            } else {
                return minInterceptionLayoutY < (int) ViewHelper.getY(mInterceptionLayout)|| (moving && mScrollable.getCurrentScrollY() - diffY < 0);
            }
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
            mScrollYOnDownMotion = mScrollable.getCurrentScrollY();
            mInitialY = ViewHelper.getTranslationY(mInterceptionLayout);
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            mMoved = true;
            float translationY = ViewHelper.getTranslationY(mInterceptionLayout) - mScrollYOnDownMotion + diffY;
            if (translationY < -mIntersectionHeight) {
                translationY = -mIntersectionHeight;
            } else if (getScreenHeight() - mHeaderBarHeight < translationY) {
                translationY = getScreenHeight() - mHeaderBarHeight;
            }

            slideTo(translationY, true);

            mMovedDistanceY = ViewHelper.getTranslationY(mInterceptionLayout) - mInitialY;
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            if (!mMoved) {
                Rect outRect = new Rect();
                mHeader.getHitRect(outRect);
                if (outRect.contains((int) ev.getX(), (int) ev.getY())) {
                    slideOnClick();
                }
            } else {
                stickToAnchors();
            }
            mMoved = false;
        }
    };

    private void changeSlidingState(final int slidingState, boolean animated) {
        mSlidingState = slidingState;
        float translationY = 0;
        switch (slidingState) {
            case SLIDING_STATE_TOP:
                translationY = 0;
                break;
            case SLIDING_STATE_MIDDLE:
                translationY = getAnchorYImage();
                break;
            case SLIDING_STATE_BOTTOM:
                translationY = getAnchorYBottom();
                break;
        }
        if (animated) {
            slideWithAnimation(translationY);
        } else {
            slideTo(translationY, false);
        }
    }

    private void slideOnClick() {
        float translationY = ViewHelper.getTranslationY(mInterceptionLayout);
        if (translationY == getAnchorYBottom()) {
            changeSlidingState(SLIDING_STATE_MIDDLE, true);
        } else if (translationY == getAnchorYImage()) {
            changeSlidingState(SLIDING_STATE_BOTTOM, true);
        }
    }

    private void stickToAnchors() {
        if (0 < mMovedDistanceY) {
            if (mSlidingSlop < mMovedDistanceY) {
                if (getAnchorYImage() < ViewHelper.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_BOTTOM, true);
                } else {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                }
            } else {
                if (getAnchorYImage() < ViewHelper.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                } else {
                    changeSlidingState(SLIDING_STATE_TOP, true);
                }
            }
        } else if (mMovedDistanceY < 0) {
            if (mMovedDistanceY < -mSlidingSlop) {
                if (getAnchorYImage() < ViewHelper.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                } else {
                    changeSlidingState(SLIDING_STATE_TOP, true);
                }
            } else {
                if (getAnchorYImage() < ViewHelper.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_BOTTOM, true);
                } else {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                }
            }
        }
    }

    private void slideTo(float translationY, final boolean animated) {
        ViewHelper.setTranslationY(mInterceptionLayout, translationY);

        if (translationY < 0) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
            lp.height = (int) -translationY + getScreenHeight();
            mInterceptionLayout.requestLayout();
        }

        float hiddenHeight = translationY < 0 ? -translationY : 0;
        ViewHelper.setTranslationY(mTitle, Math.min(mIntersectionHeight, (mHeaderBarHeight + hiddenHeight - mActionBarSize) / 2));

        float imageAnimatableHeight = getScreenHeight() - mHeaderBarHeight;
        float imageTranslationScale = imageAnimatableHeight / (imageAnimatableHeight - mImageView.getHeight());
        float imageTranslationY = Math.max(0, imageAnimatableHeight - (imageAnimatableHeight - translationY) * imageTranslationScale);
        ViewHelper.setTranslationY(mImageView, imageTranslationY);

        if (animated) {
            ViewPropertyAnimator.animate(mTitleBar).scaleY(0).setDuration(200).start();
        } else {
            ViewHelper.setScaleY(mTitleBar, 0);
        }

        if (ViewHelper.getTranslationY(mInterceptionLayout) <= mFlexibleSpaceImageHeight) {
            if (animated) {
                ViewPropertyAnimator.animate(mTitleBar).scaleY(1).setDuration(200).start();
            } else {
                ViewHelper.setScaleY(mTitleBar, 1);
            }
            mTitleBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.guide_fc5)));
        }

        changeToolbarTitleVisibility();
        changeHeaderBarColorAnimated(animated);
    }

    private void slideWithAnimation(float toY) {
        float layoutTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
        if (layoutTranslationY != toY) {
            ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mInterceptionLayout), toY).setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    slideTo((float) animation.getAnimatedValue(), true);
                }
            });
            animator.start();
        }
    }

    private void changeToolbarTitleVisibility() {
        if (ViewHelper.getTranslationY(mInterceptionLayout) <= mIntersectionHeight) {
            if (ViewHelper.getAlpha(mToolbarTitle) != 1) {
                ViewPropertyAnimator.animate(mToolbarTitle).cancel();
                ViewPropertyAnimator.animate(mToolbarTitle).alpha(1).setDuration(200).start();
            }
        } else if (ViewHelper.getAlpha(mToolbarTitle) != 0) {
            ViewPropertyAnimator.animate(mToolbarTitle).cancel();
            ViewPropertyAnimator.animate(mToolbarTitle).alpha(0).setDuration(200).start();
        } else {
            ViewHelper.setAlpha(mToolbarTitle, 0);
        }
    }

    private void changeHeaderBarColorAnimated(boolean animated) {
        if (mHeaderColorIsChanging) {
            return;
        }
        boolean shouldBeWhite = getAnchorYBottom() == ViewHelper.getTranslationY(mInterceptionLayout);
        if (!mHeaderIsAtBottom && !mHeaderColorChangedToBottom && shouldBeWhite) {
            mHeaderIsAtBottom = true;
            mHeaderIsNotAtBottom = false;
            if (animated) {
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(100);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float alpha = (float) animation.getAnimatedValue();
                        mHeaderColorIsChanging = (alpha != 1);
                        changeHeaderBarColor(alpha);
                    }
                });
                animator.start();
            } else {
                changeHeaderBarColor(1);
            }
        } else if (!mHeaderIsNotAtBottom && !shouldBeWhite) {
            mHeaderIsAtBottom = false;
            mHeaderIsNotAtBottom = true;
            if (animated) {
                ValueAnimator animator = ValueAnimator.ofFloat(1, 0).setDuration(100);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float alpha = (float) animation.getAnimatedValue();
                        mHeaderColorIsChanging = (alpha != 0);
                        changeHeaderBarColor(alpha);
                    }
                });
                animator.start();
            } else {
                changeHeaderBarColor(0);
            }
        }
    }

    private void changeHeaderBarColor(float alpha) {
        mHeaderBar.setBackgroundColor(ScrollUtils.mixColors(mColorPrimary, Color.WHITE, alpha));
        mTitle.setTextColor(ScrollUtils.mixColors(Color.WHITE, Color.BLACK, alpha));
        mHeaderColorChangedToBottom = (alpha == 1);
    }


    private float getAnchorYBottom() {
        return getScreenHeight() - mHeaderBarHeight;
    }

    private float getAnchorYImage() {
        return mImageView.getHeight();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
