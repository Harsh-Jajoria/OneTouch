package com.axepert.onetouch.requests;

public class EditProfileRequest {
    private String url, name, mobile, id;

    public EditProfileRequest(String url, String name, String mobile, String id) {
        this.url = url;
        this.name = name;
        this.mobile = mobile;
        this.id = id;
    }
}
