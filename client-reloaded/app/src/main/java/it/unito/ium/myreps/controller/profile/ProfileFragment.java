package it.unito.ium.myreps.controller.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.configuration.StorageConf;
import it.unito.ium.myreps.model.api.ApiManager;
import it.unito.ium.myreps.model.api.SrvStatus;
import it.unito.ium.myreps.model.storage.KVStorage;
import it.unito.ium.myreps.controller.BaseFragment;

public final class ProfileFragment extends BaseFragment {
    @BindView(R.id.fragment_profile_loading)
    View profileLoading;

    @BindView(R.id.fragment_profile_layout)
    View profileLayout;

    @BindView(R.id.fragment_profile_name)
    TextView profileName;

    @BindView(R.id.fragment_profile_surname)
    TextView profileSurname;

    @BindView(R.id.fragment_profile_email)
    TextView profileEmail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = bindView(R.layout.fragment_profile, inflater, container);
        setHasOptionsMenu(true);
        loadProfileData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fragment_profile_menu_logout) {
            logOutAccount();
            return true;
        }

        return false;
    }

    private void logOutAccount() {
        KVStorage kvStorage = getModel().getKVStorage();
        kvStorage.setString(StorageConf.ACCOUNT_JWT, null);
        kvStorage.setString(StorageConf.ACCOUNT_ID, null);
        getActivity().finish();
    }

    private void loadProfileData() {
        ApiManager apiManager = getModel().getApiManager();

        apiManager.loadProfile((status, response) -> {
            if (status == SrvStatus.OK) {
                runOnUiThread(() -> {
                    profileLoading.setVisibility(View.GONE);

                    profileName.setText(response.getName());
                    profileSurname.setText(response.getSurname());
                    profileEmail.setText(response.getEmail());

                    profileLayout.setVisibility(View.VISIBLE);
                });
            } else {
                runOnUiThread(() -> Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
