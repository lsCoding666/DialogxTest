package com.test.mylibrary.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtil {
    // dp->px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getConfiguration().densityDpi / 160;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(float dpValue) {
        Context context = XUtil.getApplication();
        final float scale = context.getResources().getConfiguration().densityDpi / 160;
        return (int) (dpValue * scale + 0.5f);
    }

    // px->dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getConfiguration().densityDpi / 160;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        Context context = ActivityUtil.getCurrentActivity();
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        Context context = ActivityUtil.getCurrentActivity();
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 得到屏幕宽度
     *
     * @param context context
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getScreenParameter(context, true);
    }

    /**
     * 得到屏幕高度
     *
     * @param context context
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getScreenParameter(context, false);
    }

    /**
     * 得到屏幕的参数
     *
     * @param context context
     * @param isWidth true表示得到宽度，反之得到高度
     * @return 屏幕参数
     */
    public static int getScreenParameter(Context context, boolean isWidth) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display;
        if (wm != null) {
            display = wm.getDefaultDisplay();
            display.getMetrics(dm);
        }
        if (isWidth) {
            return dm.widthPixels;
        } else {
            return dm.heightPixels;
        }
    }

    /**
     * 获得屏幕dpi
     *
     * @return 屏幕dpi
     */
    public static int getDpi(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int density = dm.densityDpi;
        return density;
    }

    public static boolean isPad(Activity activity) {
        double standard = 1920 / 1.0 / 1080;
        // 判断平板和手机，我认为优先从屏幕的长边比短边的比值得出结果
        // 如果是手机的话，长边比短边的比值 如果大于1.778 则认为是手机
        // 标准分辨率
        // 1280 720 1.778
        // 1920 1080 1.7778

        // 手机 （全面屏）
        // 2400 1080 2.222 红米k40 小米13等主流1080p分辨率
        // 3200 1440 2.222 红米k50 小米13pro 主流2k分辨率
        // 2712×1220 2.2223 红米k50至尊版 1.5k分辨率
        // 2160 1080 2 小米6x等较老记性 全面屏刚刚起步时的分辨率
        // 由于全面屏已经普及，所以按照屏幕比例来说，长边比短边大于标准分辨率的比例的屏幕就算是手机了

        // 平板
        // 2560 1660 1.54 测试用华为平板 主流分辨率
        // 2048×1536 1.33 ipad 1080p分辨率
        // 1280x800 1.6125 较老平板分辨率

        // 但是也有一种特殊情况，因为标准分辨率 比如1920 1080这些 有可能是平板，也有可能是手机
        // 在屏幕的比例无法判断是电脑还是手机的情况下，就按照勾股定理，计算屏幕对角线的像素数量
        // 再除以屏幕的像素密度dpi，得到屏幕对角线的长度（单位英寸），如果大于7则认为是平板，小于7手机

        int height = getScreenHeight(activity);
        int width = getScreenWidth(activity);
        int dpi = getDpi(activity);
        double temp = 0;
        if (height > width) {
            temp = height / 1.0 / width;
        } else {
            temp = width / 1.0 / height;
        }
        // 如果比值大于标准分辨率 认为是手机
        if (temp > standard) {
            return false;
        } else if (temp < standard) {
            return true;
        } else {
            double xiebianPx = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
            double xiebian = xiebianPx / dpi;
            if (xiebian >= 7) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void setDefaultDisplay(Activity context) {
        if (ScreenUtil.isPad(context)) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                Resources res = context.getResources();
                Configuration configuration = res.getConfiguration();
                if (res.getDisplayMetrics().densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
                    configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
                }
                float textSize = SpUtil.getFloat("TEXT_SIZE");
                if (textSize > 0) {
                    configuration.fontScale = textSize;
                }
                res.updateConfiguration(configuration, res.getDisplayMetrics());
            }
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Resources res = context.getResources();
            Configuration configuration = res.getConfiguration();
            if (res.getDisplayMetrics().densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
            }

            int screenWidth = Math.min(getScreenWidth(context), getScreenHeight(context));

            configuration.densityDpi = (int) (320 * screenWidth / 1080);
            Resources.getSystem().getConfiguration().densityDpi = (int) (300 * screenWidth / 1080);
            // Resources.getSystem().getConfiguration().fontScale = 1.4f;
            float textSize = SpUtil.getFloat("TEXT_SIZE");
            if (textSize > 0) {
                configuration.fontScale = textSize;
            }
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
    }

    public static void setTextSize(Activity context, float fontScale) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Resources res = context.getResources();
            Configuration configuration = res.getConfiguration();
            configuration.fontScale = fontScale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
    }

    public static void setFullScreen(Activity activity) {
//        if (true){
//            return;
//        }
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 如果当前activity是全屏的 那么也设置全屏
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    public static void setFullScreen(Dialog dialog) {
//        if (true){
//            return;
//        }
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(params);
        // 如果当前activity是全屏的 那么也设置全屏
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }
}
