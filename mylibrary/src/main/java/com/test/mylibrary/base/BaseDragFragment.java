package com.test.mylibrary.base;

import static com.test.mylibrary.util.ScreenUtil.setDefaultDisplay;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.test.mylibrary.R;
import com.test.mylibrary.fragment.FuncFragment;
import com.test.mylibrary.fragment.IndexFragment;
import com.test.mylibrary.fragment.MainFragment;
import com.test.mylibrary.interfaces.OnDragListener;
import com.test.mylibrary.util.ScreenUtil;
import com.test.mylibrary.util.XUtil;
import com.test.mylibrary.viewmodels.MainViewModel;

/**
 * Description : BaseFragment
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */
@SuppressWarnings("rawtypes")
public abstract class BaseDragFragment<P extends BasePresenter, T extends BaseViewModel>
        extends Fragment implements BaseView, View.OnTouchListener {

    protected FragmentActivity mContext;
    private OnDragListener onDragListener;
    public MainViewModel mainViewModel;
    protected T viewModel;

    protected P presenter;
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    private boolean isShow = false;

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

    /** 初始化布局 */
    protected abstract void initView();

    /** 初始化数据 */
    protected abstract void initData();

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        setDefaultDisplay(getActivity());
        View view = inflater.inflate(getLayoutId(), container, false);
        // setTextSize(view);
        view.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isShouldHideInput(getView(), event)) {
                            XUtil.closeSoftKeyboard();
                        }
                        return false;
                    }
                });
        setDefaultDisplay(getActivity());
//        unbinder = ButterKnife.bind(this, view);
        // requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 得到context,在后面的子类Fragment中都可以直接调用
        mContext = getActivity();
        presenter = createPresenter();
        if (getViewModelClass() != null) {
            viewModel = new ViewModelProvider(getViewModelStoreOwner()).get(getViewModelClass());
        }
        initData();
        Fragment fragment = null;
        for (Fragment fragment1 : mContext.getSupportFragmentManager().getFragments()) {
            if (fragment1 instanceof MainFragment){
                fragment = fragment1;
            }
        }
        if (fragment == null){
            fragment = mContext.getSupportFragmentManager().getFragments().get(0);
        }
        mainViewModel =
                new ViewModelProvider(fragment)
                        .get(MainViewModel.class);
        initView();
        initListener();
        return view;
    }

    protected abstract Class<T> getViewModelClass();

    private ViewModelStoreOwner getViewModelStoreOwner() {
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // do something
        // 销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }
    }

    protected abstract void initListener();

    @Override
    public void onErrorCode(BaseBean bean) {}

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
            if (event.getRawX() < editText.getX()
                    || event.getRawX() > editText.getX() + editText.getWidth()
                    || event.getY() < editText.getY()
                    || event.getY() > editText.getY() + editText.getHeight()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /** 显示加载中 */
    @Override
    public int showLoading() {
        return XUtil.showLoading(XUtil.getString(R.string.loading), mContext);
    }

    @Override
    public int showLoading(String msg) {
        return XUtil.showLoading(msg, mContext);
    }

    @Override
    public void hideLoading(int id) {
        XUtil.dismissLoading(id);
    }

    /** 隐藏加载中 */
    @Override
    public void hideLoading() {
        XUtil.dismissLoading();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 当手指移动的时候
            x2 = event.getX();
            y2 = event.getY();
            if (x1 - x2 > 50) {
                //                Log.d("<<<","左边");
                //                Toast.makeText(getActivity(), "向左滑", Toast.LENGTH_SHORT).show();
                isShow = false;
                //                onDragListener.hide();
            } else if (x2 - x1 > 50) {
                //                Log.d("<<<","右边");
                //                Toast.makeText(getActivity(), "向右滑", Toast.LENGTH_SHORT).show();
                isShow = true;
                //                onDragListener.show();
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP && onDragListener != null) {
            if (isShow) {
                onDragListener.show();
            } else {
                onDragListener.hide();
            }
        }
        return true;
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }


    @Override
    public void onResume() {
        super.onResume();
        setDefaultDisplay(getActivity());
        initListener();
        //        NavController controller = null;
        //        try {
        //            controller= NavHostFragment.findNavController(BaseDragFragment.this);
        //        }catch (Exception e){
        //            e.printStackTrace();
        //        }
        //
        //        if (controller!=null){
        //            View view = getView();
        //            if (view != null) {
        //                view.setFocusableInTouchMode(true);
        //                view.requestFocus();
        //                view.setOnKeyListener((view1, i, keyEvent) -> {
        //                    if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() ==
        // KeyEvent.ACTION_UP) {
        //                        NavHostFragment.findNavController(BaseDragFragment.this)
        //                                .popBackStack();
        //                        return true;
        //                    }
        //                    return false;
        //                });
        //            }
        //        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        setDefaultDisplay(getActivity());
        super.onAttach(context);
    }
}
