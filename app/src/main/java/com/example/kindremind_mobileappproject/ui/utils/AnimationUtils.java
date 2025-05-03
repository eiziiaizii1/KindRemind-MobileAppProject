package com.example.kindremind_mobileappproject.ui.utils;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Utility class for animations used in the app
 */
public class AnimationUtils {

    /**
     * Plays an enhanced coin animation with particle effects
     * @param coinView View to animate
     * @param onAnimationEnd Optional runnable to execute when animation completes
     */
    public static void playCoinAnimation(View coinView, Runnable onAnimationEnd) {
        // Show the coin view
        coinView.setVisibility(View.VISIBLE);
        coinView.bringToFront();

        // Reset position
        coinView.setTranslationY(0);
        coinView.setTranslationX(0);
        coinView.setScaleX(0.3f);
        coinView.setScaleY(0.3f);
        coinView.setRotation(0f);
        coinView.setAlpha(1.0f);

        // Phase 1: Appear with impact
        ObjectAnimator scaleXAnimator1 = ObjectAnimator.ofFloat(coinView, "scaleX", 0.3f, 1.4f, 1.0f);
        ObjectAnimator scaleYAnimator1 = ObjectAnimator.ofFloat(coinView, "scaleY", 0.3f, 1.4f, 1.0f);
        ObjectAnimator rotationAnimator1 = ObjectAnimator.ofFloat(coinView, "rotation", 0f, 180f);

        AnimatorSet appearSet = new AnimatorSet();
        appearSet.playTogether(scaleXAnimator1, scaleYAnimator1, rotationAnimator1);
        appearSet.setDuration(350);
        appearSet.setInterpolator(new OvershootInterpolator(1.5f));

        // Phase 2: Float with subtle movement
        ObjectAnimator floatYAnimator = ObjectAnimator.ofFloat(coinView, "translationY", 0, -20, 0, -10, 0);
        ObjectAnimator rotationAnimator2 = ObjectAnimator.ofFloat(coinView, "rotation", 180f, 540f);
        ObjectAnimator scaleXAnimator2 = ObjectAnimator.ofFloat(coinView, "scaleX", 1.0f, 1.1f, 1.0f);
        ObjectAnimator scaleYAnimator2 = ObjectAnimator.ofFloat(coinView, "scaleY", 1.0f, 1.1f, 1.0f);

        AnimatorSet floatSet = new AnimatorSet();
        floatSet.playTogether(floatYAnimator, rotationAnimator2, scaleXAnimator2, scaleYAnimator2);
        floatSet.setDuration(600);
        floatSet.setInterpolator(new DecelerateInterpolator());

        // Phase 3: Celebration - fly up and fade
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(coinView, "translationY", 0, -400);
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(coinView, "translationX", 0, 50, -50, 0);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(coinView, "alpha", 1.0f, 0.0f);
        ObjectAnimator rotationAnimator3 = ObjectAnimator.ofFloat(coinView, "rotation", 540f, 900f);

        AnimatorSet celebrationSet = new AnimatorSet();
        celebrationSet.playTogether(translationYAnimator, translationXAnimator, alphaAnimator, rotationAnimator3);
        celebrationSet.setDuration(700);
        celebrationSet.setInterpolator(new AccelerateInterpolator(0.8f));

        // Combine all animations
        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(appearSet, floatSet, celebrationSet);

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

    /**
     * Animates a card swipe to the right (complete action) with enhanced feedback
     * @param cardView The card view to animate
     * @param onAnimationEnd Optional runnable to execute when animation completes
     */
    public static void animateSwipeRight(View cardView, Runnable onAnimationEnd) {
        float screenWidth = cardView.getContext().getResources().getDisplayMetrics().widthPixels;

        // First slightly pull back before swiping (for more dynamic feel)
        ObjectAnimator pullBack = ObjectAnimator.ofFloat(
                cardView, "translationX", cardView.getTranslationX(), -50f);
        ObjectAnimator rotateBack = ObjectAnimator.ofFloat(
                cardView, "rotation", cardView.getRotation(), -5f);

        AnimatorSet pullBackSet = new AnimatorSet();
        pullBackSet.playTogether(pullBack, rotateBack);
        pullBackSet.setDuration(100);
        pullBackSet.setInterpolator(new AnticipateInterpolator(1.5f));

        // Then swipe out with acceleration
        ObjectAnimator translationX = ObjectAnimator.ofFloat(
                cardView, "translationX", -50f, screenWidth);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(
                cardView, "rotation", -5f, 15f);
        ObjectAnimator scaleDown = ObjectAnimator.ofFloat(
                cardView, "scaleY", 1f, 0.9f);

        AnimatorSet swipeSet = new AnimatorSet();
        swipeSet.playTogether(translationX, rotation, scaleDown);
        swipeSet.setDuration(300);
        swipeSet.setInterpolator(new AccelerateInterpolator(1.2f));

        // Combine animations
        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(pullBackSet, swipeSet);

        fullAnimation.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // Reset card position
                cardView.setTranslationX(0f);
                cardView.setRotation(0f);
                cardView.setScaleY(1f);

                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });

