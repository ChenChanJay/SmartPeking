package com.chanjay.smartpeking.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanjay.smartpeking.R;
import com.chanjay.smartpeking.domain.NewsData;
import com.chanjay.smartpeking.domain.TabData;
import com.chanjay.smartpeking.global.GlobalContants;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * 页签详情页面
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {

    private NewsData.NewsTabData mTabData;
    private TextView tvText;
    private String mUrl;
    private TabData mTabDetailData;

    @ViewInject(R.id.vp_news)
    private ViewPager mViewPager;

    @ViewInject(R.id.lv_list)
    private ListView mListView;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;  //头条新闻的标题

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator; //头条新闻位置指示器

    private BitmapUtils utils;

    private ArrayList<TabData.TopNewsData> mTopNewsList; //头条新闻数据集合

    private ArrayList<TabData.TabNewsData> mNewsList;  //新闻数据集合

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        //加载头布局
        View headerView = View.inflate(mActivity,R.layout.list_header_topnews,null) ;

        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        //将头条新闻以头布局的形式加给listview
        mListView.addHeaderView(headerView);

        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("页签详情页返回结果:" + result);

                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);

        mTopNewsList = mTabDetailData.data.topnews;
        mNewsList = mTabDetailData.data.news;

        if(mTopNewsList!=null){
            mViewPager.setAdapter(new TopNewsAdapter());
            mIndicator.setViewPager(mViewPager);
            mIndicator.setSnap(true); //支持快照显示
            mIndicator.setOnPageChangeListener(this);
            mIndicator.onPageSelected(0); //让指示器重新定位到第一个点

            tv_title.setText(mTopNewsList.get(0).title);
        }

        if(mNewsList!=null){
            //填充新闻列表数据
            NewsAdapter newsAdapter = new NewsAdapter();
            mListView.setAdapter(newsAdapter);
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        String title = mTopNewsList.get(position).title;
        tv_title.setText(title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 新闻列表的适配器
     */
    class NewsAdapter extends BaseAdapter{


        private BitmapUtils utils;

        public NewsAdapter() {

            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.pic_item_list_default) ;
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder ;

            if(convertView==null){
                convertView = View.inflate(mActivity, R.layout.list_news_item, null) ;
                holder = new ViewHolder() ;
                holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);

                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item = getItem(position) ;
            holder.tv_title.setText(item.title);
            holder.tv_date.setText(item.pubdate);

            utils.display(holder.iv_pic,item.listimage);



            return convertView;
        }
    }

    static class ViewHolder{
        public ImageView iv_pic ;
        public TextView tv_title ;
        public TextView tv_date ;
    }


    /**
     * 头条新闻viewpager的适配器
     */
    class TopNewsAdapter extends PagerAdapter {

        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.topnews_item_default); //设置默认加载的图片
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setScaleType(ImageView.ScaleType.FIT_XY); //基于控件大小填充图片
            container.addView(image);

            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            String url = topNewsData.topimage;
            utils.display(image, url);  //
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
