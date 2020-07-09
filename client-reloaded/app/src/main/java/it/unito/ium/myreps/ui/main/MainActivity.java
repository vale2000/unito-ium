package it.unito.ium.myreps.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.constants.StorageConstants;
import it.unito.ium.myreps.logic.storage.KVStorage;
import it.unito.ium.myreps.ui.BaseActivity;
import it.unito.ium.myreps.ui.account.AccountActivity;
import it.unito.ium.myreps.ui.login.LoginActivity;

public final class MainActivity extends BaseActivity {
    @BindView(R.id.activity_main_viewpager)
    ViewPager2 viewPager;

    @BindView(R.id.activity_main_tablt)
    TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Restore App Theme
        setContentView(R.layout.activity_main);

        LessonListPageAdapter lessonListPageAdapter = new LessonListPageAdapter(getSupportFragmentManager(), getLifecycle());
        lessonListPageAdapter.bindViewPager(viewPager);
        lessonListPageAdapter.bindTabLayout(tabLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        KVStorage kvStorage = getModel().getKVStorage();
        boolean loggedIn = kvStorage.getString(StorageConstants.ACCOUNT_JWT) != null;
        if (item.getItemId() == R.id.activity_main_account_goto) {
            startActivity(new Intent(this, loggedIn ? AccountActivity.class : LoginActivity.class));
            return true;
        }
        return false;
    }
}
