package com.test.mylibrary.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.test.mylibrary.R;
import com.test.mylibrary.base.BaseFragment;
import com.test.mylibrary.base.BasePresenter;

import butterknife.BindView;

public class FuncFragment extends BaseFragment {

    private static final String TAG = "FuncFragment";


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comit_fragment_mainpage_func;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
    }

//    @OnClick((com.test.mylibrary.com.test.mylibrary.com.test.mylibrary.com.test.mylibrary.R2.id.add_case_func_ll))
//    public void addCaseClickListener() {
//        mainViewModel.getContentFragment().setValue(AddCaseFragment.getInstance());
//
//    }
//
//    @OnClick({com.test.mylibrary.com.test.mylibrary.com.test.mylibrary.com.test.mylibrary.R2.id.progress_case_ll})
//    public void onProgressCaseClick(View view) {
//        mainViewModel.getContentFragment().setValue(new ProgressCaseFragment());
//
//    }
}
