package it.unito.ium.myreps.controllers;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.components.RecyclerViewAdapter;
import it.unito.ium.myreps.components.RecyclerViewRow;

public final class MainController extends BaseController {
    private SearchView searchView;
    private RecyclerViewAdapter repsRecViewAdapter;

    @BindView(R.id.swipe_refresh_reps)
    SwipeRefreshLayout swipeRefreshReps;
    @BindView(R.id.recycler_view_reps)
    RecyclerView repsRecView;

    @BindView(R.id.fab_my_reps_layout)
    LinearLayout fabMyRepsLayout;
    @BindView(R.id.fab_my_reps_text_view)
    TextView fabMyRepsTextView;

    @BindView(R.id.fab_all_reps_layout)
    LinearLayout fabAllRepsLayout;
    @BindView(R.id.fab_all_reps_text_view)
    TextView fabAllRepsTextView;

    @BindView(R.id.fab_action_button)
    FloatingActionButton fabActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view); // Set Content View
        ButterKnife.bind(this);

        initRecyclerViewReps();
        loadRepsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                repsRecViewAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                repsRecViewAdapter.getFilter().filter(query);
                return true;
            }
        });

        return true;
    }

    private void closeSearchView() {
        if (!searchView.hasFocus()) return;

        searchView.clearFocus();
        searchView.setIconified(true);

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_profile:
                startActivity(new Intent(this, UserController.class));
                return true;
            case R.id.menu_item_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) closeSearchView();
        else super.onBackPressed();
    }

    @OnClick(R.id.fab_action_button)
    public void fabActionOnClick(FloatingActionButton fab) {
        if (fab.getRotation() == 0) { // showFabMenu
            fabActionButton.animate().rotation(135);

            fabMyRepsLayout.setVisibility(View.VISIBLE);
            fabMyRepsLayout.animate().translationY(-350).withStartAction(() ->
                    fabMyRepsTextView.setVisibility(View.VISIBLE));

            fabAllRepsLayout.setVisibility(View.VISIBLE);
            fabAllRepsLayout.animate().translationY(-180).withStartAction(() ->
                    fabAllRepsTextView.setVisibility(View.VISIBLE));
        } else {
            hideFabMenu();
        }
    }

    private void hideFabMenu() {
        fabActionButton.animate().rotation(0);

        fabAllRepsLayout.animate().translationY(0).withEndAction(() ->
                fabAllRepsLayout.setVisibility(View.GONE));
        fabAllRepsTextView.setVisibility(View.GONE);

        fabMyRepsLayout.animate().translationY(0).withEndAction(() ->
                fabMyRepsLayout.setVisibility(View.GONE));
        fabMyRepsTextView.setVisibility(View.GONE);
    }

    private void initRecyclerViewReps() {
        repsRecView.setHasFixedSize(true);
        repsRecView.setLayoutManager(new LinearLayoutManager(this));

        repsRecViewAdapter = new RecyclerViewAdapter();
        repsRecView.setAdapter(repsRecViewAdapter);
        repsRecViewAdapter.setItemClickListener((view, item) -> {
            Intent i = new Intent(this, LessonController.class);
            i.putExtra("lesson", item);
            startActivity(i);
        });

        swipeRefreshReps.setOnRefreshListener(() -> {
            closeSearchView();
            loadRepsList();
        });
    }

    @OnClick(R.id.fab_my_reps_button)
    public void loadMyRepsList() {
        getSupportActionBar().setTitle("My booked lessons");

        Toast.makeText(this, "MY REPS", Toast.LENGTH_SHORT).show();

        hideFabMenu();
    }

    @OnClick(R.id.fab_all_reps_button)
    public void loadAllRepsList() {
        getSupportActionBar().setTitle("All available lessons");

        Toast.makeText(this, "ALL REPS", Toast.LENGTH_SHORT).show();

        hideFabMenu();
    }

    private void loadRepsList() {
        swipeRefreshReps.setRefreshing(true);

        ArrayList<RecyclerViewRow> repsList = new ArrayList<>();
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 170));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsRecViewAdapter.setDataSet(repsList);

        swipeRefreshReps.setRefreshing(false);
    }
}
