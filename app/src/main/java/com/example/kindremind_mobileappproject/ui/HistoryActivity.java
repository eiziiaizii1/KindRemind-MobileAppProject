package com.example.kindremind_mobileappproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kindremind_mobileappproject.R;
import com.example.kindremind_mobileappproject.data.DeedDataManager;
import com.example.kindremind_mobileappproject.ui.adapters.CompletedDeedWithDetails;
import com.example.kindremind_mobileappproject.ui.adapters.DBAdapter;
import com.example.kindremind_mobileappproject.ui.adapters.HistoryAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Activity for displaying the history of completed deeds
 */
public class HistoryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private ListView historyListView;
    private TextView emptyStateText;
    private HistoryAdapter adapter;
    private DeedDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize data manager
        dataManager = DeedDataManager.getInstance(this);

        // Add sample data for testing (remove this in production)
        //dataManager.addSampleCompletedDeeds();

        // Find views
        bottomNavigation = findViewById(R.id.bottom_navigation);
        historyListView = findViewById(R.id.history_list);

        // Check if there's an emptyStateText view in the layout
        emptyStateText = findViewById(R.id.empty_state_text);

        // Set up bottom navigation
        setupBottomNavigation();

        // Load and display completed deeds
        loadCompletedDeeds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the data when returning to this activity
        loadCompletedDeeds();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Navigate to main activity
                    Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                    // Clear back stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Already on history, do nothing
                    return true;
                } else if (itemId == R.id.nav_deck) {
                    // Navigate to deck activity
                    startActivity(new Intent(HistoryActivity.this, DeckActivity.class));
                    return true;
                }

                return false;
            }
        });

        // Set History as selected
        bottomNavigation.setSelectedItemId(R.id.nav_history);
    }

    /**
     * Load completed deeds from the data manager and display them
     */
    private void loadCompletedDeeds() {
        List<CompletedDeedWithDetails> completedDeeds = dataManager.getCompletedDeedsWithDetails();

        // Show empty state if there are no completed deeds
        if (completedDeeds.isEmpty() && emptyStateText != null) {
            emptyStateText.setVisibility(View.VISIBLE);
            historyListView.setVisibility(View.GONE);
        } else {
            if (emptyStateText != null) {
                emptyStateText.setVisibility(View.GONE);
            }
            historyListView.setVisibility(View.VISIBLE);

            // Create and set adapter
            adapter = new HistoryAdapter(this, completedDeeds);
            historyListView.setAdapter(adapter);
        }
    }
}