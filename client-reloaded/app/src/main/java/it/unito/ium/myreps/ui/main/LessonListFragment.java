package it.unito.ium.myreps.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.ui.BaseFragment;
import it.unito.ium.myreps.util.RecyclerViewRow;

public class LessonListFragment extends BaseFragment {
    @BindView(R.id.fragment_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_list_text_empty)
    TextView emptyText;

    private final ArrayList<RecyclerViewRow> rows;

    public LessonListFragment(ArrayList<RecyclerViewRow> rows) {
        this.rows = rows;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = bindView(R.layout.fragment_list, inflater, container);

        emptyText.setText(R.string.fragment_booking_list_rv_empty);
        loadData();

        return view;
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        // TODO
        swipeRefreshLayout.setRefreshing(false);
    }

    public String getTitle()  {
        // TODO
        return null;
    }
}
