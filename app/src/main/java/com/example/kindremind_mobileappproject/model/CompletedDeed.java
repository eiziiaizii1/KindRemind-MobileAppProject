package com.example.kindremind_mobileappproject.model;
public class CompletedDeed {

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

    // sqlite database only hold 0 / 1 for booleans
    public int getCustomIntValue(){
        if (this.custom)
            return 1;
        else
            return 0;

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