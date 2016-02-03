package com.zy.webbrowser.util;

import com.zy.webbrowser.R;
import com.zy.webbrowser.model.SettingModel;
import com.zy.webbrowser.model.WebSite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class ZyUtil {

    public static List<WebSite> getDefaultWebsites() {
        List<WebSite> list = new ArrayList<WebSite>();
        WebSite baidu = new WebSite();
        baidu.setDrawableId(R.mipmap.app_icon);
        baidu.setName("百度");
        baidu.setUrl("https://www.baidu.com/");

        WebSite xinlang = new WebSite();
        xinlang.setDrawableId(R.mipmap.app_icon);
        xinlang.setName("新浪");
        xinlang.setUrl("http://www.sina.com.cn/");

        WebSite xiecheng = new WebSite();
        xiecheng.setDrawableId(R.mipmap.app_icon);
        xiecheng.setName("携程");
        xiecheng.setUrl("http://www.ctrip.com/?utm_source=baidu&utm_medium=cpc&utm_campaign=baidu81&campaign=CHNbaidu81&adid=index&gclid=&isctrip=T");

        WebSite taobao = new WebSite();
        taobao.setDrawableId(R.mipmap.app_icon);
        taobao.setName("淘宝");
        taobao.setUrl("https://www.taobao.com/");

        WebSite yamaxun = new WebSite();
        yamaxun.setDrawableId(R.mipmap.app_icon);
        yamaxun.setName("亚马逊");
        yamaxun.setUrl("http://www.amazon.cn/?tag=baidhydrcnnv-23&ref=pz_ic_xmo_pp108");

        WebSite tongcheng = new WebSite();
        tongcheng.setDrawableId(R.mipmap.app_icon);
        tongcheng.setName("同城");
        tongcheng.setUrl("http://gz.58.com/?utm_source=market&spm=b-31580022738699-me-f-824.bdpz_biaoti");

        WebSite ganji = new WebSite();
        ganji.setDrawableId(R.mipmap.app_icon);
        ganji.setName("赶集");
        ganji.setUrl("http://gz.ganji.com/");

        WebSite meituan = new WebSite();
        meituan.setDrawableId(R.mipmap.app_icon);
        meituan.setName("美团");
        meituan.setUrl("http://www.meituan.com/cart/");

        WebSite yihaodian = new WebSite();
        yihaodian.setDrawableId(R.mipmap.app_icon);
        yihaodian.setName("一号店");
        yihaodian.setUrl("http://www.yhd.com/?tracker_u=2225501&type=3");

        WebSite suning = new WebSite();
        suning.setDrawableId(R.mipmap.app_icon);
        suning.setName("苏宁");
        suning.setUrl("http://www.suning.com/");

        WebSite youku = new WebSite();
        youku.setDrawableId(R.mipmap.app_icon);
        youku.setName("优酷");
        youku.setUrl("http://www.youku.com/");

        WebSite shouye = new WebSite();
        shouye.setDrawableId(R.mipmap.app_icon);
        shouye.setName("首页");
        shouye.setUrl("https://www.baidu.com/");

        list.add(baidu);
        list.add(xinlang);
        list.add(xiecheng);
        list.add(taobao);
        list.add(yamaxun);
        list.add(tongcheng);
        list.add(ganji);
        list.add(meituan);
        list.add(yihaodian);
        list.add(suning);
        list.add(youku);
        list.add(shouye);

        return  list;
    }


    public static List<SettingModel> getBottomSettings(){
        List<SettingModel> list = new ArrayList<SettingModel>();
        SettingModel coverModel = new SettingModel();
        coverModel.setDrawableId(R.mipmap.setting_cover);
        coverModel.setTitle("设置封面");

        SettingModel cacheModel = new SettingModel();
        cacheModel.setDrawableId(R.mipmap.app_icon);
        cacheModel.setTitle("清除缓存");

        list.add(coverModel);
        list.add(cacheModel);
        for(int i = 0; i<2; i++){
            SettingModel model = new SettingModel();
            model.setDrawableId(R.mipmap.app_icon);
            model.setTitle("我爱AV");
            list.add(model);
        }
        return  list;
    }

}
