package it.unito.ium.myreps.component.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.controller.BaseFragment;
import it.unito.ium.myreps.controller.booking.BookingListFragment;
import it.unito.ium.myreps.controller.profile.ProfileFragment;

public final class AccountPagerAdapter extends FragmentStateAdapter {
    private final List<BaseFragment> fragmentList;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavView;
    private ActionBar actionBar;


    public AccountPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ActionBar actionBar) {
        super(fragmentManager, lifecycle);
        this.actionBar = actionBar;
        fragmentList = new ArrayList<>();

        fragmentList.add(new ProfileFragment());
        fragmentList.add(new BookingListFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void bindBottomNavigationView(BottomNavigationView bottomNavView) {
        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.activity_account_bnav_profile:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.activity_account_bnav_bookings:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        });

        this.bottomNavView = bottomNavView;
    }

    public void bindViewPager(ViewPager2 viewPager) {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavView.setSelectedItemId(R.id.activity_account_bnav_profile);
                        actionBar.setTitle(R.string.activity_account_bnav_profile);
                        break;
                    case 1:
                        bottomNavView.setSelectedItemId(R.id.activity_account_bnav_bookings);
                        actionBar.setTitle(R.string.activity_account_bnav_bookings);
                        break;
                }
            }
        });

        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
    }
}
