package com.test.mylibrary.fragment;

import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.test.mylibrary.R;
import com.test.mylibrary.base.BaseDragFragment;
import com.test.mylibrary.base.BaseFragment;
import com.test.mylibrary.base.BaseFragmentViewBinding;
import com.test.mylibrary.base.BasePresenter;
import com.test.mylibrary.base.BaseViewModel;
import com.test.mylibrary.databinding.ComitFragmentMainPhoneBinding;
import com.test.mylibrary.interfaces.OnDragListener;
import com.test.mylibrary.util.XUtil;
import com.test.mylibrary.viewmodels.MainViewModel;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragmentViewBinding<BasePresenter, BaseViewModel, ComitFragmentMainPhoneBinding> {

    private static final String TAG = "MainFragment";

    private boolean isUploadLog = false;

    // 右侧区域
    private BaseDragFragment currentFragment;
    private OnDragListener onDragListener;
    private MainViewModel mainViewModel;

    @Override
    protected BasePresenter createPresenter() {
        return new BasePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comit_fragment_main_phone;
    }

    @Override
    protected void initData() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        DialogX.init(mContext);
        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW;
        DialogX.onlyOnePopTip = false;
        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.useHaptic = true;
        DialogX.cancelable = true;
        DialogX.cancelableTipDialog = true;
        WaitDialog.overrideCancelable = BaseDialog.BOOLEAN.TRUE;
        DialogXBaseRelativeLayout.debugMode = true;

        mainViewModel.getMainFragment().setValue(this);
        File dataBaseFile = new File(mContext.getFilesDir().getAbsolutePath().replace("files", "databases"));
        mainViewModel.getFuncFragment().observe(this, new Observer<BaseFragment>() {
            @Override
            public void onChanged(BaseFragment baseFragment) {
                replaceFuncFragment();
                showFunc();
            }
        });
        mainViewModel.getContentFragment().observe(this, new Observer<BaseDragFragment>() {
            @Override
            public void onChanged(BaseDragFragment baseDragFragment) {
                showFunc();
                jumpToContentFragment();
                setDragListener();
            }
        });
    }

    @Override
    protected void initListener() {
    }

