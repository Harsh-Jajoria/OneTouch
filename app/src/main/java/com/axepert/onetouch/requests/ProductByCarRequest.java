package com.axepert.onetouch.requests;

public class ProductByCarRequest {
    private String url, offset, limit, category;

    public ProductByCarRequest(String url, String offset, String limit, String category) {
        this.url = url;
        this.offset = offset;
        this.limit = limit;
        this.category = category;
    }
}
