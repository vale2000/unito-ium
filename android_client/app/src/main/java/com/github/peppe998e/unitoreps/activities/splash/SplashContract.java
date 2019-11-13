package com.github.peppe998e.unitoreps.activities.splash;

import android.os.Bundle;

import com.github.peppe998e.unitoreps.activities.base.BaseContract;

public interface SplashContract {

    interface View extends BaseContract.View {
        void openLoginView(Bundle bundle);
        void openMainView(Bundle bundle);
    }

    interface Presenter extends BaseContract.Presenter {
        void checkAndStart();
    }

}
