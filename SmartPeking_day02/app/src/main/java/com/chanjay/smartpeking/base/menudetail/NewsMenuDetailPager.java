package com.chanjay.smartpeking.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.print.PageRange;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.chanjay.smartpeking.R;
import com.chanjay.smartpeking.base.BaseMenuDetailPager;
import com.chanjay.smartpeking.base.TabDetailPager;
import com.chanjay.smartpeking.domain.NewsData;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 菜单详情页——新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager{

    private ViewPager mViewPager ;
    private ArrayList<TabDetailPager> mPagerList ;
    private ArrayList<NewsData.NewsTabData> mNewsTabData ;  //页签网络数据


    public NewsMenuDetailPager(Activity activity,ArrayList<NewsData.NewsTabData> children){
         super(activity);

         mNewsTabData = children ;
    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.news_menu_detail,null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);

        return view ;
    }

    @Override
    public void initData() {

          mPagerList = new ArrayList<TabDetailPager>() ;

        //初始化页签数据
         for (int i=0;i<mNewsTabData.size();i++){
             TabDetailPager pager = new TabDetailPager(mActivity,mNewsTabData.get(i));
             mPagerList.add(pager) ;
         }

          mViewPager.setAdapter(new MenuDetailAdapter());

    }

    class MenuDetailAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position) ;
            container.addView(pager.mRootView);
            pager.initData();
            return  pager.mRootView ;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
