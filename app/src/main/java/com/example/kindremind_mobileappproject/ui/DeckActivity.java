package com.example.kindremind_mobileappproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kindremind_mobileappproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity for displaying additional deeds with category filtering
 * This is a placeholder implementation
 */
public class DeckActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FloatingActionButton addDeedFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        // Find views
        bottomNavigation = findViewById(R.id.bottom_navigation);
        addDeedFab = findViewById(R.id.fab_add_deed);

        // Set up bottom navigation
        setupBottomNavigation();

        // Set up FAB
        addDeedFab.setOnClickListener(v -> {
            // Navigate to add deed activity
            startActivity(new Intent(DeckActivity.this, AddDeedActivity.class));
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Navigate to main activity
                    Intent intent = new Intent(DeckActivity.this, MainActivity.class);
                    // Clear back stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Navigate to history activity
                    startActivity(new Intent(DeckActivity.this, HistoryActivity.class));
                    return true;
                } else if (itemId == R.id.nav_deck) {
                    // Already on deck, do nothing
                    return true;
                }

                return false;
            }
        });

        // Set Deck as selected
        bottomNavigation.setSelectedItemId(R.id.nav_deck);
    }
}