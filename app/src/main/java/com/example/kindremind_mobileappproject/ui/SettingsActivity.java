package com.example.kindremind_mobileappproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kindremind_mobileappproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity for displaying and managing user settings
 */
public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Switch switchVibration;
    private TextView settingsTitle;

    // SharedPreferences constants
    public static final String PREFS_NAME = "KindRemindPrefs";
    public static final String PREF_VIBRATION_ENABLED = "vibration_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find views
        bottomNavigation = findViewById(R.id.bottom_navigation);
        switchVibration = findViewById(R.id.switch_vibration);
        settingsTitle = findViewById(R.id.deck_title);

        // Set the title
        settingsTitle.setText("Settings");

        // Set up bottom navigation
        setupBottomNavigation();

        // Set up the vibration switch
        setupVibrationSwitch();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Navigate to main activity
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    // Clear back stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Navigate to history activity
                    startActivity(new Intent(SettingsActivity.this, HistoryActivity.class));
                    return true;
                } else if (itemId == R.id.nav_deck) {
                    // Already on settings, do nothing
                    return true;
                }

                return false;
            }
        });

        // Set Deck as selected
        bottomNavigation.setSelectedItemId(R.id.nav_deck);
    }

    /**
     * Set up the vibration switch with the stored preference
     */
    private void setupVibrationSwitch() {
        // Get the stored preference
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean vibrationEnabled = sharedPreferences.getBoolean(PREF_VIBRATION_ENABLED, true);

        // Set the switch state
        switchVibration.setChecked(vibrationEnabled);

        // Set the change listener
        switchVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the new preference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_VIBRATION_ENABLED, isChecked);
            editor.apply();

            // Show a confirmation toast
            String message = isChecked ? "Vibration enabled" : "Vibration disabled";
            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}