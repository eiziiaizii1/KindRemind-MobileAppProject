package com.example.kindremind_mobileappproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kindremind_mobileappproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity for displaying the history of completed deeds
 * This is a placeholder implementation
 */
public class HistoryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Find views
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Set up bottom navigation
        setupBottomNavigation();
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
}