package com.chanjay.smartpeking.fragment;

import android.view.View;
import android.widget.RadioGroup;

import com.chanjay.smartpeking.R;

/**
 * 主页内容
 */
public class ContentFragment extends BaseFragment {

    private RadioGroup rg_group ;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        rg_group.check(R.id.rb_home); //默认勾选首页
    }
}
