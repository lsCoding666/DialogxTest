package com.test.mylibrary.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

/**
 * Description : BaseFragment
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */
@SuppressWarnings("rawtypes")
public abstract class BaseFragmentViewBinding<
                P extends BasePresenter, T extends BaseViewModel, U extends ViewBinding>
        extends BaseFragment<P, T> implements BaseView {
    protected U binding;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        View view = binding.getRoot();;
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    public abstract U getViewBinding(LayoutInflater layoutInflater, ViewGroup container);
}
