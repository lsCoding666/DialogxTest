package com.test.mylibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClipboardUtils;
import com.test.mylibrary.R;
import com.test.mylibrary.util.ToastUtil;

public class ComitTextView extends TextView {

    private boolean isLongClickAutoCopy = true;

    public ComitTextView(Context context) {
        this(context, null);
    }

    public ComitTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ComitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ComitTextView);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ComitTextView_isLongClickAutoCopy) {
                isLongClickAutoCopy = a.getBoolean(attr, true);
            }
        }
        a.recycle();
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isLongClickAutoCopy) {
                    ClipboardUtils.copyText(getText().toString());
                    ToastUtil.showToast("已复制到剪切板");
                }
                return false;
            }
        });
        setTextIsSelectable(true);
//        setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        setCursorVisible(false);
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                menu.removeItem(android.R.id.cut);
                menu.removeItem(android.R.id.paste);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    public void setTextSafely(CharSequence charSequence) {
        if (charSequence != null) {
            setText(charSequence);
        }
    }

    public void setTextSafely(int charSequence) {
        setText(charSequence + "");
    }

    public void setIsLongClickAutoCopy(boolean longClickCopy) {
        isLongClickAutoCopy = longClickCopy;
    }

    public boolean getIsLongClickAutoCopy(){
        return isLongClickAutoCopy;
    }
}
