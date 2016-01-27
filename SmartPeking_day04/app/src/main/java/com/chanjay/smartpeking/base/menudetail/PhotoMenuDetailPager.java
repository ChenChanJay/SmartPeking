package com.chanjay.smartpeking.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanjay.smartpeking.R;
import com.chanjay.smartpeking.base.BaseMenuDetailPager;
import com.chanjay.smartpeking.domain.PhotosData;
import com.chanjay.smartpeking.global.GlobalContants;
import com.chanjay.smartpeking.utils.CacheUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * 菜单详情页——组图
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

    private ListView lv_photo;
    private GridView gv_photo;
    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private ImageButton btn_photo;

    public PhotoMenuDetailPager(Activity activity, ImageButton btn_photo) {
        super(activity);
        this.btn_photo = btn_photo;

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDisplay();
            }
        });
    }


    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);

        lv_photo = (ListView) view.findViewById(R.id.lv_photo);
        gv_photo = (GridView) view.findViewById(R.id.gv_photo);

        return view;
    }

    @Override
    public void initData() {

        String cache = CacheUtil.getCache(mActivity, GlobalContants.PHOTO_URL);

        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }

        getDataFromServer();

    }

    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, GlobalContants.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //设置缓存
                CacheUtil.setCache(mActivity, GlobalContants.PHOTO_URL, result);

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
        PhotosData data = gson.fromJson(result, PhotosData.class);

        mPhotoList = data.data.news;  //获取组图列表集合

        if (mPhotoList != null) {
            PhotoAdapter mAdapter = new PhotoAdapter();
            lv_photo.setAdapter(mAdapter);
            gv_photo.setAdapter(mAdapter);
        }
    }


    class PhotoAdapter extends BaseAdapter {

        private BitmapUtils utils;

        public PhotoAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotosData.PhotoInfo getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.list_photo_item, null);
                holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PhotosData.PhotoInfo item = getItem(position);

            holder.tv_title.setText(item.title);


            utils.display(holder.iv_pic, item.listimage);


            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView iv_pic;
        public TextView tv_title;
    }


    private boolean isListDisplay = true;  //是否是列表展示

    /**
     * 切换展现方式
     */
    private void changeDisplay() {

        if (isListDisplay) {
            isListDisplay = false;
            lv_photo.setVisibility(View.GONE);
            gv_photo.setVisibility(View.VISIBLE);
            btn_photo.setImageResource(R.mipmap.icon_pic_list_type);
        } else {
            isListDisplay = true;
            lv_photo.setVisibility(View.VISIBLE);
            gv_photo.setVisibility(View.GONE);
            btn_photo.setImageResource(R.mipmap.icon_pic_grid_type);
        }

    }


}
