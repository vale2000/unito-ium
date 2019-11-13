package com.github.peppe998e.unitoreps.activities.base;

/**
 * This abstract class is a support (optional) class for the various PRESENTERS,
 * in order to avoid boilerplate code
 *
 * @param <V> ActivityContract.View contract (that MUST extends BaseContract.View)
 * @param <M> ModelClass (Since it may be necessary to use a MODEL that is not shared with other PRESENTERS)
 */
public abstract class BasePresenter <V extends BaseContract.View, M> {
    private V view;
    private M model;

    public BasePresenter(V view, M model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        this.model = null;
        this.view = null;
    }

    protected V getView() {
        return this.view;
    }

    protected M getModel() {
        return this.model;
    }

}