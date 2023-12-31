package com.test.mylibrary.base;

import com.google.gson.JsonParseException;
import com.test.mylibrary.R;
import com.test.mylibrary.util.XUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * Description : BaseObserver
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */
@SuppressWarnings("rawtypes")
public abstract class BaseObserver<T> extends DisposableObserver<T> {

    protected BaseView view;
    private boolean isShowDialog;
    private String loadingMsg;
    private int id = 0;

    protected BaseObserver(BaseView view) {
        this.view = view;
    }

    /**
     * 带进度条的初始化方法
     *
     * @param view view
     * @param isShowDialog 是否显示进度条
     */
    protected BaseObserver(BaseView view, boolean isShowDialog) {
        this.view = view;
        this.isShowDialog = isShowDialog;
        loadingMsg = XUtil.getString(R.string.loading);
    }

    protected BaseObserver(BaseView view, boolean isShowDialog, String msg) {
        this.view = view;
        this.isShowDialog = isShowDialog;
        this.loadingMsg = msg;
    }

    @Override
    protected void onStart() {
        if (view != null && isShowDialog) {
            XUtil.closeSoftKeyboard();
            id = view.showLoading(loadingMsg);
        }
    }

    @Override
    public void onNext(T o) {
        if (view != null && isShowDialog) {
            view.hideLoading(id);
        }
        onSuccess(o);
    }

    @SuppressWarnings("all")
    @Override
    public void onError(Throwable e) {
        if (view != null && isShowDialog) {
            view.hideLoading(id);
        }
        BaseException be;

        if (e != null) {
            // 自定义异常
            if (e instanceof BaseException) {
                be = (BaseException) e;
                // 回调到view层 处理 或者根据项目情况处理
                if (view != null) {
                    // TODO:修改basebean
                    view.onErrorCode(
                            new BaseBean(false, "failed", be.getErrorCode(), be.getErrorMsg()));
                } else {
                    onError(be.getErrorMsg());
                }
                // 系统异常
            } else {
                if (e instanceof HttpException) {
                    // HTTP错误
                    be = new BaseException(XUtil.getString(R.string.BAD_NETWORK_MSG), e);
                } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
                    // 连接错误
                    be = new BaseException(XUtil.getString(R.string.CONNECT_ERROR_MSG), e);
                } else if (e instanceof InterruptedIOException) {
                    // 连接超时
                    be = new BaseException(XUtil.getString(R.string.CONNECT_TIMEOUT_MSG), e);
                } else if (e instanceof JsonParseException
                        || e instanceof JSONException
                        || e instanceof ParseException) {
                    // 解析错误
                    be = new BaseException(XUtil.getString(R.string.PARSE_ERROR_MSG), e);
                } else {
                    be = new BaseException(XUtil.getString(R.string.OTHER_MSG), e);
                }
            }
        } else {
            be = new BaseException(XUtil.getString(R.string.OTHER_MSG));
        }
        be.printStackTrace();
        onError(be.getErrorMsg());
    }

    @Override
    public void onComplete() {
        if (view != null && isShowDialog) {
            //            view.hideLoading();
        }
    }

    /**
     * 完成
     *
     * @param o 对象
     */
    public abstract void onSuccess(T o);

    /**
     * 失败
     *
     * @param msg 失败信息
     */
    public abstract void onError(String msg);
}
