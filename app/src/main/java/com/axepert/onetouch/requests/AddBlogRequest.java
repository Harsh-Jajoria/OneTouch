package com.axepert.onetouch.requests;

public class AddBlogRequest {
    private String name, email, contact, heading, description, category_name, image;

    public AddBlogRequest(String name, String email, String contact, String heading, String description, String category_name, String image) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.heading = heading;
        this.description = description;
        this.category_name = category_name;
        this.image = image;
    }
}
