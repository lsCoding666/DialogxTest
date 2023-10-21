package com.test.mylibrary.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewbinding.ViewBinding;

import com.test.mylibrary.R;
import com.test.mylibrary.fragment.FuncFragment;
import com.test.mylibrary.fragment.IndexFragment;
import com.test.mylibrary.fragment.MainFragment;
import com.test.mylibrary.viewmodels.MainViewModel;

public abstract class BaseDragFragmentViewBinding<
                P extends BasePresenter, T extends BaseViewModel, U extends ViewBinding>
        extends BaseDragFragment<P, T> {
    protected U binding;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        View view = binding.getRoot();
        super.onCreateView(inflater, container, savedInstanceState);
        ImageView ivBack =   view.findViewById(R.id.title_back_iv);
        if (ivBack!=null){
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        NavController navController = Navigation.findNavController(mContext, R.id.fragment);
                        Log.e("<<<","回退栈大小"+navController.getBackStack().size());
                        if (navController.getBackStack().size() == 2) {
                            if (mainViewModel == null) {
                                mainViewModel =
                                        new ViewModelProvider(MainFragment.getInstance()).get(MainViewModel.class);
                            }
                            mainViewModel.getContentFragment().setValue(new IndexFragment());
                        } else {
                            navController.popBackStack();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        mainViewModel.getFuncFragment().setValue(new FuncFragment());
                        mainViewModel.getContentFragment().setValue(new IndexFragment());
                    }

                }
            });
        }

        return view;
    }

    public abstract U getViewBinding(LayoutInflater layoutInflater, ViewGroup container);
}
