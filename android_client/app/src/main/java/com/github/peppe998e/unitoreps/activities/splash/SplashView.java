package com.github.peppe998e.unitoreps.activities.splash;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.peppe998e.unitoreps.CoreModel;
import com.github.peppe998e.unitoreps.R;
import com.github.peppe998e.unitoreps.activities.base.BaseView;

public class SplashView extends BaseView<SplashContract.Presenter> implements SplashContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        setPresenter(new SplashPresenter(this, (CoreModel)getApplicationContext()));
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy();
        super.onDestroy();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
