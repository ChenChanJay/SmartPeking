package com.chanjay.smartpeking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chanjay.smartpeking.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的listview
 */
public class RefreshListView extends ListView implements OnScrollListener,OnItemClickListener{

    private static final int STATE_PULL_REFRESH = 0;  //下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;  //松开刷新
    private static final int STATE_REFRESHING = 2;  //正在刷新

    private View mHeaderView;

    private ImageView iv_arr;
    private TextView tv_title;
    private TextView tv_time;
    private ProgressBar pb_progress;

    private int startY = -1;
    private int mHeaderViewHeight;

    private int mCurrentState = STATE_PULL_REFRESH; //当前状态
    private Animation animUp;
    private Animation animDown;

    public OnRefreshListener mListener;
    private int mFooterViewHeight;
    private View mFooterView;


    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {

        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);

        iv_arr = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
        pb_progress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

        this.addHeaderView(mHeaderView);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();

        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);  //隐藏头布局

        initArrowAnim();

        tv_time.setText("最后刷新时间:" + getCurrentTime());
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView(){

        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);

        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);

        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);  //隐藏

        this.setOnScrollListener(this);
    }

    private boolean isLoadingMore ;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(scrollState==SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING){

            if(getLastVisiblePosition() == getCount()-1 && !isLoadingMore){ //滑动到最后
                System.out.println("到底......");

                mFooterView.setPadding(0,0,0,0);  //显示

                setSelection(getCount()-1);  //改变listview显示位置

                isLoadingMore = true ;

                if(mListener!=null){
                    mListener.onLoadingMore();
                }
            }

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }



    /**
     * 滑动事件处理
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getRawY();
                }
                if (mCurrentState == STATE_REFRESHING) {  //正在刷新时不做处理
                    break;
                }
                int endY = (int) ev.getRawY();

                int dy = endY - startY;  //移动偏移量

                if (dy > 0 && getFirstVisiblePosition() == 0) {  //只有下拉并且第一个item,才允许下拉
                    int padding = dy - mHeaderViewHeight;  //计算padding
                    mHeaderView.setPadding(0, padding, 0, 0); //设置当前padding

                    if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {  //状态改为松开刷新
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    }else if (padding < 0 && mCurrentState != STATE_PULL_REFRESH) {   //状态改为下拉刷新
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1; //重置startY

                if (mCurrentState == STATE_RELEASE_REFRESH) {
                    mCurrentState = STATE_REFRESHING; //正在刷新
                    mHeaderView.setPadding(0, 0, 0, 0); //显示
                    refreshState();
                } else if (mCurrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0); //隐藏
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 刷新状态
     */
    private void refreshState() {

        switch (mCurrentState) {

            case STATE_PULL_REFRESH:
                tv_title.setText("下拉刷新");
                iv_arr.setVisibility(View.VISIBLE);
                pb_progress.setVisibility(View.INVISIBLE);
                iv_arr.startAnimation(animDown);
                break;

            case STATE_RELEASE_REFRESH:
                tv_title.setText("松开刷新");
                iv_arr.setVisibility(View.VISIBLE);
                pb_progress.setVisibility(View.INVISIBLE);
                iv_arr.startAnimation(animUp);
                break;

            case STATE_REFRESHING:
                tv_title.setText("正在刷新...");
                iv_arr.clearAnimation();  //必须先清除动画才能隐藏
                iv_arr.setVisibility(View.INVISIBLE);
                pb_progress.setVisibility(View.VISIBLE);

                if (mListener != null) {
                    mListener.onRefresh();
                }

                break;

            default:
                break;
        }
    }

    /**
     * 设置刷新监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 收起下拉刷新控件
     */
    public void onRefreshComplete(boolean success) {


        if(isLoadingMore){  //正在加载更多
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadingMore = false ;

        }else{
            mCurrentState = STATE_PULL_REFRESH;
            tv_title.setText("下拉刷新");
            iv_arr.setVisibility(View.VISIBLE);
            pb_progress.setVisibility(View.INVISIBLE);
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            if (success) {
                tv_time.setText("最后刷新时间:" + getCurrentTime());
            }
        }

    }

    /**
     * 获取当前时间
     */
    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(mItemClickListener!=null){
            mItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }
    }

    OnItemClickListener mItemClickListener ;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener = listener ;
    }

    public interface OnRefreshListener {
        public void onRefresh();
        public void onLoadingMore() ;
    }


    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {

        //箭头向上的动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        //箭头向下的动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }

}
