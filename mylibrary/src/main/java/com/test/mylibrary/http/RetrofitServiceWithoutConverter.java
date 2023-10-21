package com.test.mylibrary.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author 梁爽
 * @create 2020/8/9 10:38
 */
public class RetrofitServiceWithoutConverter {
    private static volatile RetrofitServiceWithoutConverter apiRetrofit;
    private APIWithoutConverter.IFarmApi apiServer;

    /**
     * 单例调用
     *
     * @return RetrofitService
     */
    public static RetrofitServiceWithoutConverter getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new RetrofitServiceWithoutConverter();
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
    public APIWithoutConverter.IFarmApi getApiService() {
        return apiServer;
    }

    /** 初始化retrofit */
    private RetrofitServiceWithoutConverter() {
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
                        // .addConverterFactory(BaseConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(API.BASE_URL)
                        .build();

        apiServer = retrofit.create(APIWithoutConverter.IFarmApi.class);
    }
}
