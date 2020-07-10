package it.unito.ium.myreps.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import it.unito.ium.myreps.model.Model;

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    protected View bindView(int layout, @NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public Model getModel() {
        return (Model) getActivity().getApplication();
    }

    public void runOnUiThread(Runnable action) {
        getActivity().runOnUiThread(action);
    }
}
