package com.zy.webbrowser.application;

import android.app.Application;
import com.liulishuo.filedownloader.FileDownloader;

public class ZyBrowserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(this);
    }
}
