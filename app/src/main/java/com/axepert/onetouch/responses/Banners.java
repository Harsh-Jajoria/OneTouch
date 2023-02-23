package com.axepert.onetouch.responses;

public class Banners {
    public String id;
    public String title;
    public String subtitle;
    public String image;

    public Banners(String id, String title, String subtitle, String image) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImage() {
        return image;
    }
}
