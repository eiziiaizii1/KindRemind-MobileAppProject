package com.example.kindremind_mobileappproject.ui;   // adjust if your root package differs

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kindremind_mobileappproject.R;

/**
 * Shows the splash logo with a short animation and then launches MainActivity.
 * Defined in the project brief as: “SplashActivity → MainActivity (automatic after animation)” :contentReference[oaicite:0]{index=0}:contentReference[oaicite:1]{index=1}
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the theme with a static windowBackground so the logo is visible immediately.
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splashLogo);

        // Fade-in / scale animation (res/anim/splash_logo_anim.xml)
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }

            @Override public void onAnimationRepeat(Animation animation) { }

            @Override public void onAnimationEnd(Animation animation) {
                // Animation finished → open the main screen
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish(); // don’t return to splash on Back press
            }
        });

        logo.startAnimation(anim);
    }
}
