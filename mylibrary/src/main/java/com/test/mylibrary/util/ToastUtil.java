package com.test.mylibrary.util;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.test.mylibrary.R;
import com.test.mylibrary.widget.ComitTextView;

/**
 * @Author xucanyou666 @Date 2020/4/27 11:35 email：913710642@qq.com
 */
public class ToastUtil {
    private static Toast toast;

    public ToastUtil() {}

    public static void showToast(final String msg) {
        Log.e("弹出toast", "showToast: " + msg);
        if ("main".equals(Thread.currentThread().getName())) {
            createToast(msg);
        } else {
            ActivityUtil.getCurrentActivity().runOnUiThread(() -> ToastUtil.createToast(msg));
        }
    }

    public static CustomDialog showToast2(String msg) {
        Log.e("弹出toast", "showToast2: " + msg);
        return CustomDialog.show(new OnBindView<CustomDialog>(R.layout.comit_dialog_confirm) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        Button btnOk;
                        btnOk = v.findViewById(R.id.dialog_confirm_btn);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        Button btnCancel;
                        btnCancel = v.findViewById(R.id.dialog_cancel_btn);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        ComitTextView comitTextView = v.findViewById(R.id.dialog_tip_tv);
                        comitTextView.setText(msg);
                    }
                })
                //.setAnimResId(R.anim.anim_right_in, R.anim.anim_right_out)
                .setMaskColor(XUtil.getColor(android.R.color.darker_gray));

    }

    public static CustomDialog showToast2(String msg, View.OnClickListener onConfirmClickListener) {
        Log.e("弹出toast", "showToast2: " + msg);
        return CustomDialog.show(new OnBindView<CustomDialog>(R.layout.comit_dialog_confirm) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        Button btnOk;
                        btnOk = v.findViewById(R.id.dialog_confirm_btn);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (onConfirmClickListener!=null){
                                    onConfirmClickListener.onClick(view);
                                }
                                dialog.dismiss();
                            }
                        });

                        Button btnCancel;
                        btnCancel = v.findViewById(R.id.dialog_cancel_btn);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        ComitTextView comitTextView = v.findViewById(R.id.dialog_tip_tv);
                        comitTextView.setText(msg);
                    }
                })
                //.setAnimResId(R.anim.anim_right_in, R.anim.anim_right_out)
                .setMaskColor(XUtil.getColor(android.R.color.darker_gray));

    }

    @SuppressLint("ShowToast")
    private static void createToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(XUtil.getApplication(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        //        LinearLayout linearLayout = (LinearLayout) toast.getView();
        //        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        //        messageTextView.setTextSize(15.0F);
        toast.show();
    }

    public static void showCenterToast(final String msg) {
        if ("main".equals(Thread.currentThread().getName())) {
            createCenterToast(msg);
        } else {
            ActivityUtil.getCurrentActivity().runOnUiThread(() -> ToastUtil.createCenterToast(msg));
        }
    }

    @SuppressLint("ShowToast")
    private static void createCenterToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(XUtil.getApplication(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }

        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        toast.setGravity(17, 0, 0);
        messageTextView.setTextSize(15.0F);
        toast.show();
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
