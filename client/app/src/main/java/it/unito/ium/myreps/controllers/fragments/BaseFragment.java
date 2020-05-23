package it.unito.ium.myreps.controllers.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import it.unito.ium.myreps.model.Model;
import it.unito.ium.myreps.model.services.config.ConfigKey;

abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    protected View createView(LayoutInflater inflater, int layout, ViewGroup container) {
        View view = inflater.inflate(layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

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

    protected void switchFragment(int id, Fragment fragment) {
        runOnUiThread(() -> getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit());
    }
}
