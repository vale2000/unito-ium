package it.unito.ium.myreps.controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.configuration.StorageConf;
import it.unito.ium.myreps.model.api.ApiManager;
import it.unito.ium.myreps.model.api.SrvStatus;
import it.unito.ium.myreps.model.storage.KVStorage;
import it.unito.ium.myreps.controller.BaseActivity;
import it.unito.ium.myreps.controller.account.AccountActivity;

public final class LoginActivity extends BaseActivity {
    @BindView(R.id.activity_login_input_email)
    TextInputEditText emailEditText;

    @BindView(R.id.activity_login_input_password)
    TextInputEditText pwdEditText;

    @BindView(R.id.activity_login_button_exec)
    Button execButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        KVStorage kvStorage = getModel().getKVStorage();
        emailEditText.setText(kvStorage.getString(StorageConf.ACCOUNT_EMAIL, ""));
    }

    @OnClick(R.id.activity_login_button_exec)
    public void onLoginClick() {
        ApiManager apiManager = getModel().getApiManager();

        String email = emailEditText.getText().toString();
        String password = pwdEditText.getText().toString();

        if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.activity_login_input_email_error_empty));
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).find()) {
            emailEditText.setError(getString(R.string.activity_login_input_email_error_invalid));
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pwdEditText.setError(getString(R.string.activity_login_input_email_error_empty));
            pwdEditText.requestFocus();
            return;
        }

        disableView(true);
        apiManager.setCredentials(email, password).doLogin((status, response) -> {
            if (status == SrvStatus.OK) {
                startActivity(new Intent(this, AccountActivity.class));
                finish();
            } else {
                runOnUiThread(() -> Toast.makeText(this, status.toString(), Toast.LENGTH_SHORT).show());
                disableView(false);
            }
        });
    }

    private void disableView(boolean bool) {
        runOnUiThread(() -> {
            emailEditText.setEnabled(!bool);
            pwdEditText.setEnabled(!bool);
            execButton.setEnabled(!bool);
        });
    }
}
