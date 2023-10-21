package com.test.mylibrary.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Description : BaseFragment
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */
@SuppressWarnings("rawtypes")
public abstract class BaseFragmentViewDataBinding<
                P extends BasePresenter, T extends BaseViewModel, U extends ViewDataBinding>
        extends BaseFragment<P, T> implements BaseView {
    protected U binding;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View view = binding.getRoot();;
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
}
