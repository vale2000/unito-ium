package it.unito.ium.myreps.ui.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.ui.BaseFragment;

public final class BookingListFragment extends BaseFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return bindView(R.layout.fragment_list, inflater, container);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
