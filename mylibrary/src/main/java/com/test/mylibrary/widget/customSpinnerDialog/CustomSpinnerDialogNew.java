package com.test.mylibrary.widget.customSpinnerDialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.test.mylibrary.R;
import com.test.mylibrary.bean.CheckedModel;
import com.test.mylibrary.widget.CustomRecycleView;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerDialogNew {
    private OnSelectItemChangedListener onSelectItemChangedListener;
    private RelativeLayout mRlHeader;
    private LinearLayout mLlDialog;
    private CustomRecycleView recyclerView;
    private List<CheckedModel> listItems = new ArrayList<>();
    private View mIvClose;
    private String title = "请选择";
    private CustomSpinnerDialogRvAdapter customDialogRvAdapter;
    private View headerView;

    private TextView mTvTitle;
    private EditText mEtFilter;
    private List<CheckedModel> allListItems;

    private BottomDialog dialog;

    public CustomSpinnerDialogNew(List<CheckedModel> selectItems){
        this.allListItems = selectItems;
        this.listItems = selectItems;
    }

    public void showDialog() {
        BottomDialog bottomDialog =
        BottomDialog.build().setCustomView(new OnBindView<BottomDialog>(R.layout.comit_customview_spinner_dialog_new) {

            @Override
            public void onBind(BottomDialog dialog, View v) {
                mLlDialog = v.findViewById(R.id.dialog_ll);
                recyclerView = v.findViewById(R.id.dialog_rv);
                mIvClose = v.findViewById(R.id.dialog_close_iv);
                mRlHeader = v.findViewById(R.id.dialog_header_rl);
                mTvTitle = v.findViewById(R.id.dialog_title_tv);
                mEtFilter = v.findViewById(R.id.etFilter);
                mEtFilter.setVisibility(View.VISIBLE);
                mEtFilter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (customDialogRvAdapter != null) {
                            List<CheckedModel> selectItemList = new ArrayList<>();
                            if (charSequence.length() == 0) {
                                customDialogRvAdapter.getData().clear();
                                customDialogRvAdapter.getData().addAll(allListItems);
                                customDialogRvAdapter.notifyDataSetChanged();
                                return;
                            }
                            for (CheckedModel allListItem : allListItems) {
                                if (allListItem.getContent().contains(charSequence)) {
                                    selectItemList.add(allListItem);
                                }
                            }
                            customDialogRvAdapter.getData().clear();
                            customDialogRvAdapter.getData().addAll(selectItemList);
                            customDialogRvAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                        mTvTitle.setText(title);
                mIvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                customDialogRvAdapter = new CustomSpinnerDialogRvAdapter(R.layout.comit_simple_spinner_item, listItems);
                customDialogRvAdapter.setOnSelectItemChangedListener(new OnSelectItemChangedListener() {
                    @Override
                    public void onSelectItemChanged(List<String> checkedItems, List<Integer> indexList) {
                        if (onSelectItemChangedListener != null) {
                            onSelectItemChangedListener.onSelectItemChanged(checkedItems, indexList);
                        }
                        dialog.dismiss();
                    }
                });
                recyclerView.setAdapter(customDialogRvAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getCustomView().getContext(), RecyclerView.VERTICAL, false));

            }
        });
        bottomDialog.setMaxHeight(1000);
        bottomDialog.setScrollableWhenContentLargeThanVisibleRange(false);
        bottomDialog.show();
    }

    public void setOnSelectItemChangedListener(OnSelectItemChangedListener onSelectItemChangedListener) {
        this.onSelectItemChangedListener = onSelectItemChangedListener;
    }

    public void setHeaderView(View headerView) {
        if (headerView == null) {
            return;
        }
        if (headerView.getParent() != null) {
            return;
        }
        this.headerView = headerView;
        mRlHeader.removeAllViews();
        mRlHeader.addView(headerView);
    }

    public void dismiss() {
        if (dialog!=null&& dialog.isShow()){
            dialog.dismiss();
        }
    }
}
