package com.example.kindremind_mobileappproject.ui.utils;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Utility class for animations used in the app
 */
public class AnimationUtils {

    /**
     * Plays a coin animation on the provided view
     * @param coinView View to animate
     * @param onAnimationEnd Optional runnable to execute when animation completes
     */
    public static void playCoinAnimation(View coinView, Runnable onAnimationEnd) {
        // Show the coin view
        coinView.setVisibility(View.VISIBLE);

        coinView.bringToFront();

        // Reset position if needed
        coinView.setTranslationY(0);
        coinView.setScaleX(0.3f);
        coinView.setScaleY(0.3f);

        // Scale up animation
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(coinView, "scaleX", 0.3f, 1.2f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(coinView, "scaleY", 0.3f, 1.2f);

        // Move up animation
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(
                coinView, "translationY", 0, -300);

        // Fade out animation
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(coinView, "alpha", 1, 0);

        // Create animator set for first part of animation
        AnimatorSet firstSet = new AnimatorSet();
        firstSet.playTogether(scaleXAnimator, scaleYAnimator);
        firstSet.setDuration(300);
        firstSet.setInterpolator(new DecelerateInterpolator());

        // Create animator set for second part of animation
        AnimatorSet secondSet = new AnimatorSet();
        secondSet.playTogether(translationYAnimator, alphaAnimator);
        secondSet.setDuration(500);
        secondSet.setInterpolator(new AccelerateInterpolator());

        // Combine both animations to play sequentially
        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(firstSet, secondSet);

        // Run the callback when animation ends
        fullAnimation.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                coinView.setVisibility(View.INVISIBLE);
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });

        // Start the animation
        fullAnimation.start();
    }
}