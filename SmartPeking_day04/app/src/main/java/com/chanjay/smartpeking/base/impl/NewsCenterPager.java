package com.chanjay.smartpeking.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chanjay.smartpeking.MainActivity;
import com.chanjay.smartpeking.base.BaseMenuDetailPager;
import com.chanjay.smartpeking.base.BasePager;
import com.chanjay.smartpeking.base.menudetail.InteractMenuDetailPager;
import com.chanjay.smartpeking.base.menudetail.NewsMenuDetailPager;
import com.chanjay.smartpeking.base.menudetail.PhotoMenuDetailPager;
import com.chanjay.smartpeking.base.menudetail.TopicMenuDetailPager;
import com.chanjay.smartpeking.domain.NewsData;
import com.chanjay.smartpeking.fragment.LeftMenuFragment;
import com.chanjay.smartpeking.global.GlobalContants;
import com.chanjay.smartpeking.utils.CacheUtil;
import com.chanjay.smartpeking.utils.PrefUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * 新闻中心
 */
public class NewsCenterPager extends BasePager{

    private ArrayList<BaseMenuDetailPager> mPager;
    private NewsData mNewsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlideMenuEnable(true); //开启侧边栏
        tv_title.setText("新闻");

        String cache = CacheUtil.getCache(mActivity, GlobalContants.CATEGROIES_URL);

        if(!TextUtils.isEmpty(cache)){  //如果缓存存在,直接解析数据,无需访问网络
            parseData(cache);
        }

           getDataFromServer(); //不管有没有缓存,都获取最新数据,保证数据最新

    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils() ;

       //使用xutlis发送请求
        utils.send(HttpMethod.GET, GlobalContants.CATEGROIES_URL, new RequestCallBack<String>() {

            //访问成功
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果:" + result);

                //设置缓存
                CacheUtil.setCache(mActivity,GlobalContants.CATEGROIES_URL,result);

                parseData(result);
            }
            //访问失败
            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) ;
    }

    /**
     * 解析网络数据
     * @param result
     */
    private void parseData(String result) {

        Gson gson = new Gson() ;

        mNewsData = gson.fromJson(result, NewsData.class);

        System.out.println("解析结果:"+ mNewsData);

        //刷新侧边栏的数据
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        //准备4个菜单详情页
        mPager = new ArrayList<BaseMenuDetailPager>();
        mPager.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mPager.add(new TopicMenuDetailPager(mActivity));
        mPager.add(new PhotoMenuDetailPager(mActivity,btn_photo));
        mPager.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuDetailPager(0); //设置菜单详情页——新闻为默认当前页

    }


    /**
     * 设置当前详情页
     */
    public void setCurrentMenuDetailPager(int position){
        BaseMenuDetailPager pager = mPager.get(position); //获取当前显示的菜单详情页
        fl_content.removeAllViews(); //清除之前的布局
        fl_content.addView(pager.mRootView);

        //设置当前页标题
        tv_title.setText(mNewsData.data.get(position).title);

        pager.initData(); //初始化当前页的数据

        if(pager instanceof PhotoMenuDetailPager){
            btn_photo.setVisibility(View.VISIBLE);
        }else{
            btn_photo.setVisibility(View.GONE);
        }

    }


}
