package it.unito.ium.myreps.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.services.api.ServerError;
import it.unito.ium.myreps.model.services.config.ConfigKey;
import it.unito.ium.myreps.model.services.config.ConfigManager;

public final class ProfileFragment extends BaseFragment {
    private Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        loadProfileData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadProfileData() {
        ConfigManager<ConfigKey> configManager = getModel().getConfigManager();
        if (configManager.getBoolean(ConfigKey.USERDATA_CACHED)) {
            loadDataToLayout();
            return;
        }

        getModel().getApiManager().getProfileInfo((valid, response) -> {
            ServerError serverError = ServerError.SERVER_OFFLINE;
            if (valid) {
                try {
                    boolean ok = response.getBoolean("ok");
                    if (ok) {
                        JSONObject data = response.getJSONObject("data");

                        configManager.setBoolean(ConfigKey.USERDATA_CACHED, true);
                        configManager.setString(ConfigKey.USERDATA_NAME, data.getString("name"));
                        configManager.setString(ConfigKey.USERDATA_SURNAME, data.getString("surname"));
                        configManager.setString(ConfigKey.USERDATA_EMAIL, data.getString("email"));

                        runOnUiThread(this::loadDataToLayout);
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

    private void loadDataToLayout() {
        ConfigManager<ConfigKey> configManager = getModel().getConfigManager();

        profileLoading.setVisibility(View.GONE);

        profileName.setText(configManager.getString(ConfigKey.USERDATA_NAME));
        profileSurname.setText(configManager.getString(ConfigKey.USERDATA_SURNAME));
        profileEmail.setText(configManager.getString(ConfigKey.USERDATA_EMAIL));

        profileLayout.setVisibility(View.VISIBLE);
    }
}
