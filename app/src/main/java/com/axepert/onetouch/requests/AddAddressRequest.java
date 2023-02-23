package com.axepert.onetouch.requests;

public class AddAddressRequest {
    private String user_id, url, name, email, mobile, address, landmark, city, pincode, state, id;

    public AddAddressRequest(String user_id, String url, String name, String email, String mobile, String address, String landmark, String city, String pincode, String state, String id) {
        this.user_id = user_id;
        this.url = url;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.landmark = landmark;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
        this.id = id;
    }
}
