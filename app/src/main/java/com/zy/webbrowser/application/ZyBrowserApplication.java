package com.zy.webbrowser.application;

import android.app.Application;
import com.liulishuo.filedownloader.FileDownloader;
import com.zy.webbrowser.util.ZyPrefs;

public class ZyBrowserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZyPrefs.getSp(this);
        FileDownloader.init(this);
    }
}
