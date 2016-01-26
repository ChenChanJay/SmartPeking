package com.chanjay.smartpeking.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.chanjay.smartpeking.base.BasePager;

/**
 * 政务页面
 */
public class GovAffairPager extends BasePager{

    public GovAffairPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlideMenuEnable(true); //开启侧边栏

        tv_title.setText("人口管理");

        TextView tv = new TextView(mActivity);
        tv.setText("政务");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);

        fl_content.addView(tv);

    }
}
