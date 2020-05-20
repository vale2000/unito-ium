package it.unito.ium.myreps.controllers;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.controllers.fragments.BookingsFragment;
import it.unito.ium.myreps.controllers.fragments.LessonsFragment;
import it.unito.ium.myreps.controllers.fragments.LoginFragment;
import it.unito.ium.myreps.controllers.fragments.ProfileFragment;

public final class MainView extends BaseView {
    @BindView(R.id.main_bottom_nav)
    public BottomNavigationView bottomNavigationView;

    private ActionBar actionBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            actionBar.setTitle("Available lessons");
            loadFragment(new LessonsFragment());
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        int itemId = item.getItemId();

        if (itemId == R.id.main_bottom_nav_lessons) {
            actionBar.setTitle("Available lessons");
            loadFragment(new LessonsFragment());
            return true;
        } else {
            if (isLoggedIn()) {
                switch (itemId) {
                    case R.id.main_bottom_nav_profile:
                        actionBar.setTitle("Profile");
                        loadFragment(new ProfileFragment());
                        return true;
                    case R.id.main_bottom_nav_bookings:
                        actionBar.setTitle("Bookings");
                        loadFragment(new BookingsFragment());
                        return true;
                }
            } else {
                actionBar.setTitle("Login");
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

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_container, fragment)
                // .addToBackStack(null) // Add the fragment to backpress history
                .commit();
    }
}
