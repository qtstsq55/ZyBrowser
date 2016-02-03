package com.zy.webbrowser.util;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.zy.webbrowser.R;

/**
 * Created by Administrator on 2016/2/3.
 */
public class DialogUtil {

    public static void showCompleteDialog(Context context,Holder holder, int gravity, BaseAdapter adapter,
                                          OnItemClickListener itemClickListener,OnDismissListener dismissListener,
                                          boolean expanded) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setHeader(R.layout.fag_bottom_header)
                .setCancelable(true)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .setOnDismissListener(dismissListener)
                .setExpanded(expanded)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(R.color.popup_bg)
                .setContentBackgroundResource(R.color.backgroud_2)
                .create();
        dialog.show();
    }
}
