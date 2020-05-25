package it.unito.ium.myreps.controllers;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.Model;
import it.unito.ium.myreps.model.services.config.ConfigKey;

abstract class BaseView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Restore AppTheme
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setSupportActionBar(findViewById(R.id.app_toolbar));
        setActionBar(null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((getSupportActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0) {
            if (item.getItemId() == android.R.id.home) {
                super.onBackPressed();
                return true;
            }
        }
        return false;
    }

    protected boolean isLoggedIn() {
        return getModel().getConfigManager().getString(ConfigKey.AUTH_TOKEN) != null;
    }

    protected Model getModel() {
        return (Model) getApplication();
    }
}