//    @OnClick({R2.id.main_ll})
//    public void onMainLlClick() {
//        XUtil.closeSoftKeyboard();
//    }

    @Override
    protected void initView(View view) {
        mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //  mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        RelativeLayout.LayoutParams paramsOne = (RelativeLayout.LayoutParams) binding.funcFl.getLayoutParams();
        paramsOne.addRule(RelativeLayout.ALIGN_PARENT_START);
        paramsOne.setMargins(0, 0, 0, 0);
        binding.funcFl.setLayoutParams(paramsOne);

        RelativeLayout.LayoutParams paramsTwo = (RelativeLayout.LayoutParams) binding.workspaceFl.getLayoutParams();
        paramsTwo.addRule(RelativeLayout.RIGHT_OF, R.id.func_fl);
        binding.workspaceFl.setLayoutParams(paramsTwo);
        //        if (ScreenUtil.isPad(getActivity())){
        //            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //            RelativeLayout.LayoutParams paramsOne = (RelativeLayout.LayoutParams)
        // mFlFunc.getLayoutParams();
        //            paramsOne.addRule(RelativeLayout.ALIGN_PARENT_START);
        //            paramsOne.setMargins(0,0,0,0);
        //            mFlFunc.setLayoutParams(paramsOne);
        //
        //            RelativeLayout.LayoutParams paramsTwo = (RelativeLayout.LayoutParams)
        // mFl.getLayoutParams();
        //            paramsTwo.addRule(RelativeLayout.RIGHT_OF,R.id.func_fl);
        //            mFl.setLayoutParams(paramsTwo);
        //        }else{
        //            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        //        }

        //        Fragment workSpaceFragment = new WorkSpaceFragment();
        currentFragment = new IndexFragment(MainFragment.this);
        mainViewModel.getFuncFragment().setValue(new FuncFragment());
        mContext.getSupportFragmentManager().beginTransaction().replace(R.id.workspace_fl, currentFragment).commit();

        replaceFuncFragment();
        checkNetworkStatus();

        //监听键盘弹出/隐藏
//        ViewGroup rootViewGroup = (ViewGroup) ((ViewGroup) mContext.findViewById(android.R.id.content));
//        rootViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect rect = new Rect();
//                //getWindowVisibleDisplayFrame 获取当前窗口可视区域大小
//                rootViewGroup.getRootView().getWindowVisibleDisplayFrame(rect);
//                int screenHeight = mContext.getWindow().getDecorView().getHeight();
//                //键盘弹出时，可视区域大小改变，屏幕高度 - 窗口可视区域高度 = 键盘弹出高度
//                int softHeight = screenHeight - rect.bottom;
//                /**
//                 * 上移的距离 = 键盘的高度 - 按钮距离屏幕底部的高度(如果手机高度很大，上移的距离会是负数，界面将不会上移)
//                 * 按钮距离屏幕底部的高度是用屏幕高度 - 按钮底部距离父布局顶部的高度
//                 * 注意这里 btn.getBottom() 是按钮底部距离父布局顶部的高度，这里也就是距离最外层布局顶部高度
//                 */
//                int scrollDistance = softHeight - (screenHeight - 100);
//                if (scrollDistance > 0) {
//                    //具体移动距离可自行调整
//                    rootViewGroup.scrollTo(0, scrollDistance + 60);
//                } else {
//                    //键盘隐藏，页面复位
//                    rootViewGroup.scrollTo(0, 300);
//                }
//            }
//        });

    }

    private void checkNetworkStatus() {
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                // 执行网络请求，判断与后端的连通性
//                presenter.getToken();
//            }
//        };
//        timer.schedule(task, 0, 10000);  // interval为定时任务执行的间隔时间，单位为毫秒
    }

    private void setDragListener() {
        mainViewModel.getContentFragment().getValue().setOnDragListener(new OnDragListener() {
            @Override
            public void hide() {
                hideFunc();
            }

            @Override
            public void show() {
                showFunc();
            }
        });
    }

    public void hideFunc() {
        if (binding.funcFl.getVisibility() == View.VISIBLE) {
            TranslateAnimation translateAnimation1 = new TranslateAnimation(0, -binding.funcFl.getWidth(), 0, 0);
            translateAnimation1.setDuration(500);
            translateAnimation1.setFillAfter(true);
            translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.funcFl.clearAnimation();
                    binding.funcFl.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mainViewModel.getIsShowFunc().setValue(false);
            binding.funcFl.startAnimation(translateAnimation1);
            // 设置间距
            setSpacing(getView().findViewById(R.id.workspace_fl), 14);
        }
    }

    public void showFunc() {
        if (binding.funcFl.getVisibility() == View.GONE) {
            binding.funcFl.setVisibility(View.VISIBLE);
            //                mLlFunc.offsetLeftAndRight(-mLlFunc.getWidth());
            TranslateAnimation translateAnimation1 = new TranslateAnimation(-binding.funcFl.getWidth(), 0, 0, 0);
            translateAnimation1.setDuration(500);
            translateAnimation1.setFillAfter(true);
            translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mainViewModel.getIsShowFunc().setValue(true);
            binding.funcFl.startAnimation(translateAnimation1);
            // 设置间距
            setSpacing(getView().findViewById(R.id.workspace_fl), 28);
        }
    }

    private void replaceFuncFragment() {
        mContext.getSupportFragmentManager().beginTransaction().replace(R.id.func_fl, mainViewModel.getFuncFragment().getValue()).commit();
    }

    private void jumpToContentFragment() {
        mContext.getSupportFragmentManager().beginTransaction().replace(R.id.workspace_fl, Objects.requireNonNull(mainViewModel.getContentFragment().getValue())).commit();
    }

    @Override
    protected String setTitle() {
        return "";
    }

    public MainFragment() {
        isFirstStart = true;
    }

    @Override
    public ComitFragmentMainPhoneBinding getViewBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return ComitFragmentMainPhoneBinding.inflate(layoutInflater);
    }

    private static class MainFragmentHolder {
        private static final MainFragment INSTANCE = new MainFragment();
    }

    public static MainFragment getInstance() {
        return MainFragmentHolder.INSTANCE;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
