package com.test.mylibrary.base;

import android.Manifest;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;

import com.donkingliang.labels.LabelsView;
import com.hyperai.hyperlpr3.HyperLPR3;
import com.hyperai.hyperlpr3.bean.HyperLPRParameter;
import com.nex3z.flowlayout.FlowLayout;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.test.mylibrary.R;
import com.test.mylibrary.fragment.IndexFragment;
import com.test.mylibrary.fragment.MainFragment;
import com.test.mylibrary.util.ActivityUtil;
import com.test.mylibrary.util.ScreenUtil;
import com.test.mylibrary.util.XUtil;
import com.test.mylibrary.viewmodels.MainViewModel;
import com.test.mylibrary.widget.CustomConfirmDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description : BaseFragment
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */
@SuppressWarnings("rawtypes")
public abstract class BaseFragment<P extends BasePresenter, T extends BaseViewModel> extends Fragment implements BaseView {

    private Class<P> presenterClass;
    private Unbinder unbinder;
    public FragmentActivity mContext;
    protected T viewModel;
    private Class<T> viewModelClass;

    private String title = "默认标题";
    protected boolean isFirstStart;

    protected P presenter;
    protected NavController navController;
    protected MainViewModel mainViewModel;
    private CustomConfirmDialog customConfirmDialog;
    public Long interTime;

//    public BaseFragment(Class<T> viewModelClass,Class<P> presenterClass){
//        this.viewModelClass = viewModelClass;
//        this.presenterClass = presenterClass;
//    }

//    public BaseFragment() {
//        setTitle();
//    }


    /**
     * 创建 presenter
     *
     * @return presenter
     */
    protected abstract P createPresenter();

    /**
     * 得到布局文件 id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局
     */
    protected abstract void initView(View view);

    /**
     * 设置标题
     */
    protected abstract String setTitle();

