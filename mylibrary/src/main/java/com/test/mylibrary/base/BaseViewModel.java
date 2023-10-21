package com.test.mylibrary.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class BaseViewModel extends ViewModel {

    private MutableLiveData<BaseFragment> currentFragment;

    private MutableLiveData<List<BaseFragment>> fragmentList;

    private MutableLiveData<Integer> finishPosition;

    private MutableLiveData<Boolean> isEditable;

    public MutableLiveData<Boolean> getIsEditable() {
        if (isEditable == null) {
            isEditable = new MutableLiveData<>();
            isEditable.setValue(true);
        }
        return isEditable;
    }

    public MutableLiveData<BaseFragment> getCurrentFragment() {
        if (currentFragment == null) {
            currentFragment = new MutableLiveData<>();
        }
        return currentFragment;
    }

    public MutableLiveData<List<BaseFragment>> getFragmentList() {
        if (fragmentList == null) {
            fragmentList = new MutableLiveData<>();
            fragmentList.setValue(new ArrayList<>());
        }
        return fragmentList;
    }

    public MutableLiveData<Integer> getFinishPosition() {
        if (finishPosition == null) {
            finishPosition = new MutableLiveData<>();
            finishPosition.setValue(0);
        }
        return finishPosition;
    }

    public void setCurrentFragment(MutableLiveData<BaseFragment> currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
