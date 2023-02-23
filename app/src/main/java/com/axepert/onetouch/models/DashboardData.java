package com.axepert.onetouch.models;

public class DashboardData {
    private int colorId;
    private String title, count;

    public DashboardData(int colorId, String title, String count) {
        this.colorId = colorId;
        this.title = title;
        this.count = count;
    }

    public int getColorId() {
        return colorId;
    }

    public String getTitle() {
        return title;
    }

    public String getCount() {
        return count;
    }
}
