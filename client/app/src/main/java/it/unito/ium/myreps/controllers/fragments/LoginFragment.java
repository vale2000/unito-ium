package it.unito.ium.myreps.controllers.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.services.api.ServerError;
import it.unito.ium.myreps.model.services.config.ConfigKey;
import it.unito.ium.myreps.model.services.config.ConfigManager;

public final class LoginFragment extends BaseFragment {
    @BindView(R.id.login_input_email)
    TextInputEditText emailEditText;

    @BindView(R.id.login_input_layout_email)
    TextInputLayout emailInputLayout;

    @BindView(R.id.login_input_password)
    TextInputEditText pwdEditText;

    @BindView(R.id.login_input_layout_password)
    TextInputLayout pwdInputLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = bindView(inflater, R.layout.fragment_login, container);

        setMenuVisibility(false);

        ConfigManager<ConfigKey> configManager = getModel().getConfigManager();
        emailEditText.setText(configManager.getString(ConfigKey.USERDATA_EMAIL));
        pwdEditText.setText(configManager.getString(ConfigKey.USERDATA_PASSWORD));

        return view;
    }

    @OnClick(R.id.login_button_exec)
    void loginBtn(MaterialButton btn) {
        String email = emailEditText.getText().toString();
        String password = pwdEditText.getText().toString();

        if (email.isEmpty()) {
            emailInputLayout.setError(getString(R.string.fragment_login_input_email_error_empty));
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).find()) {
            emailInputLayout.setError(getString(R.string.fragment_login_input_email_error_invalid));
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pwdInputLayout.setError(getString(R.string.fragment_login_input_email_error_empty));
            pwdEditText.requestFocus();
            return;
        }

        ConfigManager<ConfigKey> configManager = getModel().getConfigManager();
        getModel().getApiManager().setCredentials(email, password)
                .doLogin((valid, response) -> {
                    ServerError serverError = ServerError.SERVER_OFFLINE;
                    try {
                        if (valid) {
                            boolean ok = response.getBoolean("ok");
                            if (ok) {
                                String token = response.getString("token");

                                getModel().getApiManager().setAuthToken(token);

                                configManager.setString(ConfigKey.AUTH_TOKEN, token);
                                configManager.setString(ConfigKey.USERDATA_EMAIL, email);
                                configManager.setString(ConfigKey.USERDATA_PASSWORD, password);

                                // switchFragment(R.id.view_main_fragments_container, nextFragment);
                                return;
                            }
                            configManager.setString(ConfigKey.AUTH_TOKEN, null);

                            String error = response.getString("error");
                            serverError = ServerError.fromString(error);
                            if (serverError == null) {
                                serverError = ServerError.UNKNOWN_ERROR;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        serverError = ServerError.UNKNOWN_ERROR;
                    }

                    ServerError finalServerError = serverError;
                    runOnUiThread(() -> Toast.makeText(getContext(), finalServerError.toString(), Toast.LENGTH_LONG).show());
                });
    }

    @Override
    public String getTitle() {
        return getModel().getString(R.string.main_bottom_nav_login);
    }
}
