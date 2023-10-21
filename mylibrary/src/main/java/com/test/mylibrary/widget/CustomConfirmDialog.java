package com.test.mylibrary.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.test.mylibrary.R;
import com.test.mylibrary.util.ActivityUtil;
import com.test.mylibrary.util.ScreenUtil;

public class CustomConfirmDialog extends AlertDialog {

    private ImageView mIvClose;
    private OnBtnClickListener onBtnClickListener;
    private Button mBtnCancel;
    private Button mBtnConfirm;
    private TextView mTvContent;
    private String content;
    private ConstraintLayout mDialog;
    private String confirmString = "确认";
    private String cancelString = "取消";
    private boolean isNeedCloseListener = true;

    public CustomConfirmDialog(Context context, String content) {
        super(context);
        this.content = content;
    }

    public CustomConfirmDialog(
            Context context,
            String content,
            String confirmString,
            String cancelString,
            boolean isNeedCloseListener) {
        super(context);
        this.content = content;
        this.confirmString = confirmString;
        this.cancelString = cancelString;
        this.isNeedCloseListener = isNeedCloseListener;
    }

    public CustomConfirmDialog(
            Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow()
//                .setFlags(
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.comit_dialog_confirm);
        initView();
        initContentTv();
        initCloseBtn();
        initConfirmBtnClick();
        initCancelBtnClick();
    }

    private void initView() {
        mDialog = findViewById(R.id.dialog);
    }

    @Override
    public void show() {
        super.show();
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        // 重置窗口属性为默认值
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        ScreenUtil.setFullScreen(this);
        getWindow()
                .setLayout(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER); // window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        getWindow().setAttributes(params);
//        getWindow()
//                .getDecorView()
//                .setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        //        setView(layout, 0, 0, 0, 0);
        //        setHeaderView(spinnerHeaderView);
    }

    /**
     * 设置提示内容
     *
     * @param content
     */
    public void setTipContent(String content) {
        this.content = content;
        mTvContent.setText(content);
    }

    private void initContentTv() {
        mTvContent = findViewById(R.id.dialog_tip_tv);
        mTvContent.setText(content);
        mTvContent.setMaxHeight(ScreenUtil.dip2px(getContext(), 300));
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void initCloseBtn() {
        mIvClose = findViewById(R.id.dialog_close_iv);
        mIvClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (onBtnClickListener != null) {
                            onBtnClickListener.onCancelBtnClick(mBtnCancel);
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        if (onBtnClickListener != null && isNeedCloseListener) {
            onBtnClickListener.onCancelBtnClick(mBtnCancel);
        }
        super.onStop();
    }

    private void initConfirmBtnClick() {
        mBtnConfirm = findViewById(R.id.dialog_confirm_btn);
        mBtnConfirm.setText(confirmString);
        mBtnConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onBtnClickListener != null) {
                            onBtnClickListener.onConfirmBtnClick(mBtnConfirm);
                        }
                    }
                });
    }

    private void initCancelBtnClick() {
        mBtnCancel = findViewById(R.id.dialog_cancel_btn);
        mBtnCancel.setText(cancelString);
        mBtnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (onBtnClickListener != null) {
                            onBtnClickListener.onCancelBtnClick(mBtnCancel);
                        }
                    }
                });
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float x = mDialog.getLeft();
        float y = mDialog.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() < x || event.getX() > x + mDialog.getWidth()) {
                dismiss();
            }
            if (event.getY() < y || event.getY() > y + mDialog.getHeight()) {
                dismiss();
            }
        }
        return super.onTouchEvent(event);
    }

    public void setConfirmTitle(String s) {
        if (mBtnConfirm != null) {
            mBtnConfirm.setText(s);
        }
    }

    public void setBtnCancelTitle(String s) {
        if (mBtnCancel != null) {
            mBtnCancel.setText(s);
        }
    }

    public interface OnBtnClickListener {
        void onConfirmBtnClick(Button confirmBtn);

        void onCancelBtnClick(Button cancelBtn);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ScreenUtil.setFullScreen(ActivityUtil.getCurrentActivity());
    }
}
