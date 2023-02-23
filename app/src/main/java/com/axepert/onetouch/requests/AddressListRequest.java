package com.axepert.onetouch.requests;

public class AddressListRequest {
    private String user_id, url;

    public AddressListRequest(String user_id, String url) {
        this.user_id = user_id;
        this.url = url;
    }
}
