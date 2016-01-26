package com.chanjay.smartpeking.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chanjay.smartpeking.base.BaseMenuDetailPager;

/**
 * 菜单详情页——组图
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager{

    public PhotoMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        TextView tv = new TextView(mActivity);
        tv.setText("菜单详情页——组图");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }


}
