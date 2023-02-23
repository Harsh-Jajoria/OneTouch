package com.axepert.onetouch.requests;

public class ProductDetailsRequest {
    private String url, slug;

    public ProductDetailsRequest(String url, String slug) {
        this.url = url;
        this.slug = slug;
    }
}
