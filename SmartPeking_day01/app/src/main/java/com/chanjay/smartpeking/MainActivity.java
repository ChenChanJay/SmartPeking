package com.chanjay.smartpeking;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chanjay.smartpeking.fragment.ContentFragment;
import com.chanjay.smartpeking.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu" ;
    private static final String FRAGMENT_CONTENT = "fragment_content" ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);  //设置侧边栏
        SlidingMenu slidingMenu = getSlidingMenu(); //获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); //设置全屏触摸
        slidingMenu.setBehindOffset(200); //设置预留屏幕的宽度

        initFragments();
    }

    /**
     *  初始化fragment 将fragment数据填充fragment布局文件
     */

    private void initFragments(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),FRAGMENT_LEFT_MENU);
        transaction.replace(R.id.fl_content,new ContentFragment(),FRAGMENT_CONTENT);

        transaction.commit();

    }

}
