package com.example.kindremind_mobileappproject.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

public class AnimationUtils {

    /**
     * Animates a card swiping right (for deed completion)
     * Card will move off-screen and NOT return
     */
    public static void animateSwipeRight(View view, final Runnable onComplete) {
        float screenWidth = view.getContext().getResources().getDisplayMetrics().widthPixels;

        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", screenWidth * 1.5f);
        translationX.setDuration(300);
        translationX.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 10f);
        rotation.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, rotation);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onComplete != null) {
                    onComplete.run();
                }

                // Do NOT reset the card's position - the next card will appear with loadNextDeed()
            }
        });

        animatorSet.start();
    }

    /**
     * Animates a card swiping left (for deed skipping)
     * Card will move off-screen and NOT return
     */
    public static void animateSwipeLeft(View view, final Runnable onComplete) {
        float screenWidth = view.getContext().getResources().getDisplayMetrics().widthPixels;

        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -screenWidth * 1.5f);
        translationX.setDuration(300);
        translationX.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", -10f);
        rotation.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, rotation);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onComplete != null) {
                    onComplete.run();
                }

                // Do NOT reset the card's position - the next card will appear with loadNextDeed()
            }
        });

        animatorSet.start();
    }

    /**
     * Animates a card returning to its original position
     */
    public static void animateCardReturn(View view, float currentTranslation) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", 0f);
        translationX.setDuration(200);
        translationX.setInterpolator(new OvershootInterpolator());

        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0f);
        rotation.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, rotation);
        animatorSet.start();
    }

    /**
     * Animates a new card appearing
     */
    public static void animateNewCard(View view) {
        // Reset view properties first
        view.setAlpha(0f);
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        view.setTranslationX(0f);  // Ensure card appears in center
        view.setRotation(0f);      // Reset rotation

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(300);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        scaleX.setDuration(300);
        scaleX.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        scaleY.setDuration(300);
        scaleY.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn, scaleX, scaleY);
        animatorSet.start();
    }

    /**
     * Plays a coin animation
     */
    public static void playCoinAnimation(final View coinView, final Runnable onComplete) {
        // Ensure coin is visible
        coinView.setVisibility(View.VISIBLE);

        ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(coinView, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(coinView, "scaleY", 1f, 1.5f);

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(coinView, "translationY", 0f, -200f);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(coinView, "alpha", 1f, 0f);
        fadeOut.setStartDelay(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXUp, scaleYUp, moveUp, fadeOut);
        animatorSet.setDuration(700);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                coinView.setVisibility(View.INVISIBLE);
                coinView.setTranslationY(0f);
                coinView.setScaleX(1f);
                coinView.setScaleY(1f);

                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        animatorSet.start();
    }
}