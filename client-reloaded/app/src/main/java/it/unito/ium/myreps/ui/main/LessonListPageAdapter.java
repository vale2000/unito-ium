package it.unito.ium.myreps.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class LessonListPageAdapter extends FragmentStateAdapter {
    private final ArrayList<LessonListFragment> fragments;
    private ViewPager2 viewPager;

    public LessonListPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragments = new ArrayList<>();

        LocalDateTime todayDate = LocalDate.now().atStartOfDay();
        Instant todayInstant = todayDate.toInstant(ZoneOffset.UTC);
        long today = todayInstant.getEpochSecond();

        fragments.add(new LessonListFragment(today + 86400));  // Domani
        fragments.add(new LessonListFragment(today + 172800)); // Dopo domani
        fragments.add(new LessonListFragment(today + 259200)); // ...
        fragments.add(new LessonListFragment(today + 345600));
        fragments.add(new LessonListFragment(today + 432000));
        fragments.add(new LessonListFragment(today + 518400));
        fragments.add(new LessonListFragment(today + 604800));
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
