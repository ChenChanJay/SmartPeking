package com.chanjay.smartpeking.fragment;

import android.view.View;

import com.chanjay.smartpeking.R;

/**
 * 侧边栏
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        return view;
    }
}
