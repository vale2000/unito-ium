package com.github.peppe998e.unitoreps.activities.login;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.peppe998e.unitoreps.CoreModel;
import com.github.peppe998e.unitoreps.R;
import com.github.peppe998e.unitoreps.activities.base.BaseView;

public class LoginView extends BaseView<LoginContract.Presenter> implements LoginContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setPresenter(new LoginPresenter(this, (CoreModel)getApplicationContext()));
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
