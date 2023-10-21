package com.test.mylibrary.widget.customSpinnerDialog;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.test.mylibrary.R;
import com.test.mylibrary.bean.CheckedModel;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerDialogRvAdapter extends BaseQuickAdapter<CheckedModel, BaseViewHolder> {
    private boolean isSignalMode = true;
    private OnSelectItemChangedListener onSelectItemChangedListener;
    private List<String> checkedItems = new ArrayList<>();

    public CustomSpinnerDialogRvAdapter(int layoutResId, @Nullable List<CheckedModel> data) {
        super(layoutResId, data);
        for (CheckedModel checkedModel : data) {
            if (checkedModel.isChecked()) {
                checkedItems.add(checkedModel.getContent());
            }
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CheckedModel model) {
        baseViewHolder.setText(R.id.commit_spinner_tv, model.getContent());
        RelativeLayout relativeLayout = baseViewHolder.findView(R.id.commit_spinner_rl);
        // 设置选中背景色和☑️
        if (model.isChecked()) {
            assert relativeLayout != null;
            relativeLayout.setBackgroundResource(android.R.color.darker_gray);
            baseViewHolder.setVisible(R.id.commit_spinner_checked_iv, true);
        } else {
            relativeLayout.setBackgroundResource(android.R.color.white);
            baseViewHolder.setVisible(R.id.commit_spinner_checked_iv, false);
        }

        relativeLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isSignalMode) {
                            // 单选模式下 点击了某个item，肯定是选择上的
                            model.setChecked(true);
                            for (CheckedModel checkedModel :
                                    CustomSpinnerDialogRvAdapter.this.getData()) {
                                if (checkedModel.isChecked() && checkedModel != model) {
                                    checkedModel.setChecked(false);
                                    notifyDataSetChanged();
                                }
                            }
                            // 选择完毕后 直接触发回调，告诉上层选择条目发生了变化
                            onItemChanged();
                        } else {
                            // TODO:多选模式下 点击后反选 点击确定按钮后，再通知上层发生了变化

                        }
                        //                notifyItemChanged(getItemPosition(model));

                    }
                });
    }

    private void onItemChanged() {
        List<String> selectedItems = new ArrayList<>();
        List<Integer> selectItemIndex = new ArrayList<>();
        for (int i = 0; i < this.getData().size(); i++) {
            CheckedModel checkedModel = this.getData().get(i);
            if (checkedModel.isChecked()) {
                selectedItems.add(checkedModel.getContent());
                selectItemIndex.add(i);
            }
        }
        if (onSelectItemChangedListener != null) {
            onSelectItemChangedListener.onSelectItemChanged(selectedItems, selectItemIndex);
        }
    }

    public void setOnSelectItemChangedListener(
            OnSelectItemChangedListener onSelectItemChangedListener) {
        this.onSelectItemChangedListener = onSelectItemChangedListener;
    }
}
