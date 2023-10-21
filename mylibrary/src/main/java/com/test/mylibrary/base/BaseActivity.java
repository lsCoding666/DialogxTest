package com.test.mylibrary.base;

import static com.test.mylibrary.util.ScreenUtil.setDefaultDisplay;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.test.mylibrary.R;
import com.test.mylibrary.util.XUtil;

import butterknife.ButterKnife;

@SuppressWarnings("rawtypes")
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView {

    protected P presenter;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        //设置竖屏
        //        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        ButterKnife.bind(this);
        presenter = createPresenter();
        initData();
        initView();
        setDefaultDisplay(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XUtil.dismissLoading();
        // 销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }
    }

    protected void initListener() {}

    @Override
    public int showLoading() {
        return XUtil.showLoading(XUtil.getString(R.string.loading), this);
    }

    @Override
    public int showLoading(String msg) {
        return XUtil.showLoading(msg, this);
    }

    @Override
    public void hideLoading(int id) {
        XUtil.dismissLoading(id);
    }

    @Override
    public void hideLoading() {
        XUtil.dismissLoading();
    }

    /** 可以处理异常 */
    @Override
    public void onErrorCode(BaseBean bean) {}

    @Override
    public void onBackPressed() {
        // 修复内存泄漏 官方bug 匿名AIDL回调中引用了mHandler这个内部变量，当onDestory
        // ()调用后，mHandler则继续被引用导致Activty无法正常释放，从而导致了泄露。
        finishAfterTransition();
    }
}
