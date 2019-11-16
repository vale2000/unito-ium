package com.github.peppe998e.unitoreps.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.peppe998e.unitoreps.CoreModel;
import com.github.peppe998e.unitoreps.R;
import com.github.peppe998e.unitoreps.activities.base.BaseView;
import com.github.peppe998e.unitoreps.activities.login.LoginView;
import com.github.peppe998e.unitoreps.activities.main.MainView;

public class SplashView extends BaseView<SplashContract.Presenter> implements SplashContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setPresenter(new SplashPresenter(this, (CoreModel)getApplicationContext()));

        // Checks for cached auth data
        getPresenter().checkAndStart();
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

    @Override
    public void openLoginView(Bundle bundle) {
        Intent i = new Intent(this, LoginView.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void openMainView(Bundle bundle) {
        Intent i = new Intent(this, MainView.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}
