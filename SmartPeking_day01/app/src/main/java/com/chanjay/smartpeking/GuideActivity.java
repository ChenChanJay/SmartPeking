package com.chanjay.smartpeking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chanjay.smartpeking.utils.PrefUtil;

import java.util.ArrayList;

import static android.view.ViewGroup.*;

/**
 * 引导页
 */
public class GuideActivity extends Activity {

    private static final int[] mImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private ViewPager vp_guide;
    private Button btn_start;
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout ll_point_group;
    private int mPointWidth; //圆点间的距离
    private View view_red_point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        btn_start = (Button) findViewById(R.id.btn_start);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        view_red_point = findViewById(R.id.view_red_point);

        initViews();

        btn_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp,表示已展示引导页
                PrefUtil.setBoolean(GuideActivity.this,"isGuideShowed",true);
                //跳转主页面
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new GuideListner());
    }

    private void initViews() {

        mImageViewList = new ArrayList<ImageView>();

        //初始化引导页的3个页面
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(image);
        }

        //初始化引导页的小圆点
        for (int i = 0; i < mImageIds.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,10);

            if (i > 0) {
                params.leftMargin = 10; //设置圆点间隔
            }

            point.setLayoutParams(params);  //设置圆点的大小
            ll_point_group.addView(point); //将圆点添加给线性布局
        }

        //获取视图树
        ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当layout执行结束后调用此方法
            @Override
            public void onGlobalLayout() {
                System.out.println("layout结束");
                //动态获取两个圆点间的距离
                ll_point_group.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointWidth = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
                System.out.println("小圆点之间的距离:" + mPointWidth);
            }
        });

    }

    /**
     * ViewPager数据适配器
     */
    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }

    }


    /**
     * viewpager的监听器
     */
    private class GuideListner implements ViewPager.OnPageChangeListener {


        //滑动的事件
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            System.out.println("当前位置:" + position + ";百分比:" + positionOffset + ";移动距离:" + positionOffsetPixels);

            int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;
            //获取当前红点的布局参数
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_red_point.getLayoutParams();
            params.leftMargin = len;  //设置左边距

            view_red_point.setLayoutParams(params);  //重新给小红点设置布局参数
        }

        //某个页面被选中
        @Override
        public void onPageSelected(int position) {

            if (position == mImageIds.length - 1) {
                btn_start.setVisibility(View.VISIBLE);
            } else {
                btn_start.setVisibility(View.INVISIBLE);
            }
        }

        //滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
