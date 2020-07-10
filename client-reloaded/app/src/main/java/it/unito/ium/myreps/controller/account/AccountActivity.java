package it.unito.ium.myreps.controller.account;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.component.account.AccountPagerAdapter;
import it.unito.ium.myreps.component.main.LessonListPageAdapter;
import it.unito.ium.myreps.controller.BaseActivity;

public final class AccountActivity extends BaseActivity {
    @BindView(R.id.activity_main_frags_container)
    ViewPager2 viewPager;

    @BindView(R.id.activity_main_bnav)
    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initPageViewer();
    }

    private void initPageViewer() {
        AccountPagerAdapter accountPagerAdapter = new AccountPagerAdapter(getSupportFragmentManager(),
                getLifecycle(), getSupportActionBar());
        accountPagerAdapter.bindBottomNavigationView(bottomNavView);
        accountPagerAdapter.bindViewPager(viewPager);
    }
}
