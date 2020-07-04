package it.unito.ium.myreps.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.ui.BaseActivity;
import it.unito.ium.myreps.ui.account.AccountActivity;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.activity_login_input_email)
    TextInputEditText loginEditText;

    @BindView(R.id.activity_login_input_password)
    TextInputEditText pwdEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.activity_login_button_exec)
    public void onLoginClick(Button btn) {
        startActivity(new Intent(this, AccountActivity.class));
        finish();
    }
}
