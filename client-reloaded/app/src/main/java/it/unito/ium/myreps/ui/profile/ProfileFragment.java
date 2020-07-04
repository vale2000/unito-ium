package it.unito.ium.myreps.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.config.KVConfiguration;
import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.api.SrvStatus;
import it.unito.ium.myreps.logic.storage.KVStorage;
import it.unito.ium.myreps.ui.BaseFragment;

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
        setHasOptionsMenu(true);
        loadProfileData();
        return bindView(R.layout.fragment_profile, inflater, container);
    }

    private void logOutAccount() {
        KVStorage kvStorage = getModel().getKVStorage();
        kvStorage.setString(KVConfiguration.ACCOUNT_JWT, null);
        kvStorage.setString(KVConfiguration.ACCOUNT_ID, null);
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
                runOnUiThread(() -> Toast.makeText(getContext(), status.toString(), Toast.LENGTH_LONG).show());
            }
        });
    }
}
