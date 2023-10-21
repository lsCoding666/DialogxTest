package com.test.mylibrary.base;

import com.google.gson.JsonParseException;
import com.test.mylibrary.R;
import com.test.mylibrary.util.XUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.HttpException;

@SuppressWarnings("rawtypes")
public abstract class BaseSingleObserver<T> extends DisposableSingleObserver<T> {
    @SuppressWarnings("all")
    @Override
    public void onError(Throwable e) {

        BaseException be;

        if (e != null) {
            // 自定义异常
            if (e instanceof BaseException) {
                be = (BaseException) e;
                // 回调到view层 处理 或者根据项目情况处理
                // 系统异常
                onError(be.getErrorMsg());
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

    public abstract void onError(String msg);
}
