package com.test.dialogtest;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.comit.comitszzf.R;
import com.test.mylibrary.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow()
//                .setFlags(
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fl, mainFragment).commit();
    }

    @Override
    public void onAttachedToWindow() {
        //        setDefaultDisplay(this);
        super.onAttachedToWindow();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //        if (newConfig.fontScale != 1) // 非默认值
        //        {
        //            getResources();
        //        }
        //        setDefaultDisplay(this);
        super.onConfigurationChanged(newConfig);
    }

    //    @Override
    //    public Resources getResources() {
    //        Resources res = super.getResources();
    //        if (res.getConfiguration().fontScale != 1) {
    //            Configuration newConfig = new Configuration();
    //            newConfig.setToDefaults(); // 设置默认
    //            res.updateConfiguration(newConfig, res.getDisplayMetrics());
    //        }
    //        return res;
    //    }
}
