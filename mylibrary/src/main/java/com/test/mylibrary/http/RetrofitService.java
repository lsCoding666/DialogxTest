package com.test.mylibrary.http;


import com.test.mylibrary.http.gson.BaseConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Description : RetrofitService
 *
 * @author XuCanyou666
 * @date 2020/2/8
 */
public class RetrofitService {

    private static volatile RetrofitService apiRetrofit;
    private API.IApi apiServer;

    /**
     * 单例调用
     *
     * @return RetrofitService
     */
    public static RetrofitService getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new RetrofitService();
                }
            }
        }
        return apiRetrofit;
    }

    public static RetrofitService getInstance2() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new RetrofitService(API.BASE_URL2);
                }
            }
        }
        return apiRetrofit;
    }

    /**
     * 获取api对象
     *
     * @return api对象
     */
    public API.IApi getApiService() {
        return apiServer;
    }

    /** 初始化retrofit */
    private RetrofitService() {
        // 防止反射破坏单例
        if (apiServer != null) {
            throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取");
        }

        OkHttpClient okHttpClient = Okhttp3Client.getInstance().getOkHttpClient();

        // 关联okHttp并加上rxJava和Gson的配置和baseUrl
        Retrofit retrofit =
                new Retrofit.Builder()
                        .client(okHttpClient)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(BaseConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(API.BASE_URL)
                        .build();
        //        RetrofitCache.getInstance().addRetrofit(retrofit);
        apiServer = retrofit.create(API.IApi.class);
    }

    private RetrofitService(String baseUrl) {
        // 防止反射破坏单例
        if (apiServer != null) {
            throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取");
        }

        OkHttpClient okHttpClient = Okhttp3Client.getInstance().getOkHttpClient();

        // 关联okHttp并加上rxJava和Gson的配置和baseUrl
        Retrofit retrofit =
                new Retrofit.Builder()
                        .client(okHttpClient)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(BaseConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(baseUrl)
                        .build();
        //        RetrofitCache.getInstance().addRetrofit(retrofit);
        apiServer = retrofit.create(API.IApi.class);
    }
}
