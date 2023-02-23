package com.axepert.onetouch.requests;

public class AddReviewRequest {
    private String seller_id, name, email, contact, review;

    public AddReviewRequest(String seller_id, String name, String email, String contact, String review) {
        this.seller_id = seller_id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.review = review;
    }
}
