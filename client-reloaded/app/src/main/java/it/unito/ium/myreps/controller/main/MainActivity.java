package it.unito.ium.myreps.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.component.main.LessonListPageAdapter;
import it.unito.ium.myreps.configuration.StorageConf;
import it.unito.ium.myreps.model.storage.KVStorage;
import it.unito.ium.myreps.controller.BaseActivity;
import it.unito.ium.myreps.controller.account.AccountActivity;
import it.unito.ium.myreps.controller.login.LoginActivity;

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

        initPageViewer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        KVStorage kvStorage = getModel().getKVStorage();
        boolean loggedIn = kvStorage.getString(StorageConf.ACCOUNT_JWT) != null;

        if (item.getItemId() == R.id.activity_main_account_goto) {
            startActivity(new Intent(this, loggedIn ? AccountActivity.class : LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initPageViewer() {
        LessonListPageAdapter lessonListPageAdapter = new LessonListPageAdapter(getSupportFragmentManager(),
                getLifecycle(), generateFragments());

        lessonListPageAdapter.bindViewPager(viewPager);
        lessonListPageAdapter.bindTabLayout(tabLayout);
    }

    private ArrayList<LessonListFragment> generateFragments() {
        ArrayList<LessonListFragment> fragments = new ArrayList<>();

        LocalDateTime todayDate = LocalDate.now().atStartOfDay();
        for (int i = 0; i < 5; i++) {
            DayOfWeek todayOfWeek = todayDate.getDayOfWeek();

            if (todayOfWeek == DayOfWeek.FRIDAY) {
                todayDate = todayDate.plusDays(3);
            } else {
                todayDate = todayDate.plusDays(1);
            }

            Instant todayInstant = todayDate.toInstant(ZoneOffset.UTC);
            long today = todayInstant.getEpochSecond();

            fragments.add(new LessonListFragment(today));
        }

        return fragments;
    }
}
