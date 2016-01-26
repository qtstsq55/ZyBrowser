package com.zy.webbrowser.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zy.webbrowser.R;

public class TitleBar extends Toolbar {

	private TextView btnTopLeft, btnTopRight, btnPopUp,btn_close;
	private TextView tvTopTitle;
	private View layoutTopTitle;
    private LinearLayout titlebarLayout;
	private Context context;

	public TitleBar(Context context) {
		super(context);
		this.context=context;
		LayoutInflater.from(context).inflate(R.layout.act_titlebar, this);
		setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		initLayout();
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		LayoutInflater.from(context).inflate(R.layout.act_titlebar, this);
        this.setContentInsetsRelative(0, 0);
		initLayout();
	}

	private void initLayout() {
		titlebarLayout = (LinearLayout) findViewById(R.id.titlebar_ly);
		btn_close=(TextView)findViewById(R.id.btn_close);
		btnTopLeft = (TextView) findViewById(R.id.btn_left);
		btnTopRight = (TextView) findViewById(R.id.btn_right);
		btnPopUp=(TextView) findViewById(R.id.btn_popup);
		tvTopTitle = (TextView) findViewById(R.id.tv_title);
		layoutTopTitle = findViewById(R.id.titlebar_rl_center);
	}

	public void setRightBtnTextColor(int color){
		btnTopRight.setTextColor(color);
	}

    public void setRightBtnTextTypeFace(int style) {
        btnTopRight.setTypeface(Typeface.DEFAULT, style);
    }
	public void setBtnClose(int Visible){
		btn_close.setVisibility(Visible);
	}

	public TextView getBtn_close() {
		return btn_close;
	}

	public void setTopTextColor(int color){
        tvTopTitle.setTextColor(color);
    }
	

	public void setLeftBtnText(String resid) {
		btnTopLeft.setCompoundDrawables(null, null, null, null);
		btnTopLeft.setText(resid);
	}
	
	public void setRightBtnText(String resid) {
		btnTopRight.setCompoundDrawables(null, null, null, null);
		btnTopRight.setText(resid);
	}
	public void setPopUpBtnText(String resid) {
		btnPopUp.setCompoundDrawables(null, null, null, null);
		btnPopUp.setText(resid);
	}
	
	public void setLeftBtnText(int resid) {
		btnTopLeft.setCompoundDrawables(null, null, null, null);
		btnTopLeft.setText(resid);
	}
	
	public void setRightBtnText(int resid) {
		btnTopRight.setCompoundDrawables(null, null, null, null);
		btnTopRight.setText(resid);
	}
	public void setPopUpBtnText(int resid) {
		btnPopUp.setCompoundDrawables(null, null, null, null);
		btnPopUp.setText(resid);
	}
	
	public void setLeftBtnIcon(int resid) {
		Drawable drawable= getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btnTopLeft.setCompoundDrawables(drawable, null, null, null);
		
		btnTopLeft.setText("");
		
	}
	
	public void setRightBtnIcon(int resid) {
		Drawable drawable= getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btnTopRight.setCompoundDrawables(null, null, drawable, null);
		
		btnTopRight.setText("");
		
	}
	public void setPopUpBtnIcon(int resid) {
		Drawable drawable= getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btnPopUp.setCompoundDrawables(null, null, drawable, null);
		btnPopUp.setText("");
		
	}

	public void changeLeftBtnText(String text) {
		btnTopLeft.setText(text);
	}

	public void setLeftBtnIconAndText(int resid, String text) {
		Drawable drawable= getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btnTopLeft.setCompoundDrawables(drawable, null, null, null);

		btnTopLeft.setText(text);

	}

	public void setRightBtnIconAndText(int resid, String text) {
		Drawable drawable= getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btnTopRight.setCompoundDrawables(null, null, drawable, null);

		btnTopRight.setText(text);

	}
	public void setPopUpBtnIconAndText(int resid, String text) {
		Drawable drawable= getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btnPopUp.setCompoundDrawables(null, null, drawable, null);
		btnPopUp.setText(text);

	}



    public TextView getTopTitleView() {
        return tvTopTitle;
    }
	
	
	public void setTopTitle(String title) {
		if(title != null) {
			title = title.trim();
		}
		tvTopTitle.setText(title);
	}

	public void setTopTitle(int resid) {
		tvTopTitle.setText(resid);
	}

	public String getTopTitle() {
		return tvTopTitle.getText().toString().trim();
	}
	
	public void setLeftBtnEnable(boolean enabled){
		btnTopLeft.setEnabled(enabled);
	}
	
	public void setRightBtnEnable(boolean enabled){
		btnTopRight.setEnabled(enabled);
	}
	public void setPopUpBtnEnable(boolean enabled){
		btnPopUp.setEnabled(enabled);
	}
	
	public void setLeftBtnStatus(int visibility) {
		btnTopLeft.setVisibility(visibility);
	}

	public void setRightBtnStatus(int visibility) {
		btnTopRight.setVisibility(visibility);

		if(visibility == View.INVISIBLE) {

			Drawable[] drawables = btnTopLeft.getCompoundDrawables();
			String text = btnTopLeft.getText().toString();

			btnTopRight.setCompoundDrawables(drawables[2], drawables[1], drawables[0], drawables[3]);
			btnTopRight.setText(text);

		}
	}
	public void setPopUpBtnStatus(int visibility) {
		btnPopUp.setVisibility(visibility);
		btnPopUp.clearAnimation();
	}

	public void setTopLeftClickListener(OnClickListener onClick) {
		btnTopLeft.setOnClickListener(onClick);
	}
	
	public void setTopRightClickListener(OnClickListener onClick) {
		btnTopRight.setOnClickListener(onClick);
	}
	public void setTopPopClickListener(OnClickListener onClick) {
		btnPopUp.setOnClickListener(onClick);		
	}

    public void setTitlebarBackground(int color){
		titlebarLayout.setBackgroundColor(color);
    }


	public void setTitleClickListener(OnClickListener onClick) {
		layoutTopTitle.setOnClickListener(onClick);
	}
	
	public void setTitleClickEnable(boolean enabled) {
		layoutTopTitle.setEnabled(enabled);
	}
	
	public View getTopLeftBtn() {
		return btnTopLeft;
	}
	public View getTopRightBtn() {
		return btnTopRight;
	}
	public View getPopUpBtn(){
		return btnPopUp;
	}

    public LinearLayout getTitlebarLayout() {
        return titlebarLayout;
    }
}
