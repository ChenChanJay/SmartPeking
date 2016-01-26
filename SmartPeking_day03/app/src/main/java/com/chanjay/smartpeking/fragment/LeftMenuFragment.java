package com.chanjay.smartpeking.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chanjay.smartpeking.MainActivity;
import com.chanjay.smartpeking.R;
import com.chanjay.smartpeking.base.impl.NewsCenterPager;
import com.chanjay.smartpeking.domain.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 侧边栏
 */
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lv_list ;
    private ArrayList<NewsData.NewsMenuData> mMenuList;
    private int mCurrentPos ;
    private MenuAdapter menuAdapter;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);

        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {

        //给ListView设置监听事件
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                menuAdapter.notifyDataSetChanged(); //ListView的适配器的getView方法会重新走一遍

                setCurrentMenuDetailPager(position);

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
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position){
        MainActivity mainUI = (MainActivity) mActivity;
        ContentFragment contentFragment = mainUI.getContentFragment();
        NewsCenterPager pager = contentFragment.getNewsCenterPager();
        pager.setCurrentMenuDetailPager(position);

    }


    //设置网络数据
    public void setMenuData(NewsData data){
        System.out.println("侧边栏拿到数据"+data);

        mMenuList = data.data;

        menuAdapter = new MenuAdapter();

        lv_list.setAdapter(menuAdapter);
    }

    /**
     * 侧边栏数据适配器
     */
    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public NewsData.NewsMenuData getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(mActivity, R.layout.list_menu_item, null);

            TextView tv_titile = (TextView) view.findViewById(R.id.tv_title);

            NewsData.NewsMenuData newsMenuData = getItem(position);

            tv_titile.setText(newsMenuData.title);

            if(mCurrentPos == position){ //判断当前绘制的view是否被选中
                //显示红色
                tv_titile.setEnabled(true);
            }else{
                //显示白色
                tv_titile.setEnabled(false);
            }

            return view;
        }

    }
}
