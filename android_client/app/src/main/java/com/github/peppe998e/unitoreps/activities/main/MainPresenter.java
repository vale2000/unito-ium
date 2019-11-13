package com.github.peppe998e.unitoreps.activities.main;

import com.github.peppe998e.unitoreps.CoreModel;
import com.github.peppe998e.unitoreps.activities.base.BasePresenter;

/**
 * Example class that extends BasePresenter and implements its CONTRACT
 *
 */
class MainPresenter extends BasePresenter<MainContract.View, CoreModel> implements MainContract.Presenter {

    MainPresenter(MainContract.View view, CoreModel model) {
        super(view, model);
    }

}
