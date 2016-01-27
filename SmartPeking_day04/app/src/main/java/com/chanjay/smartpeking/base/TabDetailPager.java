package com.chanjay.smartpeking.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanjay.smartpeking.NewsDetailActivity;
import com.chanjay.smartpeking.R;
import com.chanjay.smartpeking.domain.NewsData;
import com.chanjay.smartpeking.domain.TabData;
import com.chanjay.smartpeking.global.GlobalContants;
import com.chanjay.smartpeking.utils.CacheUtil;
import com.chanjay.smartpeking.utils.PrefUtil;
import com.chanjay.smartpeking.view.RefreshListView;
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
    private RefreshListView mListView;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;  //头条新闻的标题

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator; //头条新闻位置指示器

    private BitmapUtils utils;

    private ArrayList<TabData.TopNewsData> mTopNewsList; //头条新闻数据集合

    private ArrayList<TabData.TabNewsData> mNewsList;  //新闻数据集合
    private String mMoreUrl;  //更多页面的地址
    private NewsAdapter newsAdapter;
    private Handler mHandler ;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);

        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        //将头条新闻以头布局的形式加给listview
        mListView.addHeaderView(headerView);

        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadingMore() {

                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
                    mListView.onRefreshComplete(false);  //收起加载更多的布局
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("被点击:" + position);

                //在本地记录已读状态
                String ids = PrefUtil.getString(mActivity,"read_ids","") ;
                String readId = mNewsList.get(position).id ;

                if(!ids.contains(readId)){
                    ids = ids + readId + ",";
                    PrefUtil.setString(mActivity,"read_ids",ids);
                }

//                newsAdapter.notifyDataSetChanged();
                  changeReadState(view);  //实现局部界面更新,这个view就是被点击的item布局对象

                //跳转新闻详情页
                Intent intent = new Intent() ;
                intent.setClass(mActivity, NewsDetailActivity.class) ;
                intent.putExtra("url",mNewsList.get(position).url) ;
                mActivity.startActivity(intent);
            }
        });

        return view;
    }

    /**
     * 改变已读新闻标题的颜色
     * @param view
     */
    private void changeReadState(View view){
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setTextColor(Color.GRAY);
    }

    @Override
    public void initData() {

        String cache = CacheUtil.getCache(mActivity, mUrl);

        if(!TextUtils.isEmpty(cache)){
            parseData(cache,false);
        }

        getDataFromServer();
    }

    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("页签详情页返回结果:" + result);

                //设置缓存
                CacheUtil.setCache(mActivity,mUrl,result);

                parseData(result, false);

                mListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mListView.onRefreshComplete(false);
            }
        });
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;

                parseData(result, true);

                mListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mListView.onRefreshComplete(false);
            }
        });
    }

    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);


        String more = mTabDetailData.data.more;

        //处理下一页链接
        if (!TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalContants.SERVER_URL + more;
        } else {
            mMoreUrl = null;
        }


        if (!isMore) {
            mTopNewsList = mTabDetailData.data.topnews;
            mNewsList = mTabDetailData.data.news;

            if (mTopNewsList != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true); //支持快照显示
                mIndicator.setOnPageChangeListener(this);
                mIndicator.onPageSelected(0); //让指示器重新定位到第一个点

                tv_title.setText(mTopNewsList.get(0).title);
            }

            if (mNewsList != null) {
                //填充新闻列表数据
                newsAdapter = new NewsAdapter();
                mListView.setAdapter(newsAdapter);
            }

            //自动轮播条显示
            if(mHandler == null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {

                        int currentItem = mViewPager.getCurrentItem() ;

                        if(currentItem<mTopNewsList.size()-1){
                            currentItem ++ ;
                        }else{
                             currentItem = 0 ;
                        }

                        mViewPager.setCurrentItem(currentItem);  //切换到下一个页面
                        mHandler.sendEmptyMessageDelayed(0, 3000) ;
                    }
                };

                mHandler.sendEmptyMessageDelayed(0,3000) ;  //延迟3秒后发消息,形成循环
            }

        }else{ //如果是加载下一页,需要将数据追加给原来的集合
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news ;
            mNewsList.addAll(news) ;
            newsAdapter.notifyDataSetChanged();
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
    class NewsAdapter extends BaseAdapter {


        private BitmapUtils utils;

        public NewsAdapter() {

            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
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

            ViewHolder holder;

            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder = new ViewHolder();
                holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item = getItem(position);
            holder.tv_title.setText(item.title);
            holder.tv_date.setText(item.pubdate);

            utils.display(holder.iv_pic, item.listimage);

            String ids = PrefUtil.getString(mActivity,"read_ids","") ;

            if(ids.contains(getItem(position).id)){
                holder.tv_title.setTextColor(Color.GRAY);
            }else{
                holder.tv_title.setTextColor(Color.BLACK);
            }


            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView iv_pic;
        public TextView tv_title;
        public TextView tv_date;
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

            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            utils.display(image, topNewsData.topimage);  //

            container.addView(image);


            image.setOnTouchListener(new TopNewsTouchListener());

            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 头条新闻的触摸监听
     */
    class TopNewsTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN :
                    mHandler.removeCallbacksAndMessages(null); //删除handler中的所有消息
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP :
                    mHandler.sendEmptyMessageDelayed(0,3000);
                    break;

                default:
                    break;

            }

            return true;
        }
    }
}
