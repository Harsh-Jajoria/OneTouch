package com.axepert.onetouch.requests;

public class ProductListRequest {
    private String url, limit, offset, sort;

    public ProductListRequest(String url, String limit, String offset, String sort) {
        this.url = url;
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }
}
