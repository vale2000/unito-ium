package it.unito.ium.myreps.component.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import it.unito.ium.myreps.controller.main.LessonListFragment;

public final class LessonListPageAdapter extends FragmentStateAdapter {
    private final ArrayList<LessonListFragment> fragments;
    private ViewPager2 viewPager;

    public LessonListPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<LessonListFragment> fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
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

    public void bindTabLayout(TabLayout tabLayout) {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String tabTitle = fragments.get(position).getTitle();
            tab.setText(tabTitle);
        }).attach();
    }

    public void bindViewPager(ViewPager2 viewPager) {
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
    }
}
