package com.axepert.onetouch.requests;

public class SearchProductRequest {
    private String url, item;

    public SearchProductRequest(String url, String item) {
        this.url = url;
        this.item = item;
    }
}
