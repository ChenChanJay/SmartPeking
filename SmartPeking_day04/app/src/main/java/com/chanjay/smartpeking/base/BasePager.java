package com.chanjay.smartpeking.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chanjay.smartpeking.MainActivity;
import com.chanjay.smartpeking.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 主页的5个子页面的基类
 */
public class BasePager {

    public Activity mActivity ;
    public View mRootView;
    public TextView tv_title ;
    public ImageButton btn_menu ;
    public FrameLayout fl_content ;
    public ImageButton btn_photo ; //组图切换按钮


    public BasePager(Activity activity){
        mActivity = activity ;
        initViews();
    }


    /**
     * 初始化布局
     */
    public void initViews(){
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        tv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        fl_content = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btn_menu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        btn_photo = (ImageButton) mRootView.findViewById(R.id.btn_photo);

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     */
    public void toggleSlidingMenu(){
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle(); //切换状态,显示时隐藏,隐藏时显示
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }

    /**
     * 设置侧边栏开启或关闭
     */
    protected void setSlideMenuEnable(boolean enable){

        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

}
