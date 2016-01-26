package com.chanjay.smartpeking.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chanjay.smartpeking.domain.NewsData;

/**
 * 页签详情页面
 */
public class TabDetailPager extends BaseMenuDetailPager{

    private NewsData.NewsTabData mTabData ;
    private TextView tvText;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData ;
    }

    @Override
    public View initView() {
        tvText = new TextView(mActivity);
        tvText.setText("页签详情页");
        tvText.setTextColor(Color.RED);
        tvText.setTextSize(22);
        tvText.setGravity(Gravity.CENTER);

        return tvText;
    }

    @Override
    public void initData() {

        tvText.setText(mTabData.title);
    }
}
