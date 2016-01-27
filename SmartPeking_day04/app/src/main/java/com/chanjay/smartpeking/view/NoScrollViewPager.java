package com.chanjay.smartpeking.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能左右滑动的ViewPager
 */
public class NoScrollViewPager extends ViewPager{

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //表示事件是否拦截,返回false表示不拦截,可以让嵌套在内部的ViewPager响应左右滑的事件
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false ;
    }

    /**
     * 重新onTouchEvent方法
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false ;
    }
}
