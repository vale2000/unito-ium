package it.unito.ium.myreps.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.config.KVConfiguration;
import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.api.SrvStatus;
import it.unito.ium.myreps.logic.storage.KVStorage;
import it.unito.ium.myreps.ui.BaseActivity;
import it.unito.ium.myreps.ui.account.AccountActivity;
import it.unito.ium.myreps.ui.login.LoginActivity;

public final class MainActivity extends BaseActivity {
    private LessonListAdapter lessonListAdapter;

    @BindView(R.id.fragment_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_list_text_empty)
    TextView emptyText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Restore App Theme
        setContentView(R.layout.activity_main);

        emptyText.setText(R.string.activity_main_rv_empty);

        initLessonList();
        loadLessonList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        KVStorage kvStorage = getModel().getKVStorage();
        boolean loggedIn = kvStorage.getString(KVConfiguration.ACCOUNT_JWT) != null;
        if (item.getItemId() == R.id.activity_main_account_goto) {
            startActivity(new Intent(this, loggedIn ? AccountActivity.class : LoginActivity.class));
            return true;
        }
        return false;
    }

    private void initLessonList() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lessonListAdapter = new LessonListAdapter();
        recyclerView.setAdapter(lessonListAdapter);
        lessonListAdapter.setItemClickListener((view, item) -> {
            // TODO on CLICK
        });

        swipeRefreshLayout.setOnRefreshListener(this::loadLessonList);
    }

    private void loadLessonList() {
        swipeRefreshLayout.setRefreshing(true);

        ApiManager apiManager = getModel().getApiManager();
        apiManager.loadLessonList((status, response) -> runOnUiThread(() -> {
            if (status == SrvStatus.OK) lessonListAdapter.setDataSet(response);
            else Toast.makeText(this, status.toString(), Toast.LENGTH_LONG).show();

            emptyText.setVisibility((response == null || response.isEmpty()) ? View.VISIBLE : View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }));
    }
}
