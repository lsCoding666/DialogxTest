package com.test.mylibrary.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivityViewBinding<
        P extends BasePresenter, T extends BaseViewModel, U extends ViewBinding> extends BaseActivity<P>{
    protected U binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        //设置竖屏
        //        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        presenter = createPresenter();
        initData();
        initView(savedInstanceState);
    }

    protected abstract void initView(Bundle bundle);

    public abstract U getViewBinding();
}
