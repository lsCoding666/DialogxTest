package com.test.mylibrary.base;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;


import com.test.mylibrary.util.XUtil;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {

    private static final String TAG = PluginManager.class.getSimpleName();
    private static final String METHOD_ADD_ASSET_PATH = "addAssetPath";

    private static PluginManager sInstance;

    private static Context sContext;
    // 加载插件资源的Resources
    private Resources resources;
    // 加载插件classes的DexClassLoader
    private DexClassLoader dexClassLoader;
    // 插件路径
    private String dexPath;

    private PluginManager() {
        loadPlugin();
    }

    public static PluginManager getInstance() {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager();
                }
            }
        }
        return sInstance;
    }

    // 最先调用
    public static void initContext(Context context) {
        sContext = context.getApplicationContext();
    }

    /** 加载插件 */
    private void loadPlugin() {
        try {
            String dexPath = getDexPath();
            if (TextUtils.isEmpty(dexPath)) {
                return;
            }
            // dexClassLoader需要一个缓存目录，最好是私有目录，防止被攻击
            // data/data/当前应用的包名/pluginDir
            File pluginDir = sContext.getDir("pluginDir", Context.MODE_PRIVATE);
            dexClassLoader =
                    new DexClassLoader(
                            dexPath, pluginDir.getAbsolutePath(), null, sContext.getClassLoader());

            // 创建AssetManager，用来加载资源
            AssetManager assetManager = AssetManager.class.newInstance();
            // 执行此方法，目的是将插件包的路径添加进去
            Method addAssetPathMethod =
                    assetManager.getClass().getMethod(METHOD_ADD_ASSET_PATH, String.class); // 类型库
            addAssetPathMethod.invoke(assetManager, dexPath); // 插件包的路径

            // 使用来获取宿主的资源配置信息
            Resources r = sContext.getResources();
            // 特殊的Resources，加载插件里面的资源
            this.resources =
                    new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClassLoader getClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }

    public String getDexPath() {
        if (TextUtils.isEmpty(dexPath)) {
            File pluginFile =
                    new File(
                            XUtil.getApplication().getExternalFilesDir("test").getAbsolutePath()
                                    + "/testapp-debug.apk");
            if (!pluginFile.exists()) {
                Log.d(TAG, "getDexPath: 插件不存在");
                return "";
            }
            dexPath = pluginFile.getAbsolutePath();
        }
        return dexPath;
    }
}