        fullAnimation.start();
    }

    /**
     * Animates a card swipe to the left (skip action) with enhanced feedback
     * @param cardView The card view to animate
     * @param onAnimationEnd Optional runnable to execute when animation completes
     */
    public static void animateSwipeLeft(View cardView, Runnable onAnimationEnd) {
        float screenWidth = cardView.getContext().getResources().getDisplayMetrics().widthPixels;

        // First slightly pull back before swiping (for more dynamic feel)
        ObjectAnimator pullBack = ObjectAnimator.ofFloat(
                cardView, "translationX", cardView.getTranslationX(), 50f);
        ObjectAnimator rotateBack = ObjectAnimator.ofFloat(
                cardView, "rotation", cardView.getRotation(), 5f);

        AnimatorSet pullBackSet = new AnimatorSet();
        pullBackSet.playTogether(pullBack, rotateBack);
        pullBackSet.setDuration(100);
        pullBackSet.setInterpolator(new AnticipateInterpolator(1.5f));

        // Then swipe out with acceleration
        ObjectAnimator translationX = ObjectAnimator.ofFloat(
                cardView, "translationX", 50f, -screenWidth);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(
                cardView, "rotation", 5f, -15f);
        ObjectAnimator scaleDown = ObjectAnimator.ofFloat(
                cardView, "scaleY", 1f, 0.9f);

        AnimatorSet swipeSet = new AnimatorSet();
        swipeSet.playTogether(translationX, rotation, scaleDown);
        swipeSet.setDuration(300);
        swipeSet.setInterpolator(new AccelerateInterpolator(1.2f));

        // Combine animations
        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(pullBackSet, swipeSet);

        fullAnimation.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // Reset card position
                cardView.setTranslationX(0f);
                cardView.setRotation(0f);
                cardView.setScaleY(1f);

                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });

        fullAnimation.start();
    }

    /**
     * Animates the card when returning to its original position (incomplete swipe)
     * @param cardView The card view to animate
     * @param currentTranslation Current X translation value
     */
    public static void animateCardReturn(View cardView, float currentTranslation) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(
                cardView, "translationX", currentTranslation, 0f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(
                cardView, "rotation", cardView.getRotation(), 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, rotation);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new OvershootInterpolator(0.8f));
        animatorSet.start();
    }

    /**
     * Animates a new card entering the screen
     * @param cardView The card view to animate
     */
    public static void animateNewCard(View cardView) {
        cardView.setAlpha(0f);
        cardView.setScaleX(0.85f);
        cardView.setScaleY(0.85f);
        cardView.setTranslationY(100f);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", 0.85f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 0.85f, 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(cardView, "translationY", 100f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alpha, scaleX, scaleY, translationY);
        animatorSet.setDuration(350);
        animatorSet.setInterpolator(new DecelerateInterpolator(1.2f));
        animatorSet.start();
    }
}