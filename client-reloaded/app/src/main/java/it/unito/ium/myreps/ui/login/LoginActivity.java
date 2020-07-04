package it.unito.ium.myreps.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.config.KVConfiguration;
import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.api.SrvStatus;
import it.unito.ium.myreps.logic.storage.KVStorage;
import it.unito.ium.myreps.ui.BaseActivity;
import it.unito.ium.myreps.ui.account.AccountActivity;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.activity_login_input_email)
    TextInputEditText loginEditText;

    @BindView(R.id.activity_login_input_password)
    TextInputEditText pwdEditText;

    @BindView(R.id.activity_login_button_exec)
    Button execButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        KVStorage kvStorage = getModel().getKVStorage();
        loginEditText.setText(kvStorage.getString(KVConfiguration.ACCOUNT_EMAIL, ""));
    }

    @OnClick(R.id.activity_login_button_exec)
    public void onLoginClick() {
        ApiManager apiManager = getModel().getApiManager();
        disableView(true);

        apiManager.setCredentials(loginEditText.getText().toString(), pwdEditText.getText().toString())
                .doLogin((status, response) -> {
                    if (status == SrvStatus.OK) {
                        startActivity(new Intent(this, AccountActivity.class));
                        finish();
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, status.toString(), Toast.LENGTH_LONG).show());
                        disableView(false);
                    }
                });
    }

    private void disableView(boolean bool) {
        runOnUiThread(() -> {
            loginEditText.setEnabled(!bool);
            pwdEditText.setEnabled(!bool);
            execButton.setEnabled(!bool);
        });
    }
}
