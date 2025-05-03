package com.example.kindremind_mobileappproject.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;  // Added import for View
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kindremind_mobileappproject.R;
import com.example.kindremind_mobileappproject.model.CompletedDeed;
import com.example.kindremind_mobileappproject.model.Deed;
import com.example.kindremind_mobileappproject.ui.utils.AnimationUtils;
import com.example.kindremind_mobileappproject.ui.utils.SwipeGestureDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SwipeGestureDetector.OnSwipeListener {

    // UI Components
    private CardView deedCard;
    private ImageView categoryIcon;
    private TextView deedText;
    private ImageView coinAnimation;
    private BottomNavigationView bottomNavigation;
    private Button btnSkip;
    private Button btnComplete;

    // Current deed
    private Deed currentDeed;

    // Skips counter
    private int skipsRemaining = 3;

    // Vibrator service
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initViews();

        // Set up swipe gesture detection
        setupSwipeDetection();

        // Set up click listeners
        setupClickListeners();

        // Set up bottom navigation
        setupBottomNavigation();

        // Load today's deed
        loadTodaysDeed();

        // Get vibrator service
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Make sure coin animation is initially invisible
        coinAnimation.setVisibility(View.INVISIBLE);
    }

    private void initViews() {
        deedCard = findViewById(R.id.deed_card);
        categoryIcon = findViewById(R.id.category_icon);
        deedText = findViewById(R.id.deed_text);
        coinAnimation = findViewById(R.id.coin_animation);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        btnSkip = findViewById(R.id.btn_skip);
        btnComplete = findViewById(R.id.btn_complete);
    }

    private void setupSwipeDetection() {
        SwipeGestureDetector swipeDetector = new SwipeGestureDetector(this, this);
        deedCard.setOnTouchListener(swipeDetector);
    }

    private void setupClickListeners() {
        btnComplete.setOnClickListener(v -> completeDeed());
        btnSkip.setOnClickListener(v -> skipDeed());
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Already on home, do nothing
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Navigate to history activity
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                    return true;
                } else if (itemId == R.id.nav_deck) {
                    // Navigate to deck activity
                    startActivity(new Intent(MainActivity.this, DeckActivity.class));
                    return true;
                }

                return false;
            }
        });

        // Set Home as selected
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    private void loadTodaysDeed() {
        // For now, create a temporary deed
        // In a real implementation, this would come from your repository
        currentDeed = new Deed(1, "environment", "Unplug one idle device to save energy.");

        // Update UI with the deed details
        updateDeedUI();
    }

    private void updateDeedUI() {
        if (currentDeed != null) {
            deedText.setText(currentDeed.getText());
            categoryIcon.setImageResource(currentDeed.getCategoryIconResourceId());
        }
    }

    private void completeDeed() {
        // Vibrate as feedback
        vibrateForCompletion();

        // Make sure coin is visible and reset its properties before animation
        coinAnimation.setAlpha(1.0f);  // Make fully opaque
        coinAnimation.setVisibility(View.VISIBLE);  // Ensure visibility

        // Show coin animation
        AnimationUtils.playCoinAnimation(coinAnimation, () -> {
            // You can add additional code here that runs after the animation completes
            // For example, load the next deed
            loadNextDeed();
        });

        // Save completed deed to database
        saveDeedCompletion();

        // Show completion message
        Toast.makeText(this, R.string.deed_completed, Toast.LENGTH_SHORT).show();
    }

    // Method to load the next deed after completion
    private void loadNextDeed() {
        // In a real implementation, this would get the next deed from your repository
        // For now, we'll just reload the same deed
        loadTodaysDeed();
    }

    private void skipDeed() {
        if (skipsRemaining > 0) {
            skipsRemaining--;

            // Show skip message
            Toast.makeText(this, R.string.deed_skipped, Toast.LENGTH_SHORT).show();

            // Load next deed
            loadNextDeed();

        } else {
            // No more skips available
            Toast.makeText(this, R.string.max_skips_reached, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDeedCompletion() {
        // Get current date in the required format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Create a completed deed object
        CompletedDeed completedDeed = new CompletedDeed(
                currentDeed.getId(),
                false,  // Not a custom deed
                currentDate,
                null    // No note for now
        );

        // TODO: Save to database via repository
        // In a real implementation, this would be:
        // deedRepository.saveDeedCompletion(completedDeed);
    }

    private void vibrateForCompletion() {
        if (vibrator != null && vibrator.hasVibrator()) {
            // Create a vibration pattern - vibrate for 100ms
            VibrationEffect vibrationEffect;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
            } else {
                // For older devices
                vibrator.vibrate(100);
            }
        }
    }

    @Override
    public void onSwipeRight() {
        // Complete deed on swipe right
        completeDeed();
    }

    @Override
    public void onSwipeLeft() {
        // Skip deed on swipe left
        skipDeed();
    }
}