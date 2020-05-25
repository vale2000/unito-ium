package it.unito.ium.myreps.controllers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.controllers.fragments.BaseFragment;
import it.unito.ium.myreps.controllers.fragments.BookingsFragment;
import it.unito.ium.myreps.controllers.fragments.LessonsFragment;
import it.unito.ium.myreps.controllers.fragments.ProfileFragment;

public final class MainView extends BaseView {
    @BindView(R.id.view_main_bottom_nav)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.view_main_fragments_container)
    ViewPager2 viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_main);

        FragmentsAdapter fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager(), getLifecycle(), getSupportActionBar());
        fragmentsAdapter.setPageChangeCallback(viewPager);
        fragmentsAdapter.setNavigationItemSelectedListener(bottomNavigationView);
        viewPager.setAdapter(fragmentsAdapter);
    }
/*

    private int lastItemId = -1;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        int itemId = item.getItemId();

        if (itemId == lastItemId) return false;
        lastItemId = itemId;

        if (itemId == R.id.main_bottom_nav_lessons) {
            actionBar.setTitle(getString(R.string.main_bottom_nav_lessons));
            loadFragment(new LessonsFragment());
            return true;
        } else {
            if (isLoggedIn()) {
                switch (itemId) {
                    case R.id.main_bottom_nav_profile:
                        actionBar.setTitle(getString(R.string.main_bottom_nav_profile));
                        loadFragment(new ProfileFragment());
                        return true;
                    case R.id.main_bottom_nav_bookings:
                        actionBar.setTitle(getString(R.string.main_bottom_nav_bookings));
                        loadFragment(new BookingsFragment());
                        return true;
                }
            } else {
                actionBar.setTitle(getString(R.string.main_bottom_nav_login));
                switch (itemId) {
                    case R.id.main_bottom_nav_profile:
                        loadFragment(new LoginFragment(new ProfileFragment()));
                    case R.id.main_bottom_nav_bookings:
                        loadFragment(new LoginFragment(new BookingsFragment()));
                }
                return true;
            }
        }
        return false;
    };
*/

    public final static class FragmentsAdapter extends FragmentStateAdapter {
        private final List<BaseFragment> fragments;
        private ViewPager2 viewPager;
        private BottomNavigationView bottomNavView;
        private ActionBar actionBar;


        FragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ActionBar actionBar) {
            super(fragmentManager, lifecycle);
            this.actionBar = actionBar;
            fragments = new ArrayList<>();
            fragments.add(new LessonsFragment());
            fragments.add(new BookingsFragment());
            fragments.add(new ProfileFragment());
            // fragments.add(new LoginFragment(null)); // TODO fix
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        void setNavigationItemSelectedListener(BottomNavigationView bottomNavView) {
            this.bottomNavView = bottomNavView;
            bottomNavView.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.main_bottom_nav_lessons:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.main_bottom_nav_bookings:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.main_bottom_nav_profile:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            });
        }

        void setPageChangeCallback(ViewPager2 viewPager) {
            this.viewPager = viewPager;
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    switch (position) {
                        case 0:
                            bottomNavView.setSelectedItemId(R.id.main_bottom_nav_lessons);
                            break;
                        case 1:
                            bottomNavView.setSelectedItemId(R.id.main_bottom_nav_bookings);
                            break;
                        case 2:
                            bottomNavView.setSelectedItemId(R.id.main_bottom_nav_profile);
                            break;
                    }

                    actionBar.setTitle(fragments.get(position).getTitle());
                }
            });
        }
    }
}
