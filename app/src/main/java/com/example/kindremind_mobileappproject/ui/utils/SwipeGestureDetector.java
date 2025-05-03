package com.example.kindremind_mobileappproject.ui.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Utility class to detect swipe gestures on a view
 */
public class SwipeGestureDetector implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public SwipeGestureDetector(Context context, OnSwipeListener onSwipeListener) {
        gestureDetector = new GestureDetector(context, new GestureListener(onSwipeListener));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private final OnSwipeListener onSwipeListener;

        GestureListener(OnSwipeListener onSwipeListener) {
            this.onSwipeListener = onSwipeListener;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (e1 == null || e2 == null) return false;

                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(diffY) &&
                        Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

                    if (diffX > 0) {
                        onSwipeListener.onSwipeRight();
                    } else {
                        onSwipeListener.onSwipeLeft();
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public interface OnSwipeListener {
        void onSwipeRight();
        void onSwipeLeft();
    }
}