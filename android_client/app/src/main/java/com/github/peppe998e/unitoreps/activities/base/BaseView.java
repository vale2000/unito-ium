package com.github.peppe998e.unitoreps.activities.base;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This abstract class is a support (optional) class for the various VIEWS,
 * in order to avoid boilerplate code
 *
 * This abstract class replace (extends) AppCompatActivity
 *
 * @param <P> ActivityContract.Presenter contract (that MUST extends BaseContract.Presenter)
 */
public abstract class BaseView<P extends BaseContract.Presenter> extends AppCompatActivity {
    private P presenter;

    public BaseView() {
        this.presenter = null;
    }

    protected void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    protected P getPresenter() {
        return this.presenter;
    }

}