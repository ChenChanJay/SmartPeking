package com.chanjay.smartpeking.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chanjay.smartpeking.base.BaseMenuDetailPager;

/**
 * 菜单详情页——专题
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager{

    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        TextView tv = new TextView(mActivity);
        tv.setText("菜单详情页——专题");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }


}
