package com.example.kindremind_mobileappproject.model;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a completed deed
 */
@Entity(tableName = "deed_log")
public class CompletedDeed {
    @PrimaryKey(autoGenerate = true)
    private int pk;
    private int deedId;
    private boolean custom;
    private String date;
    private String note;

    public CompletedDeed(int deedId, boolean custom, String date, String note) {
        this.deedId = deedId;
        this.custom = custom;
        this.date = date;
        this.note = note;
    }

    // Getters and setters
    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getDeedId() {
        return deedId;
    }

    public void setDeedId(int deedId) {
        this.deedId = deedId;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}