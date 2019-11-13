package com.github.peppe998e.unitoreps.activities.splash;

import com.github.peppe998e.unitoreps.CoreModel;
import com.github.peppe998e.unitoreps.activities.base.BasePresenter;

class SplashPresenter extends BasePresenter<SplashContract.View, CoreModel> implements SplashContract.Presenter {
    SplashPresenter(SplashContract.View view, CoreModel model) {
        super(view, model);
    }

    @Override
    public void checkAndStart() {

    }
}
