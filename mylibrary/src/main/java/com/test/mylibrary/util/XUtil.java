package com.test.mylibrary.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kongzue.dialogx.dialogs.WaitDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xucanyou666 @Date 2020/4/27 09:26 email：913710642@qq.com
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class XUtil {
    private static Application mApplicationContext;
//    private static Map<Integer, ProgressDialog> progressDialogMap = new HashMap<>();
    private static Map<Integer, WaitDialog> progressDialogMap = new HashMap<>();
    private static WaitDialog progressDialog;
    private static int count = 0;
    private static WaitDialog waitDialog;

    @SuppressLint("StaticFieldLeak")
    public static void initialize(Application app) {
        mApplicationContext = app;
    }

    public static Application getApplication() {
        return mApplicationContext;
    }

    public static int showLoading(String msg) {
        count++;
        waitDialog = WaitDialog.show(msg);
//        ProgressDialog progressDialog =
//                ProgressDialog.show(ActivityUtil.getCurrentActivity(), "", msg, true, true);
//        ScreenUtil.setFullScreen(progressDialog);
        progressDialogMap.put(count, waitDialog);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        return count;
    }

    public static int showLoading(String msg, Context context) {
        count++;
         waitDialog = WaitDialog.show(msg);
        // ProgressDialog progressDialog = ProgressDialog.show(context, "", msg, true, true);
//        ScreenUtil.setFullScreen(progressDialog);
        progressDialogMap.put(count, waitDialog);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        return count;
    }

    public static int showLoading(String msg, Context context, int id) {
        count++;
         waitDialog = WaitDialog.show(msg);
        //ProgressDialog progressDialog = ProgressDialog.show(context, "", msg, true, true);
//        ScreenUtil.setFullScreen(progressDialog);
        progressDialogMap.put(count, waitDialog);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        return count;
    }

    public static String getString(int id) {
        return getApplication().getResources().getString(id);
    }

    public static int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getApplication().getResources().getColor(id, getApplication().getTheme());
        } else {
            //noinspection deprecation
            return getApplication().getResources().getColor(id);
        }
    }

//    public static int showLoading(Activity activity, String msg) {
//        count++;
//        ProgressDialog progressDialog = ProgressDialog.show(activity, "", msg, true, true);
//        progressDialogMap.put(count, progressDialog);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        return count;
//    }

    public static void dismissLoading() {
        if (waitDialog != null) {
            count--;
            if (count == 0) {
                if (waitDialog != null && waitDialog.isShow()) {
                    waitDialog.dismiss();
                }
                waitDialog = null;
            }
        }
        //        if (progressDialog != null && progressDialog.isShowing()) {
        //            progressDialog.dismiss();
        //        }
        //
    }

    public static void dismissLoading(int id) {
        if (progressDialogMap.get(id) != null) {
            WaitDialog waitDialog = progressDialogMap.get(id);
            if (waitDialog != null) {
                waitDialog.dismiss();
            }
        }
    }

    public static void closeSoftKeyboard() {
        InputMethodManager inputManger =
                (InputMethodManager)
                        ActivityUtil.getCurrentActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManger != null) {
            inputManger.hideSoftInputFromWindow(
                    ActivityUtil.getCurrentActivity().getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    public static void openSoftKeyboard(EditText editText) {
        InputMethodManager inputManger =
                (InputMethodManager)
                        ActivityUtil.getCurrentActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManger != null) {
            inputManger.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 实现文本复制功能
     *
     * @param content 复制的文本
     */
    public static void copy(String content) {
        if (!TextUtils.isEmpty(content)) {
            // 得到剪贴板管理器
            ClipboardManager clipboard =
                    (ClipboardManager)
                            ActivityUtil.getCurrentActivity()
                                    .getSystemService(Context.CLIPBOARD_SERVICE);
            // 参数一：标签  参数二：要复制到剪贴板的文本
            ClipData clip = ClipData.newPlainText("copy text", content);
            clipboard.setPrimaryClip(clip);
        }
    }
}
