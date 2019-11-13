package com.github.peppe998e.unitoreps.activities.splash;

import com.github.peppe998e.unitoreps.activities.base.BaseContract;

public interface SplashContract {

    interface View extends BaseContract.View {
        void openLoginView();
        void openMainView();
    }

    interface Presenter extends BaseContract.Presenter {
        void checkAndStart();
    }

}
