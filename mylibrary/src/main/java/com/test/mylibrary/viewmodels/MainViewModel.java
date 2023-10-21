package com.test.mylibrary.viewmodels;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.test.mylibrary.fragment.MainFragment;
import com.test.mylibrary.base.BaseDragFragment;
import com.test.mylibrary.base.BaseFragment;
import com.test.mylibrary.base.BaseViewModel;

public class MainViewModel extends BaseViewModel {
    private MutableLiveData<SQLiteDatabase> locationDataBase;
    private MutableLiveData<BaseFragment> funcFragment;
    private MutableLiveData<BaseDragFragment> contentFragment;

    private MutableLiveData<String> caseCode;
    private MutableLiveData<String> holdCode;

    private MutableLiveData<MainFragment> mainFragment;

    private MutableLiveData<Location> currentLocation;

    public MutableLiveData<SQLiteDatabase> getLocationDataBase() {
        if (locationDataBase == null)
            locationDataBase = new MutableLiveData<>();
        return locationDataBase;
    }

    public MutableLiveData<Location> getCurrentLocation() {
        if (currentLocation == null) {
            currentLocation = new MutableLiveData<>();
        }
        return currentLocation;
    }
    public MutableLiveData<MainFragment> getMainFragment() {
        if (mainFragment == null) {
            mainFragment = new MutableLiveData<>();
        }
        return mainFragment;
    }

    public MutableLiveData<String> getCaseCode() {
        if (caseCode == null) {
            caseCode = new MutableLiveData<>();
        }
        return caseCode;
    }

    // 是否展示左侧区域
    private MutableLiveData<Boolean> isShowFunc;

    public MutableLiveData<Boolean> getIsShowFunc() {
        if (isShowFunc == null) {
            isShowFunc = new MutableLiveData<>();
        }
        return isShowFunc;
    }

    public MutableLiveData<BaseFragment> getFuncFragment() {
        if (funcFragment == null) {
            funcFragment = new MutableLiveData<>();
        }
        return funcFragment;
    }

    public MutableLiveData<BaseDragFragment> getContentFragment() {
        if (contentFragment == null) {
            contentFragment = new MutableLiveData<>();
        }
        return contentFragment;
    }
}
