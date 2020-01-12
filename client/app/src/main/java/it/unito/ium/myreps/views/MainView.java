package it.unito.ium.myreps.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.views.components.RecyclerViewAdapter;

public class MainView extends BaseView {
    private RecyclerView repsRecyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); // Set Content View
        findViewWidgets(); // Find and attach all Widgets
        initViewWidgets(); // Initialize all widgets
    }

    private void findViewWidgets() {
        repsRecyclerView = findViewById(R.id.main_reps_recyclerview);
        floatingActionButton = findViewById(R.id.floating_action_button);
    }

    private void initViewWidgets() {
        initRepsRecyclerView();
        initFloatingActionButton();
    }

    private void initRepsRecyclerView() {
        repsRecyclerView.setHasFixedSize(true);
        repsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");

        recyclerViewAdapter = new RecyclerViewAdapter(animalNames);
        repsRecyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setItemClickListener((view, item) ->
                Toast.makeText(view.getContext(), "You clicked: " + item, Toast.LENGTH_SHORT).show());
    }

    private void initFloatingActionButton() {
        floatingActionButton.setOnClickListener(v ->
                Toast.makeText(v.getContext(),"you clicked me! aww :D", Toast.LENGTH_LONG).show());
    }
}
