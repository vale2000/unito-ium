package it.unito.ium.myreps.controllers.fragments;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import it.unito.ium.myreps.model.Model;
import it.unito.ium.myreps.model.services.config.ConfigKey;

abstract class BaseFragment extends Fragment {

    protected boolean isLoggedIn() {
        return getModel().getConfigManager().getString(ConfigKey.AUTH_TOKEN) != null;
    }

    protected Model getModel() {
        Activity activity = getActivity();
        return activity != null ? (Model) activity.getApplication() : null;
    }

    protected void runOnUiThread(Runnable runnable) {
        Activity activity = getActivity();
        if (activity == null) throw new NullPointerException();
        else activity.runOnUiThread(runnable);
    }
}
