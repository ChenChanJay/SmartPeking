package com.chanjay.smartpeking.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.chanjay.smartpeking.base.BasePager;

/**
 * 智慧服务页面
 */
public class SmartServicePager extends BasePager{

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {

        setSlideMenuEnable(true); //开启侧边栏

        tv_title.setText("生活");

        TextView tv = new TextView(mActivity);
        tv.setText("智慧服务");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);

        fl_content.addView(tv);

    }
}
