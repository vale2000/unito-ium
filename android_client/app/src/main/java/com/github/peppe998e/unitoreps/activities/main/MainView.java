package com.github.peppe998e.unitoreps.activities.main;

import android.os.Bundle;
import android.widget.Toast;

import com.github.peppe998e.unitoreps.CoreModel;
import com.github.peppe998e.unitoreps.R;
import com.github.peppe998e.unitoreps.activities.base.BaseView;

/**
 * Example class that extends BaseView and implements its CONTRACT
 *
 */
public class MainView extends BaseView<MainContract.Presenter> implements MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPresenter(new MainPresenter(this, (CoreModel)getApplicationContext()));
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
