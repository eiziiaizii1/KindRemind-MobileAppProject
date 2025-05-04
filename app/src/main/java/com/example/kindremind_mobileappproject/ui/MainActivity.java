package com.example.kindremind_mobileappproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kindremind_mobileappproject.R;
import com.example.kindremind_mobileappproject.data.DeedDataManager;
import com.example.kindremind_mobileappproject.model.CompletedDeed;
import com.example.kindremind_mobileappproject.model.Deed;
import com.example.kindremind_mobileappproject.ui.utils.AnimationUtils;
import com.example.kindremind_mobileappproject.ui.utils.SwipeGestureDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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

    // Data manager for deeds
    private DeedDataManager dataManager;

    // Skips counter
    private int skipsRemaining = 3;

    // Vibrator service
    private Vibrator vibrator;

    // For tracking touch move events for manual swipe animation
    private float dX = 0f;
    private static final float SWIPE_THRESHOLD = 0.25f; // Percentage of screen width
    private static final float ROTATION_FACTOR = 0.1f;

    // Key for storing current deed ID in saved instance state
    private static final String KEY_CURRENT_DEED_ID = "current_deed_id";

    // SharedPreferences for persisting deed ID between activity navigations
    private static final String PREFS_NAME = "KindRemindPrefs";
    private static final String PREF_CURRENT_DEED_ID = "current_deed_id";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize data manager
        dataManager = DeedDataManager.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize UI components
        initViews();

        // Set up swipe gesture detection
        setupSwipeDetection();

        // Set up click listeners
        setupClickListeners();

        // Set up bottom navigation
        setupBottomNavigation();

        // Load today's deed - check if we have a saved deed ID first (from instance state)
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_DEED_ID)) {
            int savedDeedId = savedInstanceState.getInt(KEY_CURRENT_DEED_ID);
            loadSpecificDeed(savedDeedId);
        } else {
            // Check if we have a saved deed ID in SharedPreferences (for navigation returns)
            int savedDeedId = sharedPreferences.getInt(PREF_CURRENT_DEED_ID, -1);
            if (savedDeedId != -1) {
                loadSpecificDeed(savedDeedId);
            } else {
                loadTodaysDeed();
            }
        }

        // Get vibrator service
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Make sure coin animation is initially invisible
        coinAnimation.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current deed ID so we can restore it later
        if (currentDeed != null) {
            outState.putInt(KEY_CURRENT_DEED_ID, currentDeed.getId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the current deed ID to SharedPreferences when leaving the activity
        if (currentDeed != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PREF_CURRENT_DEED_ID, currentDeed.getId());
            editor.apply();
        }
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
        // Enhanced manual swipe detection for smoother animations
        deedCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float screenWidth = getResources().getDisplayMetrics().widthPixels;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float displacement = newX - v.getLeft();

                        // Move the card
                        v.setTranslationX(displacement);

                        // Rotate slightly based on displacement
                        v.setRotation(displacement * ROTATION_FACTOR);
                        return true;

                    case MotionEvent.ACTION_UP:
                        float translationX = v.getTranslationX();
                        float threshold = screenWidth * SWIPE_THRESHOLD;

                        if (translationX > threshold) {
                            // Swipe right - complete deed
                            AnimationUtils.animateSwipeRight(v, () -> completeDeed());
                        } else if (translationX < -threshold) {
                            // Swipe left - skip deed
                            AnimationUtils.animateSwipeLeft(v, () -> skipDeed());
                        } else {
                            // Return to original position
                            AnimationUtils.animateCardReturn(v, translationX);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void setupClickListeners() {
        btnComplete.setOnClickListener(v -> {
            // Animate the card on button press
            AnimationUtils.animateSwipeRight(deedCard, () -> completeDeed());
        });

        btnSkip.setOnClickListener(v -> {
            // Animate the card on button press
            AnimationUtils.animateSwipeLeft(deedCard, () -> skipDeed());
        });
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
        // Get a valid deed ID from the data manager
        Deed deed = getRandomDeedFromDataManager();
        if (deed != null) {
            currentDeed = deed;
        } else {
            // Fallback to a default deed if something went wrong
            currentDeed = new Deed(1, "environment", "Unplug one idle device to save energy.");
        }

        // Update UI with the deed details
        updateDeedUI();
    }

    /**
     * Load a specific deed by ID
     * @param deedId The ID of the deed to load
     */
    private void loadSpecificDeed(int deedId) {
        Deed deed = dataManager.getDeedById(deedId);
        if (deed != null) {
            currentDeed = deed;
            updateDeedUI();
        } else {
            // If the deed with the specified ID cannot be found, fall back to a random deed
            loadTodaysDeed();
        }
    }

    /**
     * Get a random deed from the DeedDataManager
     */
    private Deed getRandomDeedFromDataManager() {
        // In a real implementation, you might want to get a deed based on some criteria
        // For now, we'll get a random deed from the data manager's map
        if (dataManager != null) {
            // Get a random deed ID between 1 and 16 (based on your current setup)
            Random random = new Random();
            int randomDeedId = random.nextInt(16) + 1;

            // Try to get the deed with that ID
            Deed deed = dataManager.getDeedById(randomDeedId);

            // If we got a valid deed, return it
            if (deed != null) {
                return deed;
            }
        }

        // If we couldn't get a valid deed, return null and let the caller handle it
        return null;
    }

    private void updateDeedUI() {
        if (currentDeed != null) {
            deedText.setText(currentDeed.getText());

            // Set the category icon based on the deed's category
            int iconResId = R.drawable.ic_category_default; // Default icon

            // You can enhance this to use specific icons based on category
            switch (currentDeed.getCat().toLowerCase()) {
                case "environment":
                    iconResId = R.drawable.ic_category_default; // Replace with actual environment icon
                    break;
                case "empathy":
                    iconResId = R.drawable.ic_category_default; // Replace with actual empathy icon
                    break;
                case "community":
                    iconResId = R.drawable.ic_category_default; // Replace with actual community icon
                    break;
                case "health":
                    iconResId = R.drawable.ic_category_default; // Replace with actual health icon
                    break;
            }

            categoryIcon.setImageResource(iconResId);

            // Animate the new card
            AnimationUtils.animateNewCard(deedCard);
        }
    }

    private void completeDeed() {
        // Vibrate as feedback
        vibrateForCompletion();

        // Make sure coin is visible and reset its properties before animation
        coinAnimation.setAlpha(1.0f);
        coinAnimation.setVisibility(View.VISIBLE);

        // Show enhanced coin animation
        AnimationUtils.playCoinAnimation(coinAnimation, () -> {
            // Load the next deed after animation completes
            loadNextDeed();
        });

        // Save completed deed to database
        saveDeedCompletion();

        // Show completion message
        Toast.makeText(this, R.string.deed_completed, Toast.LENGTH_SHORT).show();
    }

    private void loadNextDeed() {
        // Get another random deed from the data manager
        Deed nextDeed = getRandomDeedFromDataManager();

        if (nextDeed != null) {
            currentDeed = nextDeed;
        } else {
            // Fallback to cycling through some hardcoded deeds if data manager fails
            int nextId = currentDeed.getId() + 1;
            String[] categories = {"environment", "empathy", "community", "health"};
            String[] deedTexts = {
                    "Pick up litter in your local park.",
                    "Compliment a stranger today.",
                    "Offer to help an elderly neighbor with groceries.",
                    "Try a new healthy recipe for dinner."
            };

            // Cycle through available categories and texts
            int categoryIndex = nextId % categories.length;
            int textIndex = nextId % deedTexts.length;

            currentDeed = new Deed(nextId, categories[categoryIndex], deedTexts[textIndex]);
        }

        // Update UI with the deed details
        updateDeedUI();

        // Save the new deed ID to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CURRENT_DEED_ID, currentDeed.getId());
        editor.apply();
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

            // Return card to original position
            AnimationUtils.animateCardReturn(deedCard, deedCard.getTranslationX());
        }
    }

    private void saveDeedCompletion() {
        // Get current date in the required format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Create a completed deed object using the current deed's ID
        CompletedDeed completedDeed = new CompletedDeed(
                currentDeed.getId(),  // Use the current deed's ID which exists in the DeedDataManager
                false,                // Not a custom deed
                currentDate,
                null                  // No note for now
        );

        // Generate a primary key (this would be handled by Room in the database implementation)
        completedDeed.setPk(dataManager.getCompletedDeedsWithDetails().size() + 1);

        // Save to our data manager
        dataManager.saveCompletedDeed(completedDeed);
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