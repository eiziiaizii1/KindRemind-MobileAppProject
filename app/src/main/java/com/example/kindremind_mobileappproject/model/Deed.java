package com.example.kindremind_mobileappproject.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a Deed
 */
@Entity(tableName = "deeds_remote")
public class Deed {
    @PrimaryKey
    private int id;
    private String cat;
    private String text;

    public Deed(int id, String cat, String text) {
        this.id = id;
        this.cat = cat;
        this.text = text;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the drawable resource id for the category icon
     * @return Resource ID for the appropriate drawable
     */
    public int getCategoryIconResourceId() {
        // This would map to your actual drawable resources
        switch (cat.toLowerCase()) {
            case "environment":
                return android.R.drawable.ic_menu_compass; // Replace with your actual icons
            case "empathy":
                return android.R.drawable.ic_menu_share;
            case "community":
                return android.R.drawable.ic_menu_myplaces;
            case "health":
                return android.R.drawable.ic_menu_agenda;
            default:
                return android.R.drawable.ic_menu_help;
        }
    }
}