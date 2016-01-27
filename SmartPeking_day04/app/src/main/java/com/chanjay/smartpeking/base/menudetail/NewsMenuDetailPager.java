package com.chanjay.smartpeking.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chanjay.smartpeking.MainActivity;
import com.chanjay.smartpeking.R;
import com.chanjay.smartpeking.base.BaseMenuDetailPager;
import com.chanjay.smartpeking.base.TabDetailPager;
import com.chanjay.smartpeking.domain.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove.OnPageChangeListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;


/**
 * 菜单详情页——新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ArrayList<TabDetailPager> mPagerList;
    private ArrayList<NewsData.NewsTabData> mNewsTabData;  //页签网络数据
    private TabPageIndicator mIndicator;


    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);

        mNewsTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);

        ViewUtils.inject(this, view);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);

        //初始化自定义控件TabPageIndicator
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);

//        mViewPager.setOnPageChangeListener(this); 当viewpager和indicator绑定时,滑动监听需要设置给indicator而不是viewpager

        mIndicator.setOnPageChangeListener(this);

        return view;
    }

    @Override
    public void initData() {

        mPagerList = new ArrayList<TabDetailPager>();

        //初始化页签数据
        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabData.get(i));
            mPagerList.add(pager);
        }

        mViewPager.setAdapter(new MenuDetailAdapter());
        mIndicator.setViewPager(mViewPager); //将viewpager与viewpagerindicator关联在一起,必须在viewpager设置adapter完才能调用

    }

    @OnClick(R.id.btn_next)
    public void nextPage(View view) {
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("onPageSelected");

        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if (position == 0) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MenuDetailAdapter extends PagerAdapter {

        /**
         * 重写该方法.返回页面标题,用于viewpagerindicator页签显示
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
