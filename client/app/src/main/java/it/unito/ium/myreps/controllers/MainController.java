package it.unito.ium.myreps.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.swipe_refresh_reps)
    SwipeRefreshLayout swipeRefreshReps;
    @BindView(R.id.recycler_view_reps)
    RecyclerView recViewReps;

    @BindView(R.id.fab_my_reps_layout)
    LinearLayout fabMyRepsLayout;
    @BindView(R.id.fab_my_reps_text_view)
    TextView fabMyRepsTextView;
    @BindView(R.id.fab_my_reps_button)
    FloatingActionButton fabMyRepsButton;

    @BindView(R.id.fab_all_reps_layout)
    LinearLayout fabAllRepsLayout;
    @BindView(R.id.fab_all_reps_text_view)
    TextView fabAllRepsTextView;
    @BindView(R.id.fab_all_reps_button)
    FloatingActionButton fabAllRepsButton;

    @BindView(R.id.fab_action_button)
    FloatingActionButton fabActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view); // Set Content View
        ButterKnife.bind(this);

        initRecyclerViewReps();
        loadRecyclerViewReps();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_profile:
                startActivity(new Intent(this, UserController.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab_my_reps_button)
    public void fabMyRepsOnClick() {
        Toast.makeText(this, "MY REPS", Toast.LENGTH_SHORT).show();
        hideFabMenu();
    }

    @OnClick(R.id.fab_all_reps_button)
    public void fabAllRepsOnClick() {
        Toast.makeText(this, "ALL REPS", Toast.LENGTH_SHORT).show();
        hideFabMenu();
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
        } else hideFabMenu();
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
        recViewReps.setHasFixedSize(true);
        recViewReps.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshReps.setOnRefreshListener(this::loadRecyclerViewReps);
    }

    private void loadRecyclerViewReps() {
        swipeRefreshReps.setRefreshing(true);

        ArrayList<RecyclerViewRow> repsList = new ArrayList<>();
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
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));
        repsList.add(new RecyclerViewRow(0, "Programmazione 3", "Giuseppe Eletto", 180));
        repsList.add(new RecyclerViewRow(0, "Base di dati", "Edoardo Chiavazza", 150));
        repsList.add(new RecyclerViewRow(0, "Sistemi Operativi", "Matteo Brunello", 90));

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(repsList);
        recViewReps.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setItemClickListener((view, item) ->
                Toast.makeText(view.getContext(), item.getSubject(), Toast.LENGTH_SHORT).show());

        swipeRefreshReps.setRefreshing(false);
    }
}
