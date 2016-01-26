package com.chanjay.smartpeking.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chanjay.smartpeking.base.BasePager;

/**
 * 主页面
 */
public class HomePager extends BasePager{

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlideMenuEnable(false); //关闭侧边栏

        btn_menu.setVisibility(View.GONE); //隐藏

        tv_title.setText("智慧北京");

        TextView tv = new TextView(mActivity);
        tv.setText("首页");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);

        fl_content.addView(tv);

    }
}
