package com.axepert.onetouch.requests;

public class RelatedProductRequest {
    private String url, slug, limit, offset;

    public RelatedProductRequest(String url, String slug, String limit, String offset) {
        this.url = url;
        this.slug = slug;
        this.limit = limit;
        this.offset = offset;
    }
}
