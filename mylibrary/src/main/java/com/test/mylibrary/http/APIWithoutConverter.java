package com.test.mylibrary.http;

import com.test.mylibrary.util.XUtil;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * @author 梁爽
 * @create 2020/8/9 10:39
 */
public class APIWithoutConverter {
    static final String BASE_URL = "http://1.1.1.1";

    public interface IFarmApi {
        // ----------------登录注册相关----------------------
        // 登录验证码
        @GET("/code/imgCode")
        Observable<ResponseBody> getCode();
    }
}
