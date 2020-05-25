package it.unito.ium.myreps.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.ui.BaseFragment;

public final class ProfileFragment extends BaseFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return bindView(R.layout.fragment_profile, inflater, container);
    }
}
