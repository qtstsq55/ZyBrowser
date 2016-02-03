package com.zy.webbrowser.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.zy.webbrowser.R;
import com.zy.webbrowser.activity.ZyWebViewBrowserActivity;
import com.zy.webbrowser.model.WebSite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AndroidUtils {

    public static final String IMAGE_UNKNOW = "image/*";
    public static final String CAMERA_PATH = "/" + "我爱AV" + "/";

    public static String getVersionName(Context conText) {
        PackageManager pm = conText.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(conText.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "3.0.0";
    }

    public static String getUserAgent(Context context,WebView webView){
        return  "Zy/" + AndroidUtils.getVersionName(context)
                + ";" + "Android " + Build.VERSION.RELEASE + ";" + Build.BRAND
                + ";" + Build.MODEL + ";deviceName:" + Build.BRAND + " " + Build.MODEL
                + ";os:Android " + Build.VERSION.RELEASE
                + ";brand:" + Build.BRAND
                + ";model:" + Build.MODEL
                + webView.getSettings().getUserAgentString();
    }

    public static int getActionBarSize(Context context) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public static void setTranslucentStatus(Activity context,boolean on) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static int getStatusBarHeightBySdk(Activity activity) {
        int result = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return  result;
        }
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void gotoBrowserActivity(Context context,String url){
        Intent intent = new Intent(context, ZyWebViewBrowserActivity.class);
        intent.putExtra(ZyWebViewBrowserActivity.BROWSERURLKEY, url);
        context.startActivity(intent);
    }

    public static boolean chekSDCardExist(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static void getPicFromSDCard(Activity act) {
        if (chekSDCardExist()) {
            Intent intentPhoto = new Intent(Intent.ACTION_PICK);
            intentPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNKNOW);
            intentPhoto.putExtra("return-data", false);
            act.startActivityForResult(intentPhoto, ZyKey.COVERSELECTKEY);
        } else {
            Toast.makeText(act, "没有sd卡", Toast.LENGTH_LONG);
        }
    }

    public static File createCameraFile(Context context) {
        return createMediaFile(context,CAMERA_PATH);
    }

    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private static File createMediaFile(Context context,String parentPath){
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED)?Environment.getExternalStorageDirectory():context.getCacheDir();
        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        File tmpFile = new File(folderDir,getPhotoFileName());
        return tmpFile;
    }

    public static String getRealPathFromURI(Context act, Uri contentUri) {
        String imagePath = "";
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = act.getContentResolver().query(contentUri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        if (cursor == null || cursor.getCount() == 0) {
            if (cursor != null) {
                cursor.close();
            }
            return imagePath;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);
        cursor.close();
        return imagePath;
    }


}