    public String getTitle() {
        return title;
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public boolean isNeedButterKnife() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 得到context,在后面的子类Fragment中都可以直接调用
        interTime = System.currentTimeMillis();
        mContext = getActivity();
        Fragment fragment = null;
        for (Fragment fragment1 : mContext.getSupportFragmentManager().getFragments()) {
            if (fragment1 instanceof MainFragment) {
                fragment = fragment1;
            }
        }
        if (fragment == null) {
            fragment = mContext.getSupportFragmentManager().getFragments().get(0);
        }
        mainViewModel = new ViewModelProvider(fragment).get(MainViewModel.class);
        init();
        ScreenUtil.setDefaultDisplay(getActivity());
        ScreenUtil.setFullScreen(getActivity());
        View view = inflater.inflate(getLayoutId(), container, false);
        // setTextSize(view);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (isShouldHideInput(getView(), event)) {
//                    XUtil.closeSoftKeyboard();
//                    v.requestFocus();
//                }
//                return false;
//            }
//        });
        ScreenUtil.setDefaultDisplay(getActivity());
//        if (isNeedButterKnife()) {
//            unbinder = ButterKnife.bind(this, view);
//        }
        if (getViewModelClass() != null && getViewModelStoreOwner() != null) {
            Log.e("<<<", this.getClass().toString());
            viewModel = new ViewModelProvider(getViewModelStoreOwner()).get(getViewModelClass());
        }
        presenter = createPresenter();

        initData();
        title = setTitle();
        initView(view);
        initListener();

        if (mainViewModel.getIsShowFunc().getValue() != null) {
            if (mainViewModel.getIsShowFunc().getValue()) {
                setSpacing(getView(), 28);
            } else {
                setSpacing(getView(), 14);
            }
        }
        return view;
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param view
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View view, MotionEvent event) {
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View childAt = vp.getChildAt(i);
                if (isShouldHideInput(childAt, event)) {
                    return true;
                }
            }
        } else if (view instanceof EditText) {
            EditText editText = (EditText) view;
            if (event.getRawX() < editText.getX() || event.getRawX() > editText.getX() + editText.getWidth() || event.getY() < editText.getY() || event.getY() > editText.getY() + editText.getHeight()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public Class<T> getViewModelClass() {
        return null;
    }

    public ViewModelStoreOwner getViewModelStoreOwner() {
        Fragment fragment;
        try {
            fragment = getParentFragment().getParentFragment();
        } catch (Exception e) {
            e.printStackTrace();
            fragment = getParentFragment();
        }
        return fragment;
    }

    /**
     * 是否是currentFragment的子fragment,如果是，那么不改变currentFragment
     *
     * @return
     */
    public boolean isSubFragmentOfCurrentFragment() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        ScreenUtil.setDefaultDisplay(getActivity());
        initListener();
        if (viewModel != null && !isSubFragmentOfCurrentFragment()) {
            viewModel.getCurrentFragment().setValue(this);
        }
        if (navController == null) {
            try {
                navController = androidx.navigation.fragment.NavHostFragment.findNavController(BaseFragment.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            NavController finalController = navController;

            if (finalController != null) {
                mContext.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Log.e("<<<", "回退栈大小" + navController.getBackStack().size());
                        if (finalController.getBackStack().size() > 2) {
                            finalController.popBackStack();
                        } else if (finalController.getBackStack().size() == 2) {
                            mainViewModel.getContentFragment().setValue(new IndexFragment());
                        }
                    }
                });
            } else {
                mContext.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
//                        mainViewModel.getFuncFragment().setValue(new FuncFragment());
//                        mainViewModel.getContentFragment().setValue(new IndexFragment());
//                        mContext.getSupportFragmentManager().popBackStack();
                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // do something
        if (isNeedButterKnife()) {
            unbinder.unbind();
        }
        // 销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }
    }

    protected abstract void initListener();

    @Override
    public void onErrorCode(BaseBean bean) {
    }
    /**
     * 显示加载中
     */
    @Override
    public int showLoading() {
        return XUtil.showLoading(XUtil.getString(R.string.loading), mContext);
    }

    @Override
    public int showLoading(String msg) {
        int count = XUtil.showLoading(msg, mContext);
        return count;
    }

    @Override
    public void hideLoading(int id) {
        XUtil.dismissLoading(id);
    }

    /**
     * 隐藏加载中
     */
    @Override
    public void hideLoading() {
        XUtil.dismissLoading();
    }

    private void init() {
        XUtil.initialize(getActivity().getApplication());
        // 注册Activity生命周期
        mContext.getApplication().registerActivityLifecycleCallbacks(ActivityUtil.getActivityLifecycleCallbacks());
        requestPermission();
        // 车牌识别算法配置参数
        HyperLPRParameter parameter = new HyperLPRParameter()
                .setDetLevel(HyperLPR3.DETECT_LEVEL_LOW)
                .setMaxNum(1)
                .setRecConfidenceThreshold(0.75f);
// 初始化(仅执行一次生效)
        HyperLPR3.getInstance().init(mContext, parameter);
        // 由于这个时候activity已经创建了 所以第一个要手动入栈
        ActivityUtil.activityStack.push(getActivity());

        if (isFirstStart) {
//            initX5();
            isFirstStart = false;
        }

    }

    private void requestPermission() {
        PermissionX.init(this).permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (grantedList.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {

                }
            }
        });
    }

    /**
     * 设置各个控件之间的间距
     */
    public void setSpacing(View view, int dpValue) {
        if (view instanceof FlowLayout) {
            FlowLayout flowLayout = (FlowLayout) view;
            flowLayout.setChildSpacing(ScreenUtil.dip2px(getActivity(), dpValue));
        } else if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View childAt = vp.getChildAt(i);
                setSpacing(childAt, dpValue);
            }
        }
    }

    public void navigate(int resId) {
        if (navController == null) {
            return;
        }
        try {
            XUtil.closeSoftKeyboard();
            navController.navigate(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        //        ScreenUtil.setDefaultDisplay(getActivity());
        super.onAttach(context);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (newConfig.fontScale != 1) // 非默认值
        {
            Resources res = getResources();
            if (getResources().getConfiguration().fontScale != 1) {
                Configuration newConfig2 = new Configuration();
                newConfig.setToDefaults(); // 设置默认
                res.updateConfiguration(newConfig2, res.getDisplayMetrics());
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    public void popBackStack() {
        navController.popBackStack();
    }

    public void popBackStack(int resId, boolean inclusive) {
        navController.popBackStack(resId, inclusive);
    }
}
