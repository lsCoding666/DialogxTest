package com.test.mylibrary.fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.BottomDialogSlideEventLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.test.mylibrary.R;
import com.test.mylibrary.base.BaseDragFragment;
import com.test.mylibrary.base.BaseDragFragmentViewBinding;
import com.test.mylibrary.base.BasePresenter;
import com.test.mylibrary.base.BaseViewModel;
import com.test.mylibrary.bean.CheckedModel;
import com.test.mylibrary.databinding.ComitFragmentIndexBinding;
import com.test.mylibrary.util.ScreenUtil;
import com.test.mylibrary.widget.customSpinnerDialog.CustomSpinnerDialogNew;

import java.util.ArrayList;
import java.util.List;


public class IndexFragment extends BaseDragFragmentViewBinding<BasePresenter, BaseViewModel, ComitFragmentIndexBinding> {
    public static final int REQUEST_CODE_SCAN_FOR_BIND_PAPER = 5001;


    private MainFragment parentFragment;

    private static final String TAG = "IndexFragment";
    private RelativeLayout rlSignDate;
    private RelativeLayout rlSign;
    private ImageView ivSign;
    private ImageView ivSignDate;
    private Button btnCommitSign;

    public IndexFragment(MainFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    public IndexFragment() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return new BasePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comit_fragment_index;
    }

    @Override
    protected void initView() {
        ScreenUtil.setDefaultDisplay(getActivity());
        binding.dropView.setOnTouchListener(this);
        binding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initEtTemplateCodeNew();
            }
        });
        binding.btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignDialog();
            }
        });
    }

    private void showSignDialog() {
        new BottomDialog(new OnBindView<BottomDialog>(R.layout.comit_dialog_addcase_dsr_sign_date) {
            @Override
            public void onBind(BottomDialog dialog, View v) {

            }
        }).setDialogLifecycleCallback(new DialogLifecycleCallback<BottomDialog>() {
            @Override
            public void onShow(BottomDialog dialog) {
                super.onShow(dialog);
                View messageBoxView = dialog.getDialogImpl().bkg.getChildAt(0);
                // 获取视图的LayoutParams
                ViewGroup.LayoutParams params = messageBoxView.getLayoutParams();

                params.width = 500;
                params.height = 500;

                // 修改位置（在RelativeLayout中）
                if (params instanceof RelativeLayout.LayoutParams) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) params;
                    layoutParams.leftMargin = 200;
                    layoutParams.topMargin = 0;
                }

                // 应用修改后的LayoutParams
                messageBoxView.setLayoutParams(params);
            }
        }).show();
    }

    private void initEtTemplateCodeNew() {
        List<CheckedModel> selectItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            CheckedModel checkedModel = new CheckedModel(false, "测试" + i);
           selectItems.add(checkedModel);
        }
        CustomSpinnerDialogNew customSpinnerDialogNew = new CustomSpinnerDialogNew(selectItems);
        customSpinnerDialogNew.showDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected Class getViewModelClass() {
        return null;
    }

    @Override
    protected void initListener() {

    }


    @Override
    public void onResume() {
        super.onResume();
        ScreenUtil.setDefaultDisplay(getActivity());
        initListener();
        if (!(mainViewModel.getFuncFragment().getValue() instanceof FuncFragment)) {
            mainViewModel.getFuncFragment().setValue(new FuncFragment());
        }
    }

    @Override
    public ComitFragmentIndexBinding getViewBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return ComitFragmentIndexBinding.inflate(layoutInflater);
    }
}
