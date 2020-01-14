package it.unito.ium.myreps.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.controllers.components.RecyclerViewAdapter;

public class MainController extends BaseController {
    private RecyclerView repsRecyclerView;

    private FloatingActionButton fabActionButton;

    private LinearLayout fab1Layout;
    private TextView fab1TextView;
    private FloatingActionButton fab1Button;

    private LinearLayout fab2Layout;
    private TextView fab2TextView;
    private FloatingActionButton fab2Button;

    private LinearLayout fabRefreshLayout;
    private TextView fabRefreshTextView;
    private FloatingActionButton fabRefreshButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); // Set Content View
        findViewWidgets(); // Find and attach all Widgets
        initViewWidgets(); // Initialize all widgets
    }

    private void findViewWidgets() {
        // Recycler View
        repsRecyclerView = findViewById(R.id.main_reps_recycler_view);

        // Floating Action Buttons
        fabActionButton = findViewById(R.id.fab_action_button);

        fab1Layout = findViewById(R.id.fab1_layout);
        fab1TextView = findViewById(R.id.fab1_text_view);
        fab1Button = findViewById(R.id.fab1_button);

        fab2Layout = findViewById(R.id.fab2_layout);
        fab2TextView = findViewById(R.id.fab2_text_view);
        fab2Button = findViewById(R.id.fab2_button);

        fabRefreshLayout = findViewById(R.id.fab_refresh_layout);
        fabRefreshTextView = findViewById(R.id.fab_refresh_text_view);
        fabRefreshButton = findViewById(R.id.fab_refresh_button);
    }

    private void initViewWidgets() {
        initRepsRecyclerView();
        initFloatingActionButtons();
    }

    private void initRepsRecyclerView() {
        repsRecyclerView.setHasFixedSize(true);
        repsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String[]> repsList = new ArrayList<>();
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});
        repsList.add(new String[]{"Programmazione 1", "2h", "Giuseppe Eletto", ""});
        repsList.add(new String[]{"Sistemi Operativi", "3h", "Matteo Brunello", ""});
        repsList.add(new String[]{"Base di dati", "1h", "Edoardo Chiavazza", ""});
        repsList.add(new String[]{"Algebra I", "1.3h", "Mariagrazia di Montefaso", ""});
        repsList.add(new String[]{"Filosofia di pensiero", "3h", "Berlusconi Silvio Premier", ""});

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(repsList);
        repsRecyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setItemClickListener((view, item) ->
                Toast.makeText(view.getContext(), "You clicked: " + item[0], Toast.LENGTH_SHORT).show());
    }

    private void initFloatingActionButtons() {
        fabActionButton.setOnClickListener(v -> {
            if (fabActionButton.getRotation() == 0) {
                fabActionButton.animate().rotation(135);

                fab1Layout.setVisibility(View.VISIBLE);
                fab1Layout.animate().translationY(-180).withStartAction(() ->
                        fab1TextView.setVisibility(View.VISIBLE));

                fab2Layout.setVisibility(View.VISIBLE);
                fab2Layout.animate().translationY(-350).withStartAction(() ->
                        fab2TextView.setVisibility(View.VISIBLE));

                fabRefreshLayout.setVisibility(View.VISIBLE);
                fabRefreshLayout.animate().translationY(-520).withStartAction(() ->
                        fabRefreshTextView.setVisibility(View.VISIBLE));
            } else {
                fabActionButton.animate().rotation(0);

                fab1Layout.animate().translationY(0).withEndAction(() ->
                        fab1Layout.setVisibility(View.GONE));
                fab1TextView.setVisibility(View.GONE);

                fab2Layout.animate().translationY(0).withEndAction(() ->
                        fab2Layout.setVisibility(View.GONE));
                fab2TextView.setVisibility(View.GONE);

                fabRefreshLayout.animate().translationY(0).withEndAction(() ->
                        fabRefreshLayout.setVisibility(View.GONE));
                fabRefreshTextView.setVisibility(View.GONE);
            }
        });

        fab1Button.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "FAB1", Toast.LENGTH_SHORT).show());

        fab2Button.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "FAB2", Toast.LENGTH_SHORT).show());

        fabRefreshButton.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "REFRESH", Toast.LENGTH_SHORT).show());
    }
}
