package com.zy.webbrowser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;

import com.zy.webbrowser.R;

public class StartActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        handBussiness();
    }


    private void handBussiness(){
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(StartActivity.this,SelectBaseActivity.class));
                        finish();
                    }
                });
            }
        }, 1200);
    }


    @Override
    protected void initToolBar() {
        super.initToolBar();
        mTitleBar.setVisibility(View.GONE);
    }
}
