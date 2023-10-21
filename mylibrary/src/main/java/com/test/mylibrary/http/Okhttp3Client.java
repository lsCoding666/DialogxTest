package com.test.mylibrary.http;

import android.util.Log;

import com.test.mylibrary.util.SpUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author 梁爽
 * @create 2020/8/9 11:29
 */
public class Okhttp3Client {
    private static volatile Okhttp3Client okhttp3Client;
    private OkHttpClient okHttpClient;

    /**
     * 单例调用
     *
     * @return RetrofitService
     */
    public static Okhttp3Client getInstance() {
        if (okhttp3Client == null) {
            synchronized (Object.class) {
                if (okhttp3Client == null) {
                    okhttp3Client = new Okhttp3Client();
                }
            }
        }
        return okhttp3Client;
    }

    /**
     * 获取OkHttpClient对象
     *
     * @return 获取OkHttpClient对象
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private Okhttp3Client() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // 不进行客户端证书验证
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // 不进行服务器证书验证
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            // 创建 SSLContext，并使用信任所有证书的 TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            // 创建 HostnameVerifier，用于验证主机名
            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            // 配置缓存 200m
            int cacheSize = 200 * 1024 * 1024;
            //        @SuppressWarnings("AliDeprecation") File cacheFile = new
            // File(ActivityUtil.getCurrentActivity().getCacheDir(), "cache");
            //        if (!cacheFile.exists()) {
            //            //noinspection ResultOfMethodCallIgnored
            //            cacheFile.mkdirs();
            //        }
            //        Cache cache = new Cache(cacheFile, cacheSize);

            // 配置okHttp并设置时间、日志信息和cookies
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
                try {
                    String text = URLDecoder.decode(message, "utf-8");
                    Log.e("OKHttp-----", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("OKHttp-----", message);
                }
            });
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            this.okHttpClient = new OkHttpClient.Builder()
                    //                .cache(cache)
                    .addInterceptor(interceptor)
                    //                .addInterceptor(new CacheForceInterceptorNoNet())
                    // 响应拦截cookie
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            // 这里获取请求返回的cookie
                            //noinspection AlibabaUndefineMagicConstant
                            //                        if
                            // (!SpUtil.getBoolean(GlobalConstant.IS_LOGIN) &&
                            // !originalResponse.headers("token").isEmpty()) {
                            //                            //noinspection
                            // AlibabaUndefineMagicConstant
                            //
                            // SpUtil.setString(GlobalConstant.TOKEN,
                            // originalResponse.headers("token").get(0));
                            //                        }
                            return originalResponse;
                        }
                    })
                    //                .addNetworkInterceptor(new CacheInterceptorOnNet())
                    // 请求添加cookie
                    .sslSocketFactory(sslContext.getSocketFactory(),(X509TrustManager) trustAllCerts[0]).hostnameVerifier(hostnameVerifier)
                    .addNetworkInterceptor(chain -> {
                        Request request;
                        if (!SpUtil.getString("token").isEmpty()) {
                            request = chain.request().newBuilder().addHeader("token", SpUtil.getString("token")).build();
                        } else {
                            request = chain.request().newBuilder().build();
                        }
                        return chain.proceed(request);
                    }).callTimeout(120, TimeUnit.SECONDS).connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
