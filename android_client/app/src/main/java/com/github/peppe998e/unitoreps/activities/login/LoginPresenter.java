package com.github.peppe998e.unitoreps.activities.login;

import com.github.peppe998e.unitoreps.CoreModel;
import com.github.peppe998e.unitoreps.activities.base.BasePresenter;

class LoginPresenter extends BasePresenter<LoginContract.View, CoreModel> implements LoginContract.Presenter {
    LoginPresenter(LoginContract.View view, CoreModel model) {
        super(view, model);
    }


}
