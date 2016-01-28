package com.zy.webbrowser.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zy.webbrowser.R;


public class NotificationManagerUtil {

    public static final int MSG_NOTIFICATION_ID = 0x15;

	private static NotificationManager nm;
	public static NotificationManager getNotificationManager(Context context){
		if(nm == null){
			nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return nm;
	}

    public static void showCommon(Context context,int prograss){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.fag_noti);
            builder.setSmallIcon(R.mipmap.app_icon)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setOngoing(true)
                .setSound(null)
                .setContentTitle("正在下载")
                .setContentText("正在下载")
                .setContent(remoteViews);
            Notification notif = builder.build();
            notif.contentView.setProgressBar(R.id.pro_noti,100,prograss,false);
            notif.contentView.setTextViewText(R.id.tv_noti,"正在下载");
            NotificationManagerUtil.getNotificationManager(context).notify(MSG_NOTIFICATION_ID, notif);
    }

    public static void cancel(Context context){
           NotificationManagerUtil.getNotificationManager(context).cancel(MSG_NOTIFICATION_ID);
    }


}
