package com.zy.webbrowser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public abstract  class ZyBaseAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private int itemLayoutId;

    public ZyBaseAdapter(Context context, List<T> list, int itemLayoutId){
            this.context=context;
            this.list=list;
            this.itemLayoutId=itemLayoutId;
    }

    public void setDataSource(List<T> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list!=null){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(list!=null){
            return  list.get(i);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    protected  abstract void dealObject(T item,ViewHolder viewHolder,int position,View view);

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view==null){
            view= LayoutInflater.from(context).inflate(itemLayoutId,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            dealObject(list.get(i),viewHolder, i, view);
        }else{
            viewHolder = (ViewHolder) view.getTag();
            dealObject(list.get(i),viewHolder,i,view);
        }
        return  view;
    }


    public class  ViewHolder {
        private View rootView;
        public ViewHolder(View view){
            rootView = view;
        }
        public View getRootView(){
            return  rootView;
        }
    }

}
