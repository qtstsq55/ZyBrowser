package com.zy.webbrowser.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zy.webbrowser.R;
import com.zy.webbrowser.view.TitleBar;

import java.net.URLDecoder;

public class BaseActivity extends AppCompatActivity {

    protected TitleBar mTitleBar;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    protected void initActionBar(Activity activity){
        mTitleBar = (TitleBar) activity.findViewById(R.id.titlebar);
        if(mTitleBar != null) {
            setSupportActionBar(mTitleBar);
            initToolBar();
        }
    }

    protected void initToolBar() {
        mTitleBar.setTopTitle("");
        mTitleBar.setLeftBtnIcon(R.drawable.selector_actionbar_back);
        mTitleBar.setTopLeftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    protected String UrlEndoe(String webviewUrl){
        String url = "";
        try {
            url = URLDecoder.decode(webviewUrl);
        } catch (Exception e) {
            url = webviewUrl;
        }
        return  url;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_to_right_in, R.anim.left_to_right_out);
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

}
