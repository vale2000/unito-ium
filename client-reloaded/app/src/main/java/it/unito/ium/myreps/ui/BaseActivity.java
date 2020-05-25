package it.unito.ium.myreps.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import it.unito.ium.myreps.R;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Restore AppTheme
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setSupportActionBar(findViewById(R.id.app_toolbar));
        ButterKnife.bind(this);
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
}
