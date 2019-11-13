package com.github.peppe998e.unitoreps.activities.base;

/**
 * These interfaces contain all the methods that MUST be implemented
 * respectively from "ALL VIEWS" and "ALL PRESENTERS"
 *
 */
public interface BaseContract {

    interface View {
        void showToast(String text);
    }

    interface Presenter {
        void onDestroy();
    }

}
