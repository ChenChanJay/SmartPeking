package com.chanjay.smartpeking.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.chanjay.smartpeking.R;
import com.chanjay.smartpeking.base.BasePager;
import com.chanjay.smartpeking.base.impl.GovAffairPager;
import com.chanjay.smartpeking.base.impl.HomePager;
import com.chanjay.smartpeking.base.impl.NewsCenterPager;
import com.chanjay.smartpeking.base.impl.SettingPager;
import com.chanjay.smartpeking.base.impl.SmartServicePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 主页内容
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_group)
    private RadioGroup rg_group;

    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;

    private ArrayList<BasePager> mArrayList;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
//        rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        ViewUtils.inject(this, view); //注入view和事件

        return view;
    }

    @Override
    public void initData() {
        rg_group.check(R.id.rb_home); //默认勾选首页

        mArrayList = new ArrayList<BasePager>();

        //初始化5个子页面
        /*for (int i=0;i<5;i++){
            BasePager basePager = new BasePager(mActivity);
            mArrayList.add(basePager) ;
        }*/
        mArrayList.add(new HomePager(mActivity));
        mArrayList.add(new NewsCenterPager(mActivity));
        mArrayList.add(new SmartServicePager(mActivity));
        mArrayList.add(new GovAffairPager(mActivity));
        mArrayList.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new MyAdapter());

        //监听RadioGroup点击事件
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0,false); //切换页面,false取消切换动画效果
                        break;

                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1,false);
                        break;

                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2,false);
                        break;

                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3,false);
                        break;

                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4,false);
                        break;

                    default:
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mArrayList.get(position).initData();  //获取当前被选中的页面,初始化该页面数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

         //手动初始化首页数据
        mArrayList.get(0).initData();

    }

    /**
     * 获取新闻中心页面
     * @return
     */
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mArrayList.get(1);
    }


    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mArrayList.get(position);
            container.addView(pager.mRootView);
//            pager.initData();   初始化数据…… 不要在此处初始化数据,否则会预加载下一个页面
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
