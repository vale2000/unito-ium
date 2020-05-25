package it.unito.ium.myreps.controllers.fragments;

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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.services.api.ServerError;
import it.unito.ium.myreps.model.services.api.objects.User;
import it.unito.ium.myreps.model.services.config.ConfigKey;
import it.unito.ium.myreps.model.services.config.ConfigManager;

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
        View view = bindView(inflater, R.layout.fragment_profile, container);

        setHasOptionsMenu(true);
        loadProfileData();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fragment_profile_menu_disconnect) {
            logOutAccount();
            return true;
        }
        return false;
    }

    private void logOutAccount() {
        ConfigManager<ConfigKey> configManager = getModel().getConfigManager();
        configManager.setString(ConfigKey.AUTH_TOKEN, null);
        // switchFragment(R.id.view_main_fragments_container, new LoginFragment(new ProfileFragment()));
    }

    private void loadProfileData() {
        getModel().getApiManager().getProfileInfo((valid, response) -> {
            ServerError serverError = ServerError.SERVER_OFFLINE;
            if (valid) {
                try {
                    boolean ok = response.getBoolean("ok");
                    if (ok) {
                        JSONObject data = response.getJSONObject("data");

                        User userProfile = new User(data);
                        runOnUiThread(() -> {
                            profileLoading.setVisibility(View.GONE);

                            profileName.setText(userProfile.getName());
                            profileSurname.setText(userProfile.getSurname());
                            profileEmail.setText(userProfile.getEmail());

                            profileLayout.setVisibility(View.VISIBLE);
                        });
                        return;
                    }

                    String error = response.getString("error");
                    serverError = ServerError.fromString(error);
                    if (serverError == null) {
                        serverError = ServerError.UNKNOWN_ERROR;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    serverError = ServerError.UNKNOWN_ERROR;
                }
            }

            ServerError finalServerError = serverError;
            runOnUiThread(() -> Toast.makeText(getContext(), finalServerError.toString(), Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public String getTitle() {
        return getModel().getString(R.string.main_bottom_nav_profile);
    }
}
