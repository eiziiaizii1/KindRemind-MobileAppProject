package com.example.kindremind_mobileappproject.ui.adapters;

import com.example.kindremind_mobileappproject.R;

/**
 * Helper class that combines CompletedDeed with associated Deed information
 * for display in the history list
 */
public class CompletedDeedWithDetails {
    private int id;
    private String deedText;
    private String category;
    private String date;
    private String note;
    private boolean isCustom;

    public CompletedDeedWithDetails(int id, String deedText, String category, String date, String note, boolean isCustom) {
        this.id = id;
        this.deedText = deedText;
        this.category = category;
        this.date = date;
        this.note = note;
        this.isCustom = isCustom;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDeedText() {
        return deedText;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public boolean isCustom() {
        return isCustom;
    }

    /**
     * Get the appropriate icon resource ID based on the category
     */
    public int getCategoryIconResourceId() {
        switch (category.toLowerCase()) {
            case "environment":
                return R.drawable.ic_category_environment; // Replace with actual icon
            case "empathy":
                return R.drawable.ic_category_empathy; // Replace with actual icon
            case "community":
                return R.drawable.ic_category_community; // Replace with actual icon
            case "health":
                return R.drawable.ic_category_health; // Replace with actual icon
            default:
                return com.example.kindremind_mobileappproject.R.drawable.ic_category_default;
        }
    }
}